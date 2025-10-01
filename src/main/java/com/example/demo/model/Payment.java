package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;
    
    @NotNull(message = "El monto es obligatorio")
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    @Column(name = "transaction_id")
    private String transactionId;
    
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
    
    // Campos para tarjeta de crédito (enmascarados para seguridad)
    @Column(name = "card_last_four")
    private String cardLastFour;
    
    @Column(name = "card_holder_name")
    private String cardHolderName;
    
    // Campo para notas adicionales
    @Column(name = "notes", length = 500)
    private String notes;
    
    public enum PaymentMethod {
        CREDIT_CARD("Tarjeta de Crédito"),
        DEBIT_CARD("Tarjeta de Débito"),
        BANK_TRANSFER("Transferencia Bancaria"),
        CASH_ON_DELIVERY("Pago Contra Entrega"),
        PAYPAL("PayPal");
        
        private final String displayName;
        
        PaymentMethod(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum PaymentStatus {
        PENDING("Pendiente"),
        PROCESSING("Procesando"),
        COMPLETED("Completado"),
        FAILED("Fallido"),
        REFUNDED("Reembolsado"),
        CANCELLED("Cancelado");
        
        private final String displayName;
        
        PaymentStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    // Constructores
    public Payment() {
        this.createdDate = LocalDateTime.now();
    }
    
    public Payment(Purchase purchase, BigDecimal amount, PaymentMethod paymentMethod) {
        this();
        this.purchase = purchase;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }
    
    // Método para marcar el pago como completado
    public void markAsCompleted(String transactionId) {
        this.paymentStatus = PaymentStatus.COMPLETED;
        this.transactionId = transactionId;
        this.paymentDate = LocalDateTime.now();
    }
    
    // Método para marcar el pago como fallido
    public void markAsFailed(String reason) {
        this.paymentStatus = PaymentStatus.FAILED;
        this.notes = reason;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Purchase getPurchase() { return purchase; }
    public void setPurchase(Purchase purchase) { this.purchase = purchase; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public String getCardLastFour() { return cardLastFour; }
    public void setCardLastFour(String cardLastFour) { this.cardLastFour = cardLastFour; }
    
    public String getCardHolderName() { return cardHolderName; }
    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}