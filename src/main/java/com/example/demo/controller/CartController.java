package com.example.demo.controller;

import com.example.demo.model.CartItem;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        String sessionId = session.getId();
        List<CartItem> cartItems = cartService.getCartItems(sessionId);
        BigDecimal total = cartService.getCartTotal(sessionId);
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", total);
        model.addAttribute("cartItemCount", cartService.getCartItemCount(sessionId));
        
        return "cart";
    }
    
    @PostMapping("/add")
    public String addToCart(@RequestParam Long bookId,
                           @RequestParam(defaultValue = "1") int quantity,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        
        try {
            cartService.addToCart(bookId, quantity, session.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Libro agregado al carrito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al agregar el libro al carrito");
        }
        
        return "redirect:/books/" + bookId;
    }
    
    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long cartItemId,
                                @RequestParam int quantity,
                                RedirectAttributes redirectAttributes) {
        
        try {
            cartService.updateQuantity(cartItemId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Cantidad actualizada");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar la cantidad");
        }
        
        return "redirect:/cart";
    }
    
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long cartItemId,
                                RedirectAttributes redirectAttributes) {
        
        try {
            cartService.removeFromCart(cartItemId);
            redirectAttributes.addFlashAttribute("successMessage", "Libro eliminado del carrito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el libro");
        }
        
        return "redirect:/cart";
    }
    
    @PostMapping("/clear")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        
        try {
            cartService.clearCart(session.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Carrito vaciado");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al vaciar el carrito");
        }
        
        return "redirect:/cart";
    }
}