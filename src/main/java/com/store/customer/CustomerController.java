package com.store.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.store.FileUploadUtil;
import com.store.Category.CategoryNotFoundException;
import com.store.entity.Customer;

@Controller
public class CustomerController {
	private String defaultRedirectURL = "redirect:/customers/page/1?sortField=name&sortDir=asc";
	
	@Autowired private CustomerService service;
	
	@GetMapping("/customers")
	public String listFirstPage(Model model) {			
		return defaultRedirectURL;
	}
	
	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {	
		Page<Customer> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<Customer> listCustomers = page.getContent();
		
		long startCount = (pageNum - 1) * CustomerService.CUSTOMERS_PER_PAGE +1;
		long endCount = startCount + CustomerService.CUSTOMERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("listCustomers", listCustomers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "customers/customers";
	}
	
	@GetMapping("/customers/new")
	public String newCustomer(Model model) {
		Customer customer = new Customer();
		model.addAttribute("customer", customer);
		model.addAttribute("pageTitle", "Create New Customer");
		
		return "customers/customer_form";
	}
	
	@PostMapping("/customers/save")
	public String saveCustomer(Customer customer, RedirectAttributes ra) {
		service.save(customer);
		ra.addFlashAttribute("message", "The customer has been saved successfully.");
		
		
		return defaultRedirectURL;
	}
	
	@GetMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable(name = "id") Integer id, Model model, 
			RedirectAttributes ra) {		
		try {
			Customer customer = service.get(id);
			model.addAttribute("customer", customer);
			model.addAttribute("pageTitle", "Edit Customer (ID: " + id + ")");
			
			return "customers/customer_form";			
		} catch (CustomerNotFoundException e) {			
			ra.addFlashAttribute("message", e.getMessage());
			return defaultRedirectURL;
		}		
	}
	
	@GetMapping("/customers/delete/{id}")
	public String deleteCustomer(@PathVariable(name = "id") Integer id, 
			Model model, RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
					
			redirectAttributes.addFlashAttribute("message", 
					"The customer ID " + id + " has been deleted successfully");
		} catch (CustomerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		
		return defaultRedirectURL;
	}	
}
