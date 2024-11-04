package api.toystory.controller;

import api.toystory.dto.response.WrapperResponseDTO;
import api.toystory.model.entity.Category;
import api.toystory.model.entity.Product;
import api.toystory.model.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE) // Alterar para JSON
@CrossOrigin(origins = "*")  // Libera o CORS para este controlador
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // Criar uma nova categoria
    @PostMapping("/register")
    public WrapperResponseDTO<Category> createCategory(@RequestBody Category category) {
    	
        if(categoryRepository.findByNome(category.getNome()) != null) {
        	return new WrapperResponseDTO<Category>(false,"A categoria já existe.", null);
        }
        
        categoryRepository.save(category);
        
        return new WrapperResponseDTO<Category>(true,"Categoria foi registada com sucesso!", category);
    }

    // Obter todas as categorias
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    // Obter uma categoria por ID
    @GetMapping("/id/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Obter uma categoria por nome
    @GetMapping("/nome/{nome}")
    public ResponseEntity<Category> getCategoryByNome(@PathVariable String nome) {
        Category category = categoryRepository.findByNome(nome);
        return category != null ? ResponseEntity.ok(category) : ResponseEntity.notFound().build();
    }

    // Atualizar uma categoria
    @PutMapping("/edit/{id}")
    public WrapperResponseDTO<Category> updateCategory(@PathVariable Integer id, @RequestBody Category category) {
    	
        if (!categoryRepository.existsById(id)) {
        	return new WrapperResponseDTO<Category>(false,"Categoria não existe.", category);
        }
        
        
        
        category.setId(id);
        categoryRepository.save(category);
        
        return new WrapperResponseDTO<Category>(true,"Categoria foi atualizado com sucesso!", category);
    }

    // Deletar uma categoria
    @DeleteMapping("/delete/{id}")
    public WrapperResponseDTO<Category> deleteCategory(@PathVariable Integer id) {
    	
        if (!categoryRepository.existsById(id)) {
        	return new WrapperResponseDTO<Category>(true,"Categoria foi deletada com sucesso!", null);
        }

        
        categoryRepository.deleteById(id);
        
        return new WrapperResponseDTO<Category>(true,"Categoria foi deletada com sucesso!", null);
    }
}