package com.pdfchat.ai.pdfchatbackend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class FrontController {
	
	@RequestMapping("/docgpt")
	public String goHome(Model model, HttpSession session) {
		
		String message = (String) session.getAttribute("successMessage");
	    if (message != null) {
	        model.addAttribute("msg", message);
	        session.removeAttribute("successMessage"); // Remove after use
	    }
		
		System.out.println("request home");
		model.addAttribute("activeLink","docgpt");
		return "docgpt";
	}

	@RequestMapping("/")
	public String landing(Model model) {
		System.out.println("welcome page requested..");
		model.addAttribute("activeLink","home");
		return "index";
	}
	
	@RequestMapping("/features")
	public String gofeat(Model model) {
		System.out.println("requested tech..");
		model.addAttribute("activeLink","feat");
		return "tech";
	}
}
