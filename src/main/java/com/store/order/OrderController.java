package com.store.order;

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
import com.store.customer.CustomerService;
import com.store.entity.Customer;
import com.store.entity.Order;
import com.store.entity.Product;
import com.store.product.ProductService;

@Controller
public class OrderController {
	private String defaultRedirectURL = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";
	
	@Autowired private OrderService orderService;
	@Autowired private CustomerService customerService;
	@Autowired private ProductService productService;
	
	@GetMapping("/orders")
	public String listFirstPage(Model model) {			
		return defaultRedirectURL;
	}
	
	@GetMapping("/orders/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {	
		Page<Order> page = orderService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Order> listOrders = page.getContent();
		
		long startCount = (pageNum - 1) * OrderService.ORDERS_PER_PAGE +1;
		long endCount = startCount + OrderService.ORDERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("listOrders", listOrders);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "orders/orders";
	}
	
	@GetMapping("/orders/new")
	public String newOrder(Model model) {
		List<Customer> listCustomers = customerService.listAll();
		
		List<Product> listPlants = productService.listAllPlants();
		List<Product> listPots = productService.listAllPots();
		List<Product> listSoil = productService.listAllSoil();
		
		Order order = new Order();
		
		model.addAttribute("listCustomers", listCustomers);
		
		model.addAttribute("listPlants", listPlants);
		model.addAttribute("listPots", listPots);
		model.addAttribute("listSoil", listSoil);
		model.addAttribute("order", order);
		model.addAttribute("pageTitle", "Create New Order");
		
		return "orders/order_form";
	}
	
	@GetMapping("/orders/edit/{id}")
	public String editOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {		 
		try {
			Order order = orderService.get(id);
			
			List<Customer> listCustomers = customerService.listAll();
			List<Product> listPlants = productService.listAllPlants();
			List<Product> listPots = productService.listAllPots();
			List<Product> listSoil = productService.listAllSoil();
						
			model.addAttribute("listCustomers", listCustomers);
			model.addAttribute("listPlants", listPlants);
			model.addAttribute("listPots", listPots);
			model.addAttribute("listSoil", listSoil);
			model.addAttribute("order", order);
			model.addAttribute("pageTitle", "Edit Order (ID: " + id + ")");
			
			return "orders/order_form";
			
		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());			
			return defaultRedirectURL;
		}
	}
	
	@PostMapping("/orders/save")
	public String saveOrder(Order order, 
			@RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException {
		
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			order.setImage(fileName);

			Order savedOrder = orderService.save(order);
			String uploadDir = "../orders-images/" + savedOrder.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			orderService.save(order);
		}		
		
		if (orderService.isInEditMode) {
			ra.addFlashAttribute("message", "The order ID " + order.getId() + " has been updated successfully.");	
		} else {
			ra.addFlashAttribute("message", "The order ID " + order.getId() + " has been saved successfully.");
		}
		
		return defaultRedirectURL;
	}
	
	@GetMapping("/orders/detail/{id}")
	public String viewOrderDetails(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {		 
		try {
			Order order = orderService.get(id);	
						
			model.addAttribute("order", order);			
			
			return "orders/order_details_modal";			
		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			
			return defaultRedirectURL;
		}
	}
}
