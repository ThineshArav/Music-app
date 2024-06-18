package com.example.demo.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entities.Users;
import com.example.demo.services.UsersService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {
	
	@Autowired
	UsersService Service;
	
	
	@PostMapping("/createOrder")
	@ResponseBody
		public String createorder() {
		Order order=null;
		try {
			RazorpayClient razorpay = new RazorpayClient("rzp_test_rcAEjMhHFWQKFo", "gW1BEaRHTc3OfHGX7ygQ4rxn");

			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount",50000);
			orderRequest.put("currency","INR");
			orderRequest.put("receipt", "receipt#1");
			JSONObject notes = new JSONObject();
			notes.put("notes_key_1","Tea, Earl Grey, Hot");
			orderRequest.put("notes",notes);

			 order = razorpay.orders.create(orderRequest);
		}
		catch (Exception e) {
			System.out.println("exception while creating order");
		}
		return order.toString();
		}
	
	@PostMapping("/verify")
	@ResponseBody
	public boolean verifyPayment(@RequestParam  String orderId, @RequestParam String paymentId, @RequestParam String signature) {
	    try {
	        // Initialize Razorpay client with your API key and secret
	        RazorpayClient razorpayClient = new RazorpayClient("rzp_test_rcAEjMhHFWQKFo", "gW1BEaRHTc3OfHGX7ygQ4rxn");
	        // Create a signature verification data string
	        String verificationData = orderId + "|" + paymentId;

	        // Use Razorpay's utility function to verify the signature
	        boolean isValidSignature = Utils.verifySignature(verificationData, signature, "gW1BEaRHTc3OfHGX7ygQ4rxn");

	        return isValidSignature;
	    } catch (RazorpayException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	//Payment success- update premium user
	@GetMapping("Payment-success")
	public String paymentSucces(HttpSession session) {
		String email=(String) session.getAttribute("email");
		Users user=Service.getUser(email);
		user.setPremium(true);
		Service.updateUser(user);
		return "login";
		
	}
	
	//Payment failure -redirect to login 
	@GetMapping("Paymet-failure")
	public String paymentFailure() {
		//Payment error page
		return "login";
	}
}
