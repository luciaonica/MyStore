package com.store.entity;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Date orderTime;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	private int quantity;
	
	@Column(nullable = false)
	private String image;	

	private String description;

	@ManyToOne
	@JoinColumn(name = "pot_type_id")
	private Product potType;

	@ManyToOne
	@JoinColumn(name = "soil_type_id")
	private Product soilType;

	private float cost;
	private float price;
	private float total;	

	public Order() {
	}

	public Order(Integer id, Date orderTime, float cost, float total) {
		this.id = id;
		this.orderTime = orderTime;
		this.cost = cost;
		this.total = total;
	}
	
	public Order(String productName, int quantity, Date orderTime, float cost, float total) {
		this.product = new Product(productName);
		this.quantity = quantity;
		this.orderTime = orderTime;
		this.cost = cost;
		this.total = total;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;		
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public Product getPotType() {
		return potType;
	}

	public void setPotType(Product potType) {
		this.potType = potType;
	}

	public Product getSoilType() {
		return soilType;
	}

	public void setSoilType(Product soilType) {
		this.soilType = soilType;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderTime=" + orderTime + ", customer=" + customer.getName() + ", total=" + total
				+ "]";
	}
	
	@Transient
	public String getImagePath() {
		if (this.id == null) return "/images/image-thumbnail.png";
		
		return "/orders-images/" + this.id + "/" + this.image;
	}
}
