package com.example.demo.controller;

import com.example.demo.model.Payment;
import com.example.demo.model.Purchase;
import com.example.demo.service.CartService;
import com.example.demo.service.PaymentService;
import com.example.demo.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {
    
    @Autowired
    private PurchaseService purchaseService;
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private CartService cartService;
    
    /**
     * Mostrar página de checkout
     */
    @GetMapping("/checkout")
    public String showCheckout(Model model, HttpSession session) {
        String sessionId = session.getId();
        
        // Verificar que el carrito no esté vacío
        if (cartService.getCartItemCount(sessionId) == 0) {
            return "redirect:/cart?error=empty";
        }
        
        model.addAttribute("cartItems", cartService.getCartItems(sessionId));
        model.addAttribute("cartTotal", cartService.getCartTotal(sessionId));
        model.addAttribute("purchase", new Purchase());
        // Solo permitir tarjetas de crédito y débito
        Payment.PaymentMethod[] allowedMethods = {
            Payment.PaymentMethod.CREDIT_CARD, 
            Payment.PaymentMethod.DEBIT_CARD
        };
        model.addAttribute("paymentMethods", allowedMethods);
        
        return "checkout";
    }
    
    /**
     * Procesar la compra
     */
    @PostMapping("/process")
    public String processPurchase(@ModelAttribute Purchase purchase,
                                @RequestParam("paymentMethod") Payment.PaymentMethod paymentMethod,
                                @RequestParam(value = "cardHolderName", required = false) String cardHolderName,
                                @RequestParam(value = "cardNumber", required = false) String cardNumber,
                                @RequestParam(value = "expiryDate", required = false) String expiryDate,
                                @RequestParam(value = "cvv", required = false) String cvv,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        
        System.out.println("=== PROCESANDO COMPRA ===");
        System.out.println("Método de pago: " + paymentMethod);
        System.out.println("Cliente: " + purchase.getCustomerName());
        System.out.println("Email: " + purchase.getCustomerEmail());
        System.out.println("Teléfono: " + purchase.getCustomerPhone());
        System.out.println("Dirección: " + purchase.getShippingAddress());
        
        // Validación básica manual
        if (purchase.getCustomerName() == null || purchase.getCustomerName().trim().isEmpty()) {
            model.addAttribute("errorMessage", "El nombre es obligatorio");
            String sessionId = session.getId();
            model.addAttribute("cartItems", cartService.getCartItems(sessionId));
            model.addAttribute("cartTotal", cartService.getCartTotal(sessionId));
            model.addAttribute("paymentMethods", Payment.PaymentMethod.values());
            return "checkout";
        }
        
        if (purchase.getCustomerEmail() == null || purchase.getCustomerEmail().trim().isEmpty()) {
            model.addAttribute("errorMessage", "El email es obligatorio");
            String sessionId = session.getId();
            model.addAttribute("cartItems", cartService.getCartItems(sessionId));
            model.addAttribute("cartTotal", cartService.getCartTotal(sessionId));
            model.addAttribute("paymentMethods", Payment.PaymentMethod.values());
            return "checkout";
        }
        
        try {
            String sessionId = session.getId();
            System.out.println("Session ID: " + sessionId);
            
            // Verificar que el carrito no esté vacío
            int cartItemCount = cartService.getCartItemCount(sessionId);
            System.out.println("Items en carrito: " + cartItemCount);
            
            if (cartItemCount == 0) {
                System.out.println("ERROR: Carrito vacío");
                redirectAttributes.addFlashAttribute("errorMessage", "El carrito está vacío");
                return "redirect:/cart";
            }
            
            // Crear la compra
            System.out.println("Creando compra...");
            Purchase savedPurchase = purchaseService.createPurchaseFromCart(
                sessionId, 
                purchase.getCustomerName(),
                purchase.getCustomerEmail(),
                purchase.getShippingAddress(),
                purchase.getCustomerPhone()
            );
            System.out.println("Compra creada con ID: " + savedPurchase.getId());
            
            // Procesar el pago según el método seleccionado
            System.out.println("Procesando pago...");
            Payment payment = processPaymentByMethod(savedPurchase.getId(), paymentMethod, 
                                                   cardHolderName, cardNumber, expiryDate, cvv);
            System.out.println("Pago procesado. Estado: " + payment.getPaymentStatus());
            
            // Limpiar el carrito después de una compra exitosa
            if (payment.getPaymentStatus() == Payment.PaymentStatus.COMPLETED ||
                payment.getPaymentStatus() == Payment.PaymentStatus.PENDING) {
                cartService.clearCart(sessionId);
                System.out.println("Carrito limpiado");
            }
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "¡Compra realizada exitosamente! Número de orden: " + savedPurchase.getId());
            
            System.out.println("Redirigiendo a confirmación...");
            return "redirect:/purchase/confirmation/" + savedPurchase.getId();
            
        } catch (Exception e) {
            System.out.println("ERROR EN PROCESO DE COMPRA:");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error al procesar la compra: " + e.getMessage());
            return "redirect:/purchase/checkout";
        }
    }
    
    /**
     * Procesar pago según el método seleccionado
     */
    private Payment processPaymentByMethod(Long purchaseId, Payment.PaymentMethod paymentMethod,
                                         String cardHolderName, String cardNumber, 
                                         String expiryDate, String cvv) {
        
        switch (paymentMethod) {
            case CREDIT_CARD:
            case DEBIT_CARD:
                return paymentService.processCardPayment(purchaseId, paymentMethod, 
                                                       cardHolderName, cardNumber, expiryDate, cvv);
            case CASH_ON_DELIVERY:
                return paymentService.processCashOnDeliveryPayment(purchaseId);
            case BANK_TRANSFER:
                return paymentService.processBankTransferPayment(purchaseId);
            default:
                throw new RuntimeException("Método de pago no soportado: " + paymentMethod);
        }
    }
    
    /**
     * Mostrar página de confirmación de compra
     */
    @GetMapping("/confirmation/{purchaseId}")
    public String showConfirmation(@PathVariable Long purchaseId, Model model) {
        try {
            Purchase purchase = purchaseService.getPurchaseById(purchaseId);
            Payment payment = paymentService.getPaymentByPurchaseId(purchaseId).orElse(null);
            
            model.addAttribute("purchase", purchase);
            model.addAttribute("payment", payment);
            
            return "purchase-confirmation";
        } catch (Exception e) {
            return "redirect:/books?error=purchase_not_found";
        }
    }
    
    /**
     * Ver historial de compras por email
     */
    @GetMapping("/history")
    public String showPurchaseHistory(@RequestParam(required = false) String email, Model model) {
        if (email != null && !email.trim().isEmpty()) {
            try {
                System.out.println("🔍 Buscando compras para email: " + email);
                List<Purchase> purchases = purchaseService.getPurchasesByCustomerEmail(email.trim());
                System.out.println("📋 Compras encontradas: " + purchases.size());
                
                model.addAttribute("purchases", purchases);
                model.addAttribute("searchEmail", email);
                
                if (purchases.isEmpty()) {
                    model.addAttribute("message", "No se encontraron compras para el email: " + email);
                    System.out.println("❌ No hay compras para este email");
                } else {
                    System.out.println("✅ Mostrando " + purchases.size() + " compras");
                }
            } catch (Exception e) {
                System.out.println("🚨 Error al buscar compras: " + e.getMessage());
                model.addAttribute("errorMessage", "Error al buscar compras: " + e.getMessage());
            }
        } else {
            model.addAttribute("message", "Ingresa tu email para buscar tu historial de compras");
        }
        
        return "purchase-history";
    }
    
    /**
     * Ver detalles de una compra específica
     */
    @GetMapping("/details/{purchaseId}")
    public String showPurchaseDetails(@PathVariable Long purchaseId, Model model) {
        try {
            Purchase purchase = purchaseService.getPurchaseById(purchaseId);
            Payment payment = paymentService.getPaymentByPurchaseId(purchaseId).orElse(null);
            
            model.addAttribute("purchase", purchase);
            model.addAttribute("payment", payment);
            
            return "purchase-details";
        } catch (Exception e) {
            return "redirect:/purchase/history?error=purchase_not_found";
        }
    }
    

    
    /**
     * Panel administrativo para gestionar compras
     */
    @GetMapping("/admin")
    public String showAdminPanel(Model model, 
                               @RequestParam(required = false) Purchase.PurchaseStatus status) {
        
        List<Purchase> purchases;
        if (status != null) {
            purchases = purchaseService.getPurchasesByStatus(status);
        } else {
            purchases = purchaseService.getAllPurchases();
        }
        
        model.addAttribute("purchases", purchases);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("allStatuses", Purchase.PurchaseStatus.values());
        
        // Estadísticas básicas
        model.addAttribute("pendingCount", purchaseService.getPurchaseCountByStatus(Purchase.PurchaseStatus.PENDING));
        model.addAttribute("confirmeddCount", purchaseService.getPurchaseCountByStatus(Purchase.PurchaseStatus.CONFIRMED));
        model.addAttribute("shippedCount", purchaseService.getPurchaseCountByStatus(Purchase.PurchaseStatus.SHIPPED));
        
        return "purchase-admin";
    }
    
    /**
     * Actualizar estado de una compra (admin)
     */
    @PostMapping("/admin/update-status")
    public String updatePurchaseStatus(@RequestParam Long purchaseId,
                                     @RequestParam Purchase.PurchaseStatus newStatus,
                                     RedirectAttributes redirectAttributes) {
        try {
            purchaseService.updatePurchaseStatus(purchaseId, newStatus);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Estado de la compra actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error al actualizar el estado: " + e.getMessage());
        }
        
        return "redirect:/purchase/admin";
    }
}