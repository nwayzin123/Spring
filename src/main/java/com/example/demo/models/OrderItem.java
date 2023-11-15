package com.example.demo.models;

import com.example.demo.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

@Entity
public class OrderItem {
	
	
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

private String orderId;
private String transactionId;

@ManyToOne
@JoinColumn(name="member_id",nullable=false)
private Member member;

@ManyToOne
@JoinColumn(name="item_id",nullable=false)
private FlowerItem item;



private int quantity;
private String color;

@Transient
private double subtotal;



public String getColor() {
	return color;
}
public void setColor(String color) {
	this.color = color;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getOrderId() {
	return orderId;
}
public void setOrderId(String orderId) {
	this.orderId = orderId;
}
public String getTransactionId() {
	return transactionId;
}
public void setTransactionId(String transactionId) {
	this.transactionId = transactionId;
}
public Member getMember() {
	return member;
}
public void setMember(Member member) {
	this.member = member;
}
public FlowerItem getItem() {
	return item;
}
public void setItem(FlowerItem item) {
	this.item = item;
}
public int getQuantity() {
	return quantity;
}
public void setQuantity(int quantity) {
	this.quantity = quantity;
}
public double getSubtotal() {
	return subtotal;
}
public void setSubtotal(double subtotal) {
	this.subtotal = subtotal;
}
/*
 * public OrderItem(int id, String orderId, String transactionId, Member member,
 * FlowerItem item, int quantity, double subtotal) { super(); this.id = id;
 * this.orderId = orderId; this.transactionId = transactionId; this.member =
 * member; this.item = item; this.quantity = quantity; this.subtotal = subtotal;
 * } public OrderItem() {
 * 
 * // TODO Auto-generated constructor stub }
 */

}
