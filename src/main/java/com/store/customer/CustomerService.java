package com.store.customer;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.store.entity.Customer;

@Service
public class CustomerService {
	public static final int CUSTOMERS_PER_PAGE = 6;
	
	@Autowired private CustomerRepository repo;
	
	public List<Customer> listAll(){
		return (List<Customer>) repo.findAll();
	}
	
	public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum-1, CUSTOMERS_PER_PAGE, sort);
		
		if (keyword != null) {
			return repo.findAll(keyword, pageable);
		}
		
		return repo.findAll(pageable);
	}

	public void save(Customer customer) {
		repo.save(customer);
		
	}

	public Customer get(Integer id) throws CustomerNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CustomerNotFoundException("Could not find any customer with ID " + id);
		}
	}
}
