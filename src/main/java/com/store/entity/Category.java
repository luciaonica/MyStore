package com.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "category")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128, nullable = false, unique = true)
	private String name;
		
	@Column(length = 128, nullable = false)
	private String image;	
	
	public Category() {
	}
	
	public Category(String name) {
		this.name = name;
	}

	public Category(Integer id) {
		this.id = id;
	}	

	public Category(Integer id, String name) {
		this.id = id;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}	
	
	@Transient
	public String getImagePath() {
		if (this.id == null) return "/images/image-thumbnail.png";
		
		return "/category-images/" + this.id + "/" + this.image;
	}

	@Override
	public String toString() {
		return this.name;
	}	

}
