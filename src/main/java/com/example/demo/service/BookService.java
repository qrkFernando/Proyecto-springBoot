package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    // Obtener todos los libros
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    // Obtener libro por ID
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }
    
    // Obtener libros disponibles
    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailableTrue();
    }
    
    // Buscar libros por categoría
    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategoryIgnoreCase(category);
    }
    
    // Buscar libros por palabra clave
    public List<Book> searchBooks(String keyword) {
        return bookRepository.findByTitleOrAuthorContainingIgnoreCase(keyword);
    }
    
    // Buscar libros por rango de precio
    public List<Book> getBooksByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return bookRepository.findByPriceBetween(minPrice, maxPrice);
    }
    
    // Obtener todas las categorías
    public List<String> getAllCategories() {
        return bookRepository.findAllCategories();
    }
    
    // Guardar libro
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }
    
    // Eliminar libro
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
    
    // Verificar si hay stock disponible
    public boolean hasStock(Long bookId, int quantity) {
        Optional<Book> book = bookRepository.findById(bookId);
        return book.isPresent() && book.get().getStock() >= quantity && book.get().getAvailable();
    }
    
    // Reducir stock
    public void reduceStock(Long bookId, int quantity) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            int newStock = book.getStock() - quantity;
            book.setStock(Math.max(0, newStock));
            if (newStock <= 0) {
                book.setAvailable(false);
            }
            bookRepository.save(book);
        }
    }
}