package com.example.demo.repository;

import com.example.demo.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    
    // Buscar compras por email del cliente
    List<Purchase> findByCustomerEmailOrderByPurchaseDateDesc(String customerEmail);
    
    // Buscar compras por estado
    List<Purchase> findByStatusOrderByPurchaseDateDesc(Purchase.PurchaseStatus status);
    
    // Buscar compras por session ID
    List<Purchase> findBySessionIdOrderByPurchaseDateDesc(String sessionId);
    
    // Buscar compras en un rango de fechas
    @Query("SELECT p FROM Purchase p WHERE p.purchaseDate BETWEEN :startDate AND :endDate ORDER BY p.purchaseDate DESC")
    List<Purchase> findByPurchaseDateBetween(@Param("startDate") LocalDateTime startDate, 
                                           @Param("endDate") LocalDateTime endDate);
    
    // Buscar la compra más reciente por session ID
    Optional<Purchase> findFirstBySessionIdOrderByPurchaseDateDesc(String sessionId);
    
    // Contar compras por estado
    long countByStatus(Purchase.PurchaseStatus status);
    
    // Buscar compras que contengan un libro específico
    @Query("SELECT DISTINCT p FROM Purchase p JOIN p.purchaseItems pi WHERE pi.book.id = :bookId")
    List<Purchase> findPurchasesContainingBook(@Param("bookId") Long bookId);
}