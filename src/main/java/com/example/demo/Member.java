package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Member {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)

private int id;

private String name;

private String username;

private String password;

private String email;

private String role;

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
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}
public Member(int id, String name, String username, String password, String email, String role) {
	super();
	this.id = id;
	this.name = name;
	this.username = username;
	this.password = password;
	this.email = email;
	this.role = role;
}
public Member() {
	super();
	// TODO Auto-generated constructor stub
}
	
	
}
