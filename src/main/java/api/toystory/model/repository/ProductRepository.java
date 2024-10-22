package api.toystory.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import api.toystory.model.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
