package com.example.demo;

import java.security.Principal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.MemberDetails;
import com.example.demo.MemberRepository;
import com.example.demo.models.Category;
import com.example.demo.models.FlowerItem;
import com.example.demo.models.OrderItem;
import com.example.demo.CartItemRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.OrderItemRepository;

@Controller
public class CartItemController {
	@Autowired
	private CartItemRepository cartItemRepo;

	@Autowired
	private ItemRepository itemRepo;

	@Autowired
	private MemberRepository memberRepo;
	
	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private OrderItemRepository orderRepo;

	@Autowired
	private JavaMailSender javaMailSender;

	@GetMapping("/cart")
	public String showCart(Model model, Principal principal) {

		// Get currently logged in user
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();

		// Get shopping cart items added by this user
		// *Hint: You will need to use the method we added in the CartItemRepository
		List<CartItem> cartItemList = cartItemRepo.findAllByMemberId(loggedInMemberId);

		// Add the shopping cart items to the model

		double total = 0;
		for (CartItem cartItem : cartItemList) {
			cartItem.setSubtotal(cartItem.getQuantity() * cartItem.getItem().getPrice());
			total += cartItem.getSubtotal();
		}
		model.addAttribute("cartItemList", cartItemList);

		// Calculate the total cost of all items in the shopping cart

		// Add the shopping cart total to the model

		model.addAttribute("total", total);
		return "cart";
	}

	@PostMapping("/cart/add/{itemId}")
	public String addToCart(@PathVariable("itemId") int itemId, @RequestParam("quantity") int quantity,
			Principal principal) {

		// Get currently logged in user
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();

		// Check in the cartItemRepo if item was previously added by user.
		// *Hint: we will need to write a new method in the CartItemRepository
		boolean flag = false;
		CartItem cItem = new CartItem();
		List<CartItem> cartItemList = cartItemRepo.findAllByMemberId(loggedInMemberId);
		for (CartItem cartItem : cartItemList) {
			if (cartItem.getItem().getId() == itemId) {
				cItem = cartItem;
				flag = true;
				break;
			}

		}
		if (flag) {
			// if the item was previously added, then we get the quantity that was
			// previously added and increase that

			cItem.setQuantity(cItem.getQuantity() + quantity);

			// Save the CartItem object back to the repository
			cartItemRepo.save(cItem);
		}

		// if the item was NOT previously added,
		// then prepare the item and member objects
		else {

			FlowerItem item = itemRepo.getReferenceById(itemId);
			Member member = memberRepo.getReferenceById(loggedInMemberId);
			// Create a new CartItem object
			CartItem cartItem = new CartItem();

			// Set the item and member as well as the new quantity in the new CartItem
			// object
			cartItem.setItem(item);
			cartItem.setMember(member);
			cartItem.setQuantity(quantity);

			// Save the new CartItem object to the repository
			cartItemRepo.save(cartItem);

		}

		return "redirect:/cart";
	}

	@PostMapping("/cart/update/{id}")
	public String updateCart(@PathVariable("id") int cartItemId, @RequestParam("qty") int qty) {

		// Get cartItem object by cartItem's id
		CartItem cartItem = cartItemRepo.getReferenceById(cartItemId);

		// Set the quantity of the carItem object retrieved
		if (qty > 0) {
			cartItem.setQuantity(qty);
			// Save the cartItem back to the cartItemRepo
			cartItemRepo.save(cartItem);
		} else {
			cartItemRepo.deleteById(cartItemId);
		}

		return "redirect:/cart";
	}

	@GetMapping("/cart/remove/{id}")
	public String removeFromCart(@PathVariable("id") int cartItemId) {

		// Remove item from cartItemRepo
		cartItemRepo.deleteById(cartItemId);

		return "redirect:/cart";
	}

	// viewallcart
	@GetMapping("/view/cart")
	public String viewCart(Model model) {
		List<OrderItem> orderItemList = orderRepo.findAll();
		model.addAttribute("orderItemList", orderItemList);
		return "viewallcart";

	}

	// for paypal

	/*
	 * public void sendEmail(String to, String subject, String body) {
	 * SimpleMailMessage msg = new SimpleMailMessage(); msg.setTo(to);
	 * msg.setSubject(subject); msg.setText(body);
	 * 
	 * System.out.println("Sending"); javaMailSender.send(msg);
	 * System.out.println("Sent");
	 * 
	 * }
	 */

