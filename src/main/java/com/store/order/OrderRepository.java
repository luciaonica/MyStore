package com.store.order;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.store.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	@Query("SELECT o FROM Order o WHERE o.customer.name LIKE %?1% OR o.product.name LIKE %?1% OR o.description LIKE %?1%")
	public Page<Order> findAll(String keyword, Pageable pageable);

	public Long countById(Integer id);

	@Query("SELECT o FROM Order o WHERE o.customer.id = ?1 ")
	public List<Order> findByCustomer(Integer customerId);
	
	@Query("SELECT o FROM Order o WHERE o.product.id = ?1 OR o.potType.id = ?1 OR o.soilType.id = ?1")
	public List<Order> findByProduct(Integer productId);
	
	@Query("SELECT NEW com.store.entity.Order(o.product.name, o.quantity, o.orderTime, o.cost, o.total) FROM Order o "
			+ "WHERE o.orderTime between ?1 AND ?2 ORDER BY o.orderTime ASC")
	public List<Order> findByProductAndTimeBetween(Date startTime, Date endTime);

	@Query("SELECT NEW com.store.entity.Order(o.id, o.orderTime, o.cost, o.total) FROM Order o "
			+ "WHERE o.orderTime between ?1 AND ?2 ORDER BY o.orderTime ASC")
	public List<Order> findByOrderTimeBetween(Date startTime, Date endTime);
	
}
