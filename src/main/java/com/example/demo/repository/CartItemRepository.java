package com.example.demo.repository;

import com.example.demo.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    // Obtener todos los items del carrito por session ID
    List<CartItem> findBySessionId(String sessionId);
    
    // Buscar un item específico por libro y session ID
    Optional<CartItem> findByBookIdAndSessionId(Long bookId, String sessionId);
    
    // Eliminar todos los items de una sesión
    void deleteBySessionId(String sessionId);
}