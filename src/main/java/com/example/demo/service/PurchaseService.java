package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.PurchaseRepository;
import com.example.demo.repository.PurchaseItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PurchaseService {
    
    @Autowired
    private PurchaseRepository purchaseRepository;
    
    @Autowired
    private PurchaseItemRepository purchaseItemRepository;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private BookService bookService;
    
    /**
     * Crear una nueva compra a partir del carrito de compras
     */
    public Purchase createPurchaseFromCart(String sessionId, String customerName, 
                                         String customerEmail, String shippingAddress, 
                                         String customerPhone) {
        
        // Obtener items del carrito
        List<CartItem> cartItems = cartService.getCartItems(sessionId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }
        
        // Validar stock disponible para todos los items
        validateStockAvailability(cartItems);
        
        // Calcular total
        BigDecimal totalAmount = cartService.getCartTotal(sessionId);
        
        // Crear la compra
        Purchase purchase = new Purchase(customerName, customerEmail, shippingAddress, 
                                       customerPhone, totalAmount, sessionId);
        
        final Purchase savedPurchase = purchaseRepository.save(purchase);
        
        // Crear los items de compra
        List<PurchaseItem> purchaseItems = cartItems.stream()
            .map(cartItem -> new PurchaseItem(savedPurchase, cartItem.getBook(), cartItem.getQuantity()))
            .collect(Collectors.toList());
        
        purchaseItemRepository.saveAll(purchaseItems);
        savedPurchase.setPurchaseItems(purchaseItems);
        
        // Actualizar stock de libros
        updateBookStock(cartItems);
        
        return savedPurchase;
    }
    
    /**
     * Validar que hay suficiente stock para todos los items del carrito
     */
    private void validateStockAvailability(List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            Book book = item.getBook();
            if (!book.getAvailable()) {
                throw new RuntimeException("El libro '" + book.getTitle() + "' no está disponible");
            }
            if (book.getStock() < item.getQuantity()) {
                throw new RuntimeException("Stock insuficiente para el libro '" + book.getTitle() + 
                                         "'. Stock disponible: " + book.getStock() + 
                                         ", Cantidad solicitada: " + item.getQuantity());
            }
        }
    }
    
    /**
     * Actualizar el stock de los libros después de una compra
     */
    private void updateBookStock(List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            Book book = item.getBook();
            int newStock = book.getStock() - item.getQuantity();
            book.setStock(newStock);
            
            // Si el stock llega a 0, marcar como no disponible
            if (newStock <= 0) {
                book.setAvailable(false);
            }
            
            bookService.save(book);
        }
    }
    
    /**
     * Confirmar una compra (cambiar estado a CONFIRMED)
     */
    public Purchase confirmPurchase(Long purchaseId) {
        Purchase purchase = getPurchaseById(purchaseId);
        purchase.setStatus(Purchase.PurchaseStatus.CONFIRMED);
        return purchaseRepository.save(purchase);
    }
    
    /**
     * Cancelar una compra y restaurar el stock
     */
    public Purchase cancelPurchase(Long purchaseId) {
        Purchase purchase = getPurchaseById(purchaseId);
        
        if (purchase.getStatus() == Purchase.PurchaseStatus.DELIVERED) {
            throw new RuntimeException("No se puede cancelar una compra que ya fue entregada");
        }
        
        // Restaurar stock
        for (PurchaseItem item : purchase.getPurchaseItems()) {
            Book book = item.getBook();
            book.setStock(book.getStock() + item.getQuantity());
            book.setAvailable(true);
            bookService.save(book);
        }
        
        purchase.setStatus(Purchase.PurchaseStatus.CANCELLED);
        return purchaseRepository.save(purchase);
    }
    
    /**
     * Actualizar estado de una compra
     */
    public Purchase updatePurchaseStatus(Long purchaseId, Purchase.PurchaseStatus status) {
        Purchase purchase = getPurchaseById(purchaseId);
        purchase.setStatus(status);
        return purchaseRepository.save(purchase);
    }
    
    /**
     * Obtener compra por ID
     */
    public Purchase getPurchaseById(Long id) {
        return purchaseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Compra no encontrada con ID: " + id));
    }
    
    /**
     * Obtener todas las compras
     */
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }
    
    /**
     * Obtener compras por email del cliente
     */
    public List<Purchase> getPurchasesByCustomerEmail(String email) {
        return purchaseRepository.findByCustomerEmailOrderByPurchaseDateDesc(email);
    }
    
    /**
     * Obtener compras por estado
     */
    public List<Purchase> getPurchasesByStatus(Purchase.PurchaseStatus status) {
        return purchaseRepository.findByStatusOrderByPurchaseDateDesc(status);
    }
    
    /**
     * Obtener compras por session ID
     */
    public List<Purchase> getPurchasesBySessionId(String sessionId) {
        return purchaseRepository.findBySessionIdOrderByPurchaseDateDesc(sessionId);
    }
    
    /**
     * Obtener la compra más reciente por session ID
     */
    public Optional<Purchase> getLatestPurchaseBySessionId(String sessionId) {
        return purchaseRepository.findFirstBySessionIdOrderByPurchaseDateDesc(sessionId);
    }
    
    /**
     * Obtener estadísticas de compras
     */
    public long getPurchaseCountByStatus(Purchase.PurchaseStatus status) {
        return purchaseRepository.countByStatus(status);
    }
}