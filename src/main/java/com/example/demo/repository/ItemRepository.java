package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Category;
import com.example.demo.models.FlowerItem;

public interface ItemRepository extends JpaRepository<FlowerItem, Integer>{

	public List<FlowerItem> findAllByCategory(Category category);

}
