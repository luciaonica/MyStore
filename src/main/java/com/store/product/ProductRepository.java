package com.store.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.store.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	@Query("Select p from Product p where p.description like %?1% or p.name like %?1% or p.category.name like %?1%")
	Page<Product> findAll(String keyword, Pageable pageable);

	@Query("select p from Product p where p.category.id = ?1 ")
	public List<Product> findByCategory(Integer categpryId);

	@Query("select p from Product p where p.category.id = 4 or p.category.id = 3 ")
	public List<Product> findAllPots();
}
