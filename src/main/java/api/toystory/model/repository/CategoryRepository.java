package api.toystory.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import api.toystory.model.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	public Category findByNome(String nome);
	
}
