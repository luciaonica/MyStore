package com.store.Category;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.store.entity.Category;
import com.store.product.ProductRepository;

@Service
public class CategoryService {
public static final int ROOT_CATEGORIES_PER_PAGE = 5;
	
	@Autowired
	private CategoryRepository repo;
	
	@Autowired
	private ProductRepository productRepo;
	
	public List<Category> listAll(){
		return (List<Category>) repo.findAll();
	}
	
	public Page<Category> listByPage(int pageNum, String sortDir, String keyword) {
		Sort sort = Sort.by("name");
		
		if (sortDir.equals("asc")) {
			sort = sort.ascending();
		} else if (sortDir.equals("desc")) {
			sort = sort.descending();
		}
		
		Pageable pageable = PageRequest.of(pageNum - 1, ROOT_CATEGORIES_PER_PAGE, sort);
		
		if (keyword != null && !keyword.isEmpty()) {
			return  repo.search(keyword, pageable);	
		} 
		
		return repo.findAll(pageable);
	}
		
	public Category save(Category category) {	
		
		return  repo.save(category);
	}
	
	public Category get(Integer id) throws CategoryNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CategoryNotFoundException("Could not find any category with ID " + id);
		}
	}	
	
	public void delete(Integer id) throws CategoryNotFoundException {
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new CategoryNotFoundException("Could not find any category with ID " + id);
		}
		
		if (productRepo.findByCategory(id).size()>0) {			
			throw new CategoryNotFoundException("Could not delete category with ID " + id + 
					". There are some products in this category.");
		}
		
		repo.deleteById(id);
	}	
}
