package com.example.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class FlowerItem {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

@NotNull
@NotEmpty(message="Name cannot be empty!")
@Size(min=3,max=50,message="Category length must be between 3 and 50 characters!")
private String name;

@NotNull
@NotEmpty(message="Color cannot be empty!")
@Size(min=3,max=10,message="Category length must be between 3 and 10 characters!")
private String color;

@DecimalMin(value="0.1",message="price should be positive numerial value!")
private double price;

@Min(value=1,message="quantity should be positive whole number!")
private int quantity;


@ManyToOne
@JoinColumn(name="category_id",nullable = false) 

@NotNull(message="category not empty!")
private Category category;

@NotNull(message="Image not empty!")
private String imgName;


public String getImgName() {
	return imgName;
}
public void setImgName(String imgName) {
	this.imgName = imgName;
}
public Category getCategory() {
	return category; }
public void
setCategory(Category category) { 
	this.category = category; }




public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getColor() {
	return color;
}
public void setColor(String color) {
	this.color = color;
}
public double getPrice() {
	return price;
}
public void setPrice(double price) {
	this.price = price;
}
public int getQuantity() {
	return quantity;
}
public void setQuantity(int quantity) {
	this.quantity = quantity;
}
public FlowerItem(int id, String name, String color, double price, int quantity) {
	super();
	this.id = id;
	this.name = name;
	this.color = color;
	this.price = price;
	this.quantity = quantity;
}

public FlowerItem() {
	// TODO Auto-generated constructor stub
}


}