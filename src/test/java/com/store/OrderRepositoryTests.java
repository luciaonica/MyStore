package com.store;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.store.entity.Order;
import com.store.order.OrderRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderRepositoryTests {
	
	@Autowired	private OrderRepository repo;	
	
	@Test
	public void testFindByProductAndTimeBetween() throws ParseException {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = dateFormatter.parse("2023-01-27");
		Date endTime = dateFormatter.parse("2023-02-04");
		
		List<Order> listOrders = repo.findByProductAndTimeBetween(startTime, endTime);
		
		assertThat(listOrders.size()).isGreaterThan(0);
		
		for (Order order : listOrders) {
			System.out.printf("%-75s | %d | %10.2f | %10.2f  \n", 
					order.getProduct().getName(), order.getQuantity(), order.getCost(), order.getTotal());
		}
	}
	
	
		
}
