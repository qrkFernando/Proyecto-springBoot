package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private CartService cartService;
    
    @GetMapping("/{id}")
    public String bookDetail(@PathVariable Long id, Model model, HttpSession session) {
        Optional<Book> bookOpt = bookService.getBookById(id);
        
        if (bookOpt.isPresent()) {
            model.addAttribute("book", bookOpt.get());
            model.addAttribute("cartItemCount", cartService.getCartItemCount(session.getId()));
            return "book-detail";
        } else {
            return "redirect:/";
        }
    }
    
    @GetMapping("/category/{category}")
    public String booksByCategory(@PathVariable String category, Model model, HttpSession session) {
        List<Book> books = bookService.getBooksByCategory(category);
        List<String> categories = bookService.getAllCategories();
        
        model.addAttribute("books", books);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("cartItemCount", cartService.getCartItemCount(session.getId()));
        
        return "category";
    }
}