	@PostMapping("/cart/process_order")
	public String processOrder(Model model, @RequestParam("cartTotal") double cartTotal,
			@RequestParam("orderId") String orderId, @RequestParam("transactionId") String transactionId,
			Principal principal) {

		//drop down
		  List<Category> listCategories=categoryRepo.findAll();
		  model.addAttribute("listCategories", listCategories);

		/*
		 * System.out.println(orderId); System.out.println(transactionId);
		 */
		// Get currently logged in user
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();
		// Retrieve cart items purchased
		List<CartItem> cartItemList = cartItemRepo.findAllByMemberId(loggedInMemberId);
		// Get member object
		Member member = memberRepo.getReferenceById(loggedInMemberId);
		// Loop to iterate through all cart items
		for (int i = 0; i < cartItemList.size(); i++) {
			// for (CartItem cartItem : cartItemList) {
			// Retrieve details about current cart item
			CartItem cartItem = cartItemList.get(i);
			double subtotal = 0;
			double total = 0;
			int qty = cartItem.getQuantity();
			double price = cartItem.getItem().getPrice();
			subtotal = qty * price;
			total += subtotal;
			cartItem.setSubtotal(subtotal);
			// Update item table
			FlowerItem item = itemRepo.getReferenceById(cartItem.getItem().getId());
			item.setQuantity(item.getQuantity()-qty);
			itemRepo.save(item);

			// Add item to order table
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderId(orderId);
			orderItem.setQuantity(qty);
			orderItem.setSubtotal(subtotal);
			orderItem.setTransactionId(transactionId);
			orderItem.setItem(item);
			orderItem.setMember(member);
			orderRepo.save(orderItem);
		}
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("transactionId", transactionId);
		// clear cart items belonging to member
		cartItemRepo.deleteAll(cartItemList);
		// cartItemList.clear();
		// model.addAttribute("cartItemList", cartItemList);
		// Pass info to view to display success page
		model.addAttribute("success", "THANK YOU FOR YOUR ORDER!");
		// Send email
		String subject = "Order is confirmed!";
		String body = "Thank you for your order!\n" + "Order ID: " + orderId + "\n" + "Transaction ID: "
				+ transactionId;
		String to = member.getEmail();
		sendEmail(to, subject, body);
		return "success";
	}

	public void sendEmail(String to, String subject, String body) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(body);

		System.out.println("Sending");
		javaMailSender.send(msg);
		System.out.println("Sent");

	}
	/*
	 * @PostMapping("/cart/process_order") public String processOrder(Model
	 * model,@RequestParam("cartTotal")double cartTotal,@RequestParam("memberId")int
	 * memberId,@RequestParam("orderId")String
	 * orderId,@RequestParam("transactionId")String transactionId,Principal
	 * principal) {
	 * 
	 * System.out.println(orderId); System.out.println(transactionId);
	 * 
	 * //Get currently logged in user MemberDetails
	 * loggedInMember=(MemberDetails)SecurityContextHolder.getContext().
	 * getAuthentication().getPrincipal(); int
	 * loggedInMemberId=loggedInMember.getMember().getId();
	 * 
	 * //Retrieve cart items purchased List<CartItem>
	 * cartItemList=cartItemRepo.findAllByMemberId(loggedInMemberId);
	 * 
	 * //Get member object Member
	 * member=memberRepo.getReferenceById(loggedInMemberId);
	 * 
	 * //Loop to iterate through all cart items for(int
	 * i=0;i<cartItemList.size();i++) {
	 * 
	 * //for (CartItem cartItem:cartItemList){
	 * 
	 * 
	 * 
	 * //Retrive details about current cart item CartItem
	 * cartItem=cartItemList.get(i); double subtotal=0; double total=0; int
	 * qty=cartItem.getQuantity(); String color=cartItem.getColor(); double
	 * price=cartItem.getItem().getPrice(); subtotal=qty*price; total+=subtotal;
	 * cartItem.setSubtotal(subtotal);
	 * 
	 * //Update item table FlowerItem
	 * item=itemRepo.getReferenceById(cartItem.getItem().getId());
	 * item.setQuantity(item.getQuantity()-qty); itemRepo.save(item);
	 * 
	 * 
	 * //Add item to order table OrderItem orderItem=new OrderItem();
	 * orderItem.setOrderId(orderId); orderItem.setQuantity(qty);
	 * orderItem.setSubtotal(subtotal); orderItem.setTransactionId(transactionId);
	 * orderItem.setItem(item); orderItem.setColor(color);
	 * orderItem.setMember(member); orderRepo.save(orderItem);
	 * 
	 * } model.addAttribute("cartItemList", cartItemList);
	 * model.addAttribute("orderId", orderId); model.addAttribute("transactionId",
	 * transactionId);
	 * 
	 * //clear cart items belonging to member cartItemRepo.deleteAll(cartItemList);
	 * // (or) cartItemList.clear();
	 * 
	 * 
	 * //pass info to view to display success page model.addAttribute("success",
	 * "Thank You for your order!") ;
	 * 
	 * 
	 * //send email String subject="Order is confirmed!"; String
	 * body="Thank you for your order!\n"+"Order ID:" + orderId
	 * +"\n"+"Transaction ID:"+transactionId;
	 * 
	 * String to=member.getEmail(); sendEmail(to,subject,body); return "success";
	 * 
	 * }
	 */

	// for paypal

}