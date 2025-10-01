package com.example.demo.controller;

import com.example.demo.service.BookService;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private CartService cartService;
    
    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpSession session) {
        try {
            // Obtener categorías para el dropdown del navbar
            List<String> categories = bookService.getAllCategories();
            model.addAttribute("categories", categories);
            
            // Obtener cantidad de items en el carrito
            int cartItemCount = cartService.getCartItemCount(session.getId());
            model.addAttribute("cartItemCount", cartItemCount);
        } catch (Exception e) {
            // En caso de error, usar valores por defecto
            model.addAttribute("categories", List.of());
            model.addAttribute("cartItemCount", 0);
        }
    }
}