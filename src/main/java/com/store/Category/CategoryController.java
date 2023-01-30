package com.store.Category;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.store.FileUploadUtil;
import com.store.entity.Category;

@Controller
public class CategoryController {	
	private String defaultRedirectURL = "redirect:/categories/page/1?sortField=name&sortDir=asc";
	
	@Autowired
	private CategoryService service;
	
	@GetMapping("/categories")
	public String listFirstPage(Model model) {
		return defaultRedirectURL;
	}
	
	@GetMapping("/categories/page/{pageNum}") 
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, 
			@Param("sortDir") String sortDir, 
			@Param("keyword") String keyword,
			Model model) {
		if (sortDir ==  null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		Page<Category> page = service.listByPage(pageNum, sortDir, keyword);
		List<Category> listCategories = page.getContent();
		
		long startCount = (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", "name");
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);		
		
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("reverseSortDir", reverseSortDir);
		
		return "categories/categories";		
	}
	
	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		
		model.addAttribute("category", new Category());
		model.addAttribute("pageTitle", "Create New Category");
		
		return "categories/category_form";
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(Category category, 
			@RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);

			Category savedCategory = service.save(category);
			String uploadDir = "../category-images/" + savedCategory.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			service.save(category);
		}
		
		ra.addFlashAttribute("message", "The category has been saved successfully.");
		return defaultRedirectURL;
	}
	
	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Category category = service.get(id);
			
			model.addAttribute("category", category);
			model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");
			
			return "categories/category_form";			
		} catch (CategoryNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}
	}
		
	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			String categoryDir = "../category-images/" + id;
			FileUploadUtil.removeDir(categoryDir);
			
			redirectAttributes.addFlashAttribute("message", 
					"The category ID " + id + " has been deleted successfully");
		} catch (CategoryNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		
		return "redirect:/categories";
	}	
}
