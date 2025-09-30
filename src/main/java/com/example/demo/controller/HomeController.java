package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private CartService cartService;
    
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        List<Book> books = bookService.getAvailableBooks();
        List<String> categories = bookService.getAllCategories();
        
        model.addAttribute("books", books);
        model.addAttribute("categories", categories);
        model.addAttribute("cartItemCount", cartService.getCartItemCount(session.getId()));
        
        return "index";
    }
    
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword,
                        @RequestParam(required = false) String category,
                        Model model, HttpSession session) {
        
        List<Book> books;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            books = bookService.searchBooks(keyword.trim());
        } else if (category != null && !category.trim().isEmpty()) {
            books = bookService.getBooksByCategory(category.trim());
        } else {
            books = bookService.getAvailableBooks();
        }
        
        List<String> categories = bookService.getAllCategories();
        
        model.addAttribute("books", books);
        model.addAttribute("categories", categories);
        model.addAttribute("searchKeyword", keyword);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("cartItemCount", cartService.getCartItemCount(session.getId()));
        
        return "search";
    }
}