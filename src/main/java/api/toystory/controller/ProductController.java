package api.toystory.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale.Category;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import api.toystory.dto.response.WrapperResponseDTO;
import api.toystory.model.entity.Product;
import api.toystory.model.repository.ProductRepository;

@RestController
@RequestMapping(value = "/product")
@CrossOrigin(origins = "*")  // Libera o CORS para este controlador
public class ProductController {
	
	@Autowired
	private ProductRepository repo;
	
	private static String UPLOAD_DIR = "E:/programas/laragon/www/toystory-backend/src/main/resources/static/images";
	
	@GetMapping("/")
	@ResponseBody
	public List<Product> listAll() {
	    return repo.findAll();
	}
	
	@GetMapping("/{id}")
	public Product getById(@PathVariable("id") Integer id) {
		
		Product product = repo.findById(id).get();
		return product;	
		
	}
	
	@DeleteMapping("/delete/{id}")
	public boolean delete(@PathVariable("id") Integer id) {
		
		if(!repo.existsById(id)) return false;
		
		repo.deleteById(id);
		
		return true;
		
	}
	
	@PostMapping(value = "/register", consumes = {"multipart/form-data"}, produces = "application/json")
	@ResponseBody
	public WrapperResponseDTO<Product> insert(
	        @RequestParam String nome,
	        @RequestParam String descricao,
	        @RequestParam Integer quantidade,
	        @RequestParam String detalhes,
	        @RequestParam String marca,
	        @RequestParam BigDecimal preco,
	        @RequestParam String categoria,
	        @RequestParam(required = false) MultipartFile imagem) {

	    try {
	        // Verifica se a imagem foi fornecida
	        if (imagem == null || imagem.isEmpty()) {
	            return new WrapperResponseDTO<>(false, "Imagem é obrigatória.", null);
	        }

	        // Cria o diretório de upload caso ele não exista
	        File directory = new File(UPLOAD_DIR);
	        if (!directory.exists() && !directory.mkdirs()) {
	            return new WrapperResponseDTO<>(false, "Erro ao criar diretório de upload.", null);
	        }

	        // Valida o tipo da imagem
	        String extension = imagem.getOriginalFilename().substring(imagem.getOriginalFilename().lastIndexOf("."));
	        if (!extension.equalsIgnoreCase(".png") &&
	            !extension.equalsIgnoreCase(".jpg") &&
	            !extension.equalsIgnoreCase(".jpeg")) {

	            return new WrapperResponseDTO<>(false, "Erro: Tipo de imagem inválido. Aceitos: .png, .jpg, .jpeg.", null);
	        }

	        // Salva a imagem
	        byte[] bytes = imagem.getBytes();
	        String imageName = UUID.randomUUID() + extension;
	        Path path = Paths.get(UPLOAD_DIR + "/" + imageName);
	        Files.write(path, bytes);

	        // Constrói a URL da imagem
	        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
	                .path("/images/")
	                .path(imageName)
	                .toUriString();

	        // Cria e salva o objeto Product
	        Product product = new Product();
	        product.setNome(nome);
	        product.setDescricao(descricao);
	        product.setQuantidade(quantidade);
	        product.setDetalhes(detalhes);
	        product.setMarca(marca);
	        product.setPreco(preco);
	        product.setCategoria(categoria);
	        product.setImagem(imageUrl);

	        repo.save(product);

	        return new WrapperResponseDTO<>(true, "Produto registrado.", product);

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return new WrapperResponseDTO<>(false, "Erro interno: " + ex.getMessage(), null);
	    }
	}

	@PutMapping(value = "/edit/{id}", consumes = {"multipart/form-data"}, produces = "application/json")
	@ResponseBody
    public WrapperResponseDTO<Product> updateProduct(
            @PathVariable Integer id,
            @RequestParam String nome,
            @RequestParam String descricao,
            @RequestParam Integer quantidade,
            @RequestParam String detalhes,
            @RequestParam String marca,
            @RequestParam BigDecimal preco,
            @RequestParam String categoria,
            @RequestParam(required = false) MultipartFile imagem) {

        Optional<Product> productOptional = repo.findById(id);
        
        if (productOptional.isEmpty()) {
        	return new WrapperResponseDTO<Product>(false,"Produto não encontrado.", null);
        }

        Product product = productOptional.get();
        product.setNome(nome);
        product.setDescricao(descricao);
        product.setQuantidade(quantidade);
        product.setDetalhes(detalhes);
        product.setMarca(marca);
        product.setPreco(preco);
        product.setCategoria(categoria);

        // Processa o upload de imagem se um arquivo de imagem for enviado
        if (imagem != null && !imagem.isEmpty()) {
            String randomFileName = UUID.randomUUID().toString() + "_" + imagem.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, randomFileName);

            try {
                // Cria o diretório de upload, se necessário
                Files.createDirectories(filePath.getParent());
                // Salva a imagem no diretório especificado
                Files.write(filePath, imagem.getBytes());

                // Atualiza o caminho da imagem no produto
                product.setImagem(randomFileName);

            } catch (IOException e) {
            	return new WrapperResponseDTO<Product>(false,e.toString(), null);
            }
        }

        // Salva o produto atualizado no repositório
        Product updatedProduct = repo.save(product);
        return new WrapperResponseDTO<Product>(true,"Produto foi atualizado com sucesso!", updatedProduct);
    }
	
	
}
