package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    // Agregar item al carrito
    public void addToCart(Long bookId, int quantity, String sessionId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            
            // Verificar si el item ya existe en el carrito
            Optional<CartItem> existingItem = cartItemRepository.findByBookIdAndSessionId(bookId, sessionId);
            
            if (existingItem.isPresent()) {
                // Actualizar cantidad
                CartItem item = existingItem.get();
                item.setQuantity(item.getQuantity() + quantity);
                cartItemRepository.save(item);
            } else {
                // Crear nuevo item
                CartItem newItem = new CartItem(book, quantity, sessionId);
                cartItemRepository.save(newItem);
            }
        }
    }
    
    // Obtener items del carrito
    public List<CartItem> getCartItems(String sessionId) {
        return cartItemRepository.findBySessionId(sessionId);
    }
    
    // Actualizar cantidad de un item
    public void updateQuantity(Long cartItemId, int quantity) {
        Optional<CartItem> itemOpt = cartItemRepository.findById(cartItemId);
        if (itemOpt.isPresent()) {
            CartItem item = itemOpt.get();
            if (quantity <= 0) {
                cartItemRepository.delete(item);
            } else {
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            }
        }
    }
    
    // Remover item del carrito
    public void removeFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
    
    // Limpiar carrito
    public void clearCart(String sessionId) {
        cartItemRepository.deleteBySessionId(sessionId);
    }
    
    // Calcular total del carrito
    public BigDecimal getCartTotal(String sessionId) {
        List<CartItem> items = cartItemRepository.findBySessionId(sessionId);
        return items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // Contar items en el carrito
    public int getCartItemCount(String sessionId) {
        List<CartItem> items = cartItemRepository.findBySessionId(sessionId);
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}