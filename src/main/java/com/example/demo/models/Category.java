package com.example.demo.models;


import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;
	
	@NotNull
	@NotEmpty(message="Category name cannot be empty!")
private String name;

@OneToMany(mappedBy = "category" ,cascade=CascadeType.ALL)
private Set<FlowerItem> items;



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
public Category(int id, String name) {
	super();
	this.id = id;
	this.name = name;
}
public Category() {
	// TODO Auto-generated constructor stub
}
}
