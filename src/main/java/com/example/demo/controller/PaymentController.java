package com.example.demo.controller;

import com.example.demo.model.Payment;
import com.example.demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    /**
     * Panel administrativo de pagos
     */
    @GetMapping("/admin")
    public String showPaymentAdmin(Model model, 
                                 @RequestParam(required = false) Payment.PaymentStatus status) {
        
        try {
            List<Payment> payments;
            if (status != null) {
                payments = paymentService.findByStatus(status);
                model.addAttribute("filterStatus", status);
            } else {
                payments = paymentService.findAll();
            }
            
            model.addAttribute("payments", payments);
            model.addAttribute("paymentStatuses", Payment.PaymentStatus.values());
            model.addAttribute("message", "Panel de pagos funcionando correctamente");
            
            // Estadísticas
            model.addAttribute("totalPayments", payments.size());
            model.addAttribute("completedPayments", 
                payments.stream().filter(p -> p.getPaymentStatus() == Payment.PaymentStatus.COMPLETED).count());
            model.addAttribute("pendingPayments", 
                payments.stream().filter(p -> p.getPaymentStatus() == Payment.PaymentStatus.PENDING).count());
                
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los pagos: " + e.getMessage());
        }
        
        return "payment-admin";
    }
    
    /**
     * Confirmar un pago pendiente
     */
    @PostMapping("/admin/confirm")
    public String confirmPayment(@RequestParam Long paymentId,
                               @RequestParam String transactionId,
                               RedirectAttributes redirectAttributes) {
        try {
            paymentService.confirmPendingPayment(paymentId, transactionId);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Pago confirmado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error al confirmar el pago: " + e.getMessage());
        }
        
        return "redirect:/payment/admin";
    }
    
    /**
     * Marcar un pago como fallido
     */
    @PostMapping("/admin/mark-failed")
    public String markPaymentAsFailed(@RequestParam Long paymentId,
                                    @RequestParam String reason,
                                    RedirectAttributes redirectAttributes) {
        try {
            paymentService.markPaymentAsFailed(paymentId, reason);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Pago marcado como fallido");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error al marcar el pago como fallido: " + e.getMessage());
        }
        
        return "redirect:/payment/admin";
    }
    
    /**
     * Procesar reembolso
     */
    @PostMapping("/admin/refund")
    public String processRefund(@RequestParam Long paymentId,
                              @RequestParam String reason,
                              RedirectAttributes redirectAttributes) {
        try {
            paymentService.processRefund(paymentId, reason);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Reembolso procesado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error al procesar el reembolso: " + e.getMessage());
        }
        
        return "redirect:/payment/admin";
    }
    
    /**
     * Ver detalles de un pago específico
     */
    @GetMapping("/details/{paymentId}")
    public String showPaymentDetails(@PathVariable Long paymentId, Model model) {
        try {
            Payment payment = paymentService.getPaymentById(paymentId);
            model.addAttribute("payment", payment);
            return "payment-details";
        } catch (Exception e) {
            return "redirect:/payment/admin?error=payment_not_found";
        }
    }
}