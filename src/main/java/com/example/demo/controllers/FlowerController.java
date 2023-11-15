package com.example.demo.controllers;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.models.Category;
import com.example.demo.models.FlowerItem;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ItemRepository;

import jakarta.validation.Valid;


@Controller
public class FlowerController {
@Autowired
private CategoryRepository categoryRepo;

/*
 * @Autowired private ItemRepository itemRepo;
 */

//view_flowercategory.html
@GetMapping("/categories")
public String viewFlowerCategory(Model model) {
	List<Category> listcategory=categoryRepo.findAll();
	model.addAttribute("listcategory", listcategory);
	
	//drop down
	  List<Category> listCategories=categoryRepo.findAll();
	  model.addAttribute("listCategories", listCategories);
	 
	
	return "view_flower_category";
	
}
	
//add_flowercategory.html
@GetMapping("/flowercategory/add")
public String addCategory(Model model) {
	Category category=new Category();
	model.addAttribute("category", category);
	return "add_flower_category";
	
}
@PostMapping("/category/save")
public String saveCategory(@Valid Category category,BindingResult bindingResult) {
	if(bindingResult.hasErrors()) {
		return "add_flower_category";
	}
categoryRepo.save(category);
return "redirect:/categories";

}
//edit_category.html
@GetMapping("/category/edit/{id}")
public String editCategory(@PathVariable("id")Integer id,Model model) {
	Category category=categoryRepo.getReferenceById(id);
	model.addAttribute("category", category);
	return "edit_category";
	
}
@PostMapping("category/edit/{id}")
public String saveUpdateCategory(@PathVariable("id") Integer id,Category category) {
	categoryRepo.save(category);
	return "redirect:/categories";
	
}
//delete_category.html
@GetMapping("/category/delete/{id}")
public String deleteCategory(@PathVariable("id")Integer id) {
	categoryRepo.deleteById(id); 
	/*
	 * Category category=categoryRepo.getReferenceById(id); List<FlowerItem>
	 * itemList=itemRepo.findAllByCategory(category); itemRepo.deleteAll(itemList);
	 * categoryRepo.delete(category);
	 */
	
	
	
	return "redirect:/categories";

}


 
 
 
}
