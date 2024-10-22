package api.toystory.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale.Category;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import api.toystory.model.entity.Product;
import api.toystory.model.repository.ProductRepository;

@RestController
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	private ProductRepository repo;
	
	private static String UPLOAD_DIR = "E:/programas/laragon/www/toystory-backend/src/main/resources/static/images";
	
	@GetMapping("/")
	public List<Product> listAll() {
		return repo.findAll();
	}
	
	@GetMapping("/{id}")
	public Product getById(@PathVariable("id") Integer id) {
		
		Product product = repo.findById(id).get();
		return product;	
		
	}
	
	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		
		repo.deleteById(id);
		
		return "Produto deletado!";
		
	}
	
	@PostMapping("/register")
	public String insert(@RequestBody Product product, @RequestParam("imagem") MultipartFile image) {
		
        if (image.isEmpty()) {
            return "A imagem é obrigatória";
        }
        
        try {
        
	        File directory = new File(UPLOAD_DIR);
	        
	        if (!directory.exists()) 
	        	if (!directory.mkdirs()) return "Erro ao criar arquivo de upload.";  
	        
	        String extension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
	        
	        // Verificando se a extensão é válida
	        if (!extension.equalsIgnoreCase(".png") && 
	            !extension.equalsIgnoreCase(".jpg") && 
	            !extension.equalsIgnoreCase(".jpeg")) {
	            return "Erro: Tipo de imagem inválido. Aceitos: .png, .jpg, .jpeg.";
	        }
	        
	        //salvando imagem
        	
	        byte[] bytes = image.getBytes();//recupeando o tamanho dela..
	        String imageName = UUID.randomUUID() + ".jpeg";
	        Path path = Paths.get(UPLOAD_DIR + imageName);//criando o caminho completo dela com o nome dela
	        Files.write(path, bytes);//escrendo no servidor..
			
	        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
	                .path("/images/")
	                .path(imageName)
	                .toUriString();
	        
	        product.setImagem(imageUrl);
	        
        }catch(Exception ex) {
        	
        	ex.printStackTrace();
        	return ex.toString();
        	
        }

		repo.save(product);
		
		return "Produto foi registrado!";
		
	}
	
	@PutMapping("edit/{id}")
	public String update(@RequestParam("imagem") MultipartFile image, 
	                     @RequestParam("nome") String nome, 
	                     @RequestParam("descricao") String descricao, 
	                     @RequestParam("preco") float preco,
	                     @RequestParam("categoria") Integer idCategoria,
	                     @RequestParam("marca") String marca,
	                     @RequestParam("quantidade") Integer quantidade,
	                     @RequestParam("detalhes") String detalhes,
	                     @PathVariable("id") Integer id) {
	    
	    // Recuperar o produto existente do banco de dados
	    Product productUpdated = repo.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
	    
	    // Atualizar os campos do produto
	    productUpdated.setNome(nome);
	    productUpdated.setDescricao(descricao);
	    productUpdated.setPreco(preco);
	    productUpdated.setCategoria(idCategoria);
	    productUpdated.setMarca(marca);
	    productUpdated.setQuantidade(quantidade);
	    productUpdated.setDetalhes(detalhes);
	    
	    // Se uma nova imagem for enviada, processar e atualizar a URL
	    if (image != null && !image.isEmpty()) {
	        // Salvar a nova imagem
	        try {
	            // Gerar um nome único para a imagem
	            String originalFileName = image.getOriginalFilename();
	            String extension = "";

	            if (originalFileName != null && originalFileName.contains(".")) {
	                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
	            }

	            String imageName = UUID.randomUUID().toString() + extension;
	            Path path = Paths.get(UPLOAD_DIR, imageName);
	            Files.write(path, image.getBytes());
	            
	            // Atualizar a URL da imagem no produto
	            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
	                    .path("/images/")
	                    .path(imageName)
	                    .toUriString();
	            productUpdated.setImagem(imageUrl); // Atualiza o campo de imagem do produto
	            
	        } catch (IOException ex) {
	            ex.printStackTrace();
	            return "Erro ao salvar a imagem: " + ex.getMessage();
	        }
	    }

	    // Salvar as alterações no banco de dados
	    repo.save(productUpdated);
	    
	    return "Produto atualizado com sucesso!";
	}

	
}
