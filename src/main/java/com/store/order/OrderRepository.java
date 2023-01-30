package com.store.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.store.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	@Query("Select o from Order o where o.customer.name like %?1% or o.product.name like %?1% or o.description like %?1%")
	public Page<Order> findAll(String keyword, Pageable pageable);

}
