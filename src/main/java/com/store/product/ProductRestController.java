package com.store.product;

import java.text.DecimalFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.store.entity.Product;

@RestController
public class ProductRestController {
	
	
	@Autowired private ProductRepository productRepo;	
		
	@GetMapping("/calculateCost/plant/{plantId}/quantity/{quantity}/soil/{soilId}/pot/{potId}")
	public Float calculatePlantCost(@PathVariable(name = "plantId") Integer plantId, @PathVariable(name = "quantity") Integer quantity,
			@PathVariable(name = "soilId") Integer soilId, @PathVariable(name = "potId") Integer potId) {
		Product plant = productRepo.findById(plantId).get();
		Product pot = productRepo.findById(potId).get();
		Product soil = productRepo.findById(soilId).get();
		
		Float cost = plant.getUnitPrice() * quantity + pot.getUnitPrice() + pot.getVolume() * soil.getUnitPrice();
		
		DecimalFormat df = new DecimalFormat("#.##");
		
		return Float.valueOf(df.format(cost));
	}
}
