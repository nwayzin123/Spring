package com.example.demo.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.models.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.ui.Model;
@Controller
public class HomeController {
	
@Autowired
public CategoryRepository categoryRepo;
@GetMapping("/")
	public String Index(Model model) {
	
	//drop down
	  List<Category> listCategories=categoryRepo.findAll();
	  model.addAttribute("listCategories", listCategories);
		return "index";
		
}

@GetMapping("/403")
public String error403() {
	return "403";
	
}


}
