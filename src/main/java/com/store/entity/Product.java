package com.store.entity;

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
@Table(name = "product")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 64, nullable = false, unique = true)
	private String name;
	
	@Column(length = 255)
	private String description;
	
	@Column(nullable = false)
	private String image;	
	
	private int quantity;
	
	private float price;
	
	private float volume;
	
	private float unitPrice;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	private float height_inch;
	private float top_width_inch;
	private float bottom_width_inch;
	private float top_length_inch;
	private float bottom_length_inch;	

	public Product() {
	}
	
	public Product(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public float getHeight_inch() {
		return height_inch;
	}

	public void setHeight_inch(float height_inch) {
		this.height_inch = height_inch;
	}

	public float getTop_width_inch() {
		return top_width_inch;
	}

	public void setTop_width_inch(float top_width_inch) {
		this.top_width_inch = top_width_inch;
	}

	public float getBottom_width_inch() {
		return bottom_width_inch;
	}

	public void setBottom_width_inch(float bottom_width_inch) {
		this.bottom_width_inch = bottom_width_inch;
	}

	public float getTop_length_inch() {
		return top_length_inch;
	}

	public void setTop_length_inch(float top_length_inch) {
		this.top_length_inch = top_length_inch;
	}

	public float getBottom_length_inch() {
		return bottom_length_inch;
	}

	public void setBottom_length_inch(float bottom_length_inch) {
		this.bottom_length_inch = bottom_length_inch;
	}
	
	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description=" + description + "]";
	}

	@Transient
	public String getImagePath() {
		if (this.id == null) return "/images/image-thumbnail.png";
		
		return "/products-images/" + this.id + "/" + this.image;
	}	

}
