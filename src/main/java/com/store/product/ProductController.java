package com.store.product;

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
import com.store.category.CategoryService;
import com.store.entity.Category;
import com.store.entity.Product;

@Controller
public class ProductController {
	private String defaultRedirectURL = "redirect:/products/page/1?sortField=id&sortDir=asc";
	
	@Autowired private ProductService productService;
	@Autowired private CategoryService categoryService;
	
	@GetMapping("/products")
	public String listFirstPage() {			
		return defaultRedirectURL;
	}
	
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {	
		Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Product> listProducts = page.getContent();
		
		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE +1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "products/products";
	}
	
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Category> listCategories = categoryService.listAll();
		
		Product product = new Product();
		model.addAttribute("product", product);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Create New Product");
		
		return "products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct(Product product, 
			@RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException {
		float unit_price = 0;
		
		if (product.getCategory().getId() == 2) {
			unit_price = product.getPrice() / product.getVolume();
		} else if (product.getCategory().getId() == 3 || product.getCategory().getId() == 4) {			
			unit_price = (float) Math.ceil(product.getPrice() / product.getQuantity() * 100) /100;
		} else {
			unit_price = product.getPrice();			
		}
		
		product.setUnitPrice(unit_price);
		setVolumeOfPot(product);	
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			product.setImage(fileName);

			Product savedProduct = productService.save(product);
			String uploadDir = "../products-images/" + savedProduct.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			productService.save(product);
		}
		
		ra.addFlashAttribute("message", "The product has been saved successfully.");
		
		
		return defaultRedirectURL;
	}

	private void setVolumeOfPot(Product product) {
		
		if (product.getCategory().getId() == 3) {
			float volume = 0.0f;
			float radius1 = product.getBottom_width_inch() / 2;
			float radius2 = product.getTop_width_inch() / 2;
			float height = product.getHeight_inch();
					
			volume = (float) (height * Math.PI * (radius1 * radius1 + radius2 * radius2 + radius1 * radius2) / 3);
			
			product.setVolume(volume);
		}
		
		if (product.getCategory().getId() == 4) {
			float volume = 0.0f;
			float topLenght = product.getTop_length_inch();
			float bottomLenght = product.getBottom_length_inch();
			float topWidth = product.getTop_width_inch();
			float bottomWidth = product.getBottom_width_inch();
			
			float height = product.getHeight_inch();
			
			volume = (float) (height * (topLenght * topWidth + bottomLenght * bottomWidth +	Math.sqrt(bottomWidth * bottomLenght * topWidth * topLenght))/ 3);
			
			product.setVolume(volume);
		}
		
	}

	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable(name = "id") Integer id, Model model, 
			RedirectAttributes ra) {		
		try {
			List<Category> listCategories = categoryService.listAll();
			
			Product product = productService.get(id);
			
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
			
			return "products/product_form";			
		} catch (ProductNotFoundException e) {			
			ra.addFlashAttribute("message", e.getMessage());
			return defaultRedirectURL;
		}		
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable("id") Integer id, Model model, 
			RedirectAttributes ra) {
		try {
			productService.delete(id);		
			
			ra.addFlashAttribute("message", 
					"The product with ID " + id + " has been deleted successfully");			
		}catch (ProductNotFoundException ex) {			
			ra.addFlashAttribute("message", ex.getMessage());				
		}
		
		return defaultRedirectURL;		
	}	
}
