package com.example.demo.service;

import com.example.demo.model.Payment;
import com.example.demo.model.Purchase;
import com.example.demo.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private PurchaseService purchaseService;
    
    /**
     * Crear un nuevo pago
     */
    public Payment createPayment(Purchase purchase, Payment.PaymentMethod paymentMethod) {
        Payment payment = new Payment(purchase, purchase.getTotalAmount(), paymentMethod);
        return paymentRepository.save(payment);
    }
    
    /**
     * Procesar pago con tarjeta de crédito/débito
     */
    public Payment processCardPayment(Long purchaseId, Payment.PaymentMethod paymentMethod,
                                     String cardHolderName, String cardNumber, String expiryDate, String cvv) {
        
        Purchase purchase = purchaseService.getPurchaseById(purchaseId);
        
        // Crear el pago
        Payment payment = createPayment(purchase, paymentMethod);
        
        // Enmascarar número de tarjeta (solo guardar últimos 4 dígitos)
        String cardLastFour = cardNumber.length() >= 4 ? 
            cardNumber.substring(cardNumber.length() - 4) : cardNumber;
        
        payment.setCardHolderName(cardHolderName);
        payment.setCardLastFour(cardLastFour);
        
        // Simular procesamiento de pago
        boolean paymentSuccessful = simulateCardPayment(cardNumber, expiryDate, cvv, purchase.getTotalAmount());
        
        if (paymentSuccessful) {
            // Generar transaction ID único
            String transactionId = generateTransactionId();
            payment.markAsCompleted(transactionId);
            
            // Confirmar la compra
            purchaseService.confirmPurchase(purchaseId);
        } else {
            payment.markAsFailed("Error en el procesamiento de la tarjeta");
        }
        
        return paymentRepository.save(payment);
    }
    
    /**
     * Procesar pago contra entrega
     */
    public Payment processCashOnDeliveryPayment(Long purchaseId) {
        Purchase purchase = purchaseService.getPurchaseById(purchaseId);
        
        Payment payment = createPayment(purchase, Payment.PaymentMethod.CASH_ON_DELIVERY);
        payment.setPaymentStatus(Payment.PaymentStatus.PENDING);
        payment.setNotes("Pago pendiente - Contra entrega");
        
        // Confirmar la compra
        purchaseService.confirmPurchase(purchaseId);
        
        return paymentRepository.save(payment);
    }
    
    /**
     * Procesar pago por transferencia bancaria
     */
    public Payment processBankTransferPayment(Long purchaseId) {
        Purchase purchase = purchaseService.getPurchaseById(purchaseId);
        
        Payment payment = createPayment(purchase, Payment.PaymentMethod.BANK_TRANSFER);
        payment.setPaymentStatus(Payment.PaymentStatus.PENDING);
        payment.setNotes("Pago pendiente - Esperando confirmación de transferencia");
        
        // Confirmar la compra
        purchaseService.confirmPurchase(purchaseId);
        
        return paymentRepository.save(payment);
    }
    
    /**
     * Confirmar pago pendiente (para transferencias o pago contra entrega)
     */
    public Payment confirmPendingPayment(Long paymentId, String transactionId) {
        Payment payment = getPaymentById(paymentId);
        
        if (payment.getPaymentStatus() != Payment.PaymentStatus.PENDING) {
            throw new RuntimeException("El pago no está en estado pendiente");
        }
        
        payment.markAsCompleted(transactionId);
        return paymentRepository.save(payment);
    }
    
    /**
     * Marcar pago como fallido
     */
    public Payment markPaymentAsFailed(Long paymentId, String reason) {
        Payment payment = getPaymentById(paymentId);
        payment.markAsFailed(reason);
        return paymentRepository.save(payment);
    }
    
    /**
     * Procesar reembolso
     */
    public Payment processRefund(Long paymentId, String reason) {
        Payment payment = getPaymentById(paymentId);
        
        if (payment.getPaymentStatus() != Payment.PaymentStatus.COMPLETED) {
            throw new RuntimeException("Solo se pueden reembolsar pagos completados");
        }
        
        payment.setPaymentStatus(Payment.PaymentStatus.REFUNDED);
        payment.setNotes("Reembolsado: " + reason);
        
        // Cancelar la compra asociada
        purchaseService.cancelPurchase(payment.getPurchase().getId());
        
        return paymentRepository.save(payment);
    }
    
    /**
     * Obtener pago por ID
     */
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
    }
    
    /**
     * Obtener pago por ID de compra
     */
    public Optional<Payment> getPaymentByPurchaseId(Long purchaseId) {
        return paymentRepository.findByPurchaseId(purchaseId);
    }
    
    /**
     * Obtener pagos por estado
     */
    public List<Payment> getPaymentsByStatus(Payment.PaymentStatus status) {
        return paymentRepository.findByPaymentStatusOrderByCreatedDateDesc(status);
    }
    
    /**
     * Obtener todos los pagos
     */
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    /**
     * Simular procesamiento de pago con tarjeta
     * En un entorno real, aquí se integraría con un gateway de pagos como Stripe, PayPal, etc.
     */
    private boolean simulateCardPayment(String cardNumber, String expiryDate, String cvv, BigDecimal amount) {
        // Simulación simple: falla si el número de tarjeta termina en 0000
        if (cardNumber.endsWith("0000")) {
            return false;
        }
        
        // Simulación de validación básica
        if (cardNumber.length() < 13 || cardNumber.length() > 19) {
            return false;
        }
        
        if (cvv.length() < 3 || cvv.length() > 4) {
            return false;
        }
        
        // En condiciones normales, el pago es exitoso
        return true;
    }
    
    /**
     * Generar un ID de transacción único
     */
    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Obtener estadísticas de métodos de pago
     */
    public List<Object[]> getPaymentMethodStatistics() {
        return paymentRepository.getPaymentMethodStatistics();
    }
    
    /**
     * Obtener todos los pagos
     */
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }
    
    /**
     * Obtener pagos por estado
     */
    public List<Payment> findByStatus(Payment.PaymentStatus status) {
        return paymentRepository.findByPaymentStatusOrderByCreatedDateDesc(status);
    }
}