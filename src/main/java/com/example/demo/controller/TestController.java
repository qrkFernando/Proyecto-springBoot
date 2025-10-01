package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.service.BookService;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private BookService bookService;
    
    @Autowired
    private CartService cartService;

    @GetMapping("/info")
    public String testInfo(Model model, HttpSession session) {
        try {
            // Información del sistema
            model.addAttribute("totalBooks", bookService.countBooks());
            model.addAttribute("availableBooks", bookService.countAvailableBooks());
            model.addAttribute("cartItems", cartService.getCartItemCount(session.getId()));
            
            // Información de la sesión  
            model.addAttribute("sessionId", session.getId());
            model.addAttribute("sessionCreationTime", new java.util.Date(session.getCreationTime()));
            model.addAttribute("sessionLastAccessed", new java.util.Date(session.getLastAccessedTime()));
            
            // Libros destacados para la prueba
            List<Book> featuredBooks = bookService.findAvailableBooks().stream()
                    .limit(3)
                    .toList();
            model.addAttribute("featuredBooks", featuredBooks);
            
            // Estado del carrito
            List<CartItem> cartItems = cartService.getCartItems(session.getId());
            model.addAttribute("cartItemsList", cartItems);
            double cartTotal = cartItems.stream()
                    .mapToDouble(item -> item.getBook().getPrice().doubleValue() * item.getQuantity())
                    .sum();
            model.addAttribute("cartTotal", cartTotal);
            
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar información del sistema: " + e.getMessage());
            model.addAttribute("totalBooks", 0);
            model.addAttribute("availableBooks", 0);
            model.addAttribute("cartItems", 0);
            model.addAttribute("cartTotal", 0.0);
            model.addAttribute("sessionId", session.getId());
            model.addAttribute("sessionCreationTime", new java.util.Date());
            model.addAttribute("sessionLastAccessed", new java.util.Date());
        }
        
        return "test-info";
    }

    @PostMapping("/add-sample-books")
    public String addSampleBooksToCart(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            // Agregar algunos libros de muestra al carrito
            List<Book> availableBooks = bookService.findAvailableBooks();
            int booksAdded = 0;
            
            for (Book book : availableBooks.stream().limit(3).toList()) {
                cartService.addToCart(book.getId(), 1, session.getId());
                booksAdded++;
            }
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Se agregaron " + booksAdded + " libros de muestra al carrito.");
                
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error al agregar libros de muestra: " + e.getMessage());
        }
        
        return "redirect:/test/info";
    }

    @PostMapping("/clear-test-data")
    public String clearTestData(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            cartService.clearCart(session.getId());
            redirectAttributes.addFlashAttribute("successMessage", 
                "Datos de prueba eliminados exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error al limpiar datos de prueba: " + e.getMessage());
        }
        
        return "redirect:/test/info";
    }

    @GetMapping("/system-status")
    public String systemStatus(Model model) {
        // Estado del sistema
        model.addAttribute("javaVersion", System.getProperty("java.version"));
        model.addAttribute("springBootVersion", org.springframework.boot.SpringBootVersion.getVersion());
        model.addAttribute("serverTime", new java.util.Date());
        model.addAttribute("availableMemory", Runtime.getRuntime().freeMemory());
        model.addAttribute("totalMemory", Runtime.getRuntime().totalMemory());
        model.addAttribute("maxMemory", Runtime.getRuntime().maxMemory());
        
        // Estado de la base de datos
        try {
            long totalBooks = bookService.countBooks();
            model.addAttribute("databaseStatus", "Conectado");
            model.addAttribute("totalBooksInDB", totalBooks);
        } catch (Exception e) {
            model.addAttribute("databaseStatus", "Error: " + e.getMessage());
            model.addAttribute("totalBooksInDB", 0);
        }
        
        return "test-info";
    }
}