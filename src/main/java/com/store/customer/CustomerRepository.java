package com.store.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.store.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	@Query("Select c from Customer c where concat(c.name, ' ', c.id) like %?1% or c.address like %?1%")
	public Page<Customer> findAll(String keyword, Pageable pageable);

	public Long countById(Integer id);

}
