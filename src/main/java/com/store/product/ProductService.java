package com.store.product;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.store.entity.Product;

@Service
public class ProductService {
	public static final int PRODUCTS_PER_PAGE = 40;
	
	@Autowired private ProductRepository repo;
	
	public List<Product> listAll(){
		return (List<Product>) repo.findAll();
	}
	
	public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum-1, PRODUCTS_PER_PAGE, sort);
		
		if (keyword != null) {
			return repo.findAll(keyword, pageable);
		}
		
		return repo.findAll(pageable);
	}

	public Product save(Product product) {
		return repo.save(product);
		
	}

	public Product get(Integer id) throws ProductNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ProductNotFoundException("Could not find any product with ID " + id);
		}
	}

	public List<Product> listAllPots() {
		
		return repo.findAllPots();
	}

	public List<Product> listAllSoil() {
		
		return repo.findByCategory(2);
	}

	public List<Product> listAllPlants() {
		return repo.findByCategory(1);
	}

}
