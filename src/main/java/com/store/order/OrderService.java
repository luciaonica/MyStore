package com.store.order;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.store.entity.Order;

@Service
public class OrderService {
	
	public static final int ORDERS_PER_PAGE = 10;
	public boolean isInEditMode = false;
	
	@Autowired private OrderRepository repo;
	
	public List<Order> listAll(){
		return (List<Order>) repo.findAll();
	}
	
	public Page<Order> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum-1, ORDERS_PER_PAGE, sort);
		
		if (keyword != null) {
			return repo.findAll(keyword, pageable);
		}
		
		return repo.findAll(pageable);
	}

	public Order save(Order orderInForm) {	
		
		if (orderInForm.getId() == null) {
			orderInForm.setOrderTime(new Date());	
			isInEditMode = false;
		} else {		
			Order orderInDB = repo.findById(orderInForm.getId()).get();
			orderInForm.setOrderTime(orderInDB.getOrderTime());
			isInEditMode = true;
		}
		
		return repo.save(orderInForm);
	}	
	
	public Order get(Integer id) throws OrderNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new OrderNotFoundException("Could not find any order with ID " + id);
		}
	}

	public void delete(Integer id) throws OrderNotFoundException{
		Long countById = repo.countById(id);
		
		if (countById == null || countById == 0) {
			throw new OrderNotFoundException("Could not found any order with ID " + id);
		}
		
		repo.deleteById(id);
		
	}

}
