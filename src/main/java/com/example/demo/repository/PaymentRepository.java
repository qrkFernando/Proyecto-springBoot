package com.example.demo.repository;

import com.example.demo.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    // Buscar pago por ID de compra
    Optional<Payment> findByPurchaseId(Long purchaseId);
    
    // Buscar pagos por estado
    List<Payment> findByPaymentStatusOrderByCreatedDateDesc(Payment.PaymentStatus paymentStatus);
    
    // Buscar pagos por método de pago
    List<Payment> findByPaymentMethodOrderByCreatedDateDesc(Payment.PaymentMethod paymentMethod);
    
    // Buscar pago por transaction ID
    Optional<Payment> findByTransactionId(String transactionId);
    
    // Buscar pagos en un rango de fechas
    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate ORDER BY p.paymentDate DESC")
    List<Payment> findByPaymentDateBetween(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    // Contar pagos por estado
    long countByPaymentStatus(Payment.PaymentStatus paymentStatus);
    
    // Obtener estadísticas de métodos de pago más utilizados
    @Query("SELECT p.paymentMethod, COUNT(p) FROM Payment p WHERE p.paymentStatus = 'COMPLETED' GROUP BY p.paymentMethod ORDER BY COUNT(p) DESC")
    List<Object[]> getPaymentMethodStatistics();
}