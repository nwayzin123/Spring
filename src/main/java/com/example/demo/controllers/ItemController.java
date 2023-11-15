package com.example.demo.controllers;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.example.demo.models.Category;
import com.example.demo.models.FlowerItem;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ItemRepository;

import jakarta.validation.Valid;






@Controller
public class ItemController {
@Autowired
public ItemRepository itemRepo;	

@Autowired
public CategoryRepository categoryRepo;

@GetMapping("/view/user")
public String viewUser(Model model) {
	List<FlowerItem> itemList=itemRepo.findAll();
	model.addAttribute("itemList", itemList);
	
	
	//drop down
	  List<Category> listCategories=categoryRepo.findAll();
	  model.addAttribute("listCategories", listCategories);
	  return "view_user";
	
	
}


  @GetMapping("/viewuser{id}") 
  public String viewUserByCategory(Model model,@PathVariable("id")Integer id) {
  Category category=categoryRepo.getReferenceById(id); 
  List<FlowerItem> itemList=itemRepo.findAllByCategory(category); 
  model.addAttribute("itemList",itemList); 
  
//drop down
  List<Category> listCategories=categoryRepo.findAll();
  model.addAttribute("listCategories", listCategories);
  return "category_view_user";
  
  }
 
 

//view_flower_item.html
@GetMapping("/items")
public String viewFlowerItem(Model model) {
	List<FlowerItem> listItem=itemRepo.findAll();
	model.addAttribute("listItem", listItem);
	
	//drop down
	  List<Category> listCategories=categoryRepo.findAll();
	  model.addAttribute("listCategories", listCategories);
	return "view_flower_item";
}

//add_flower_item.html	
@GetMapping("/item/add")
public String addItem(Model model) {
	FlowerItem item=new FlowerItem();
	model.addAttribute("item", item);
	
	//get data from category
		List<Category> listCategories=categoryRepo.findAll();
		model.addAttribute("listCategories", listCategories);
		return "add_flower_item";
	
}

/*
 * @PostMapping("/item/save") public String saveItem(FlowerItem item) {
 * itemRepo.save(item); return "redirect:/items";
 * 
 * }
 */

//upload image
@PostMapping("/item/save")
public String saveItem(FlowerItem item,@Valid BindingResult bindingResult,@RequestParam("itemImage") MultipartFile imgFile) {
	if(bindingResult.hasErrors()) {
		return "add_flower_item";
	}

	
String imageName = imgFile.getOriginalFilename();
//set the image name in item object
item.setImgName(imageName);
//save the item obj to the db
FlowerItem savedItem = itemRepo.save(item);
try {
	// prepare the directory path
	String uploadDir = "uploads/items/" + savedItem.getId();
	Path uploadPath = Paths.get(uploadDir);
	// check if the upload path exists, if not it will be created
	if (!Files.exists(uploadPath)) {
		Files.createDirectories(uploadPath);
	}
	// prepare path for file
	Path fileToCreatePath = uploadPath.resolve(imageName);
	System.out.println("File path: " + fileToCreatePath);
	// copy file to the upload location
	Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return "redirect:/items";
	}

//view_single.html
@GetMapping("/item/{id}")
public String viewSingleItem(@PathVariable("id")Integer id,Model model) {
	FlowerItem item=itemRepo.getReferenceById(id);
	model.addAttribute("item", item);
	return "view_single_item";
	
}

//edit_item.html
@GetMapping("/item/edit/{id}")
public String editCategory(@PathVariable("id")Integer id,Model model) {
	FlowerItem item=itemRepo.getReferenceById(id);
	model.addAttribute("item", item);
	
	//get data from category
	List<Category> listCategories=categoryRepo.findAll();
	model.addAttribute("listCategories", listCategories);
	return "edit_flower_item";
	
}

//edit_item.save
@PostMapping("/item/edit/{id}/{img}")
public String saveUpdatedItem(FlowerItem item, @PathVariable("id") Integer id, @PathVariable("img") String imgName,
		@RequestParam("imageName") MultipartFile imgFile) throws IOException {

	System.out.println(imgName);
	item.setImgName(imgName);

	if (!imgFile.isEmpty()) {
		imgName = imgFile.getOriginalFilename();

		// set the image name in item object
		item.setImgName(imgName);

		// prepare the directory path
		String uploadDir = "uploads/items/" + item.getId();
		Path uploadPath = Paths.get(uploadDir);

//		Path imagePathToDelete = uploadPath.resolve(imgName);

		File fileToDelete = new File(uploadDir, imgName);
		fileToDelete.delete();

		// Delete the image file
//		Files.delete(imagePathToDelete);
	}
	// save the item obj to the db
	FlowerItem savedItem = itemRepo.save(item);

	try {
		// prepare the directory path
		String uploadDir = "uploads/items/" + savedItem.getId();
		Path uploadPath = Paths.get(uploadDir);

		// check if the upload path exists, if not it will be created
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		// prepare path for file
		Path fileToCreatePath = uploadPath.resolve(imgName);
		System.out.println("File path: " + fileToCreatePath);

		// copy file to the upload location
		Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	itemRepo.save(item);

	return "redirect:/items";


}
//delete_item.html
@GetMapping("/item/delete/{id}")
public String deleteItem(@PathVariable("id")Integer id) {
	itemRepo.deleteById(id);
	return "redirect:/items";

}

//view detail
/*
 * @GetMapping("/view/details/{id}") public String
 * viewDetail(@PathVariable("id")Integer id,Model model) {
 * 
 * FlowerItem item=itemRepo.getReferenceById(id); model.addAttribute("item",
 * item); return "view_detail";
 * 
 * }
 */
@GetMapping("/view/details/{id}")
public String viewSingleItem1(@PathVariable("id")Integer id,Model model) {
	FlowerItem item=itemRepo.getReferenceById(id);
	model.addAttribute("item", item);
	return "view_detail";
	
}

@GetMapping("/about")
public String about() {
 return "about";
	
}

}

