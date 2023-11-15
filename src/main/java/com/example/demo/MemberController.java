package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.FlowerItem;

import jakarta.validation.Valid;

@Controller
public class MemberController {
	
@Autowired
private MemberRepository memberRepo;

//view_member
@GetMapping("/members")
public String viewMember(Model model) {
	List<Member> memberList=memberRepo.findAll();
	model.addAttribute("memberList", memberList);
	return "view_members";
	
}

//add a new member for admin
@GetMapping("/member/add")
public String addMember(Model model) {
Member member=new Member();
model.addAttribute("member", member);
return "add_member";
	}


@PostMapping("/member/save")
public String saveMember(@Valid Member member,BindingResult bindingResult,RedirectAttributes redirectAttribute) {
if(bindingResult.hasErrors())	{
	return "add_member";
}
BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
String encodePassword=passwordEncoder.encode(member.getPassword());
member.setPassword(encodePassword);
member.setRole("ROLE_ADMIN");
memberRepo.save(member);
redirectAttribute.addFlashAttribute("success","Member registered!");

return "redirect:/members";
	
}


//add a new member for user
@GetMapping("/member/user/add")
public String addMemberUser(Model model) {
Member member=new Member();
model.addAttribute("member", member);
return "sign_up";
	}


@PostMapping("/member/user/save")
public String saveMemberUser(@Valid Member member,BindingResult bindingResult,RedirectAttributes redirectAttribute) {
if(bindingResult.hasErrors())	{
	return "sign_up";
}
BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
String encodePassword=passwordEncoder.encode(member.getPassword());
member.setPassword(encodePassword);
member.setRole("ROLE_USER");
memberRepo.save(member);
redirectAttribute.addFlashAttribute("success","User registered!");

return "login";
	
}


//edit_member
@GetMapping("/member/edit/{id}")
public String editMember(@PathVariable("id")Integer id,Model model)  {
	Member member=memberRepo.getReferenceById(id);
	model.addAttribute("member", member);
	return "edit_member";
	
}

//edit_member.save

@PostMapping("/member/edit/{id}")
public String saveUpdateMember(@Valid Member member,BindingResult bindingResult,RedirectAttributes redirectAttribute) {
if(bindingResult.hasErrors())	{
	return "add_member";
}
BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
String encodePassword=passwordEncoder.encode(member.getPassword());
member.setPassword(encodePassword);
member.setRole("ROLE_USER");
memberRepo.save(member);
redirectAttribute.addFlashAttribute("success","Member registered!");

return "redirect:/members";
	
}


//delete_member.html
@GetMapping("/member/delete/{id}")
public String deleteMember(@PathVariable("id")Integer id) {
	memberRepo.deleteById(id);
	return "redirect:/members";

}

}
