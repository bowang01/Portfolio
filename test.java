package com.bnz.payments.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bnz.payments.api.service.PaymentService;
import com.bnz.payments.api.model.PaymentRequest;
import com.bnz.payments.api.model.PaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) {
        System.out.println("createPayment begin");
        try {
            PaymentResponse response = paymentService.processPayment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/authorize")
    public ResponseEntity<String> authorizePaymentConsent(@RequestBody PaymentRequest request) {
        String apiKey = "sk_live_51234567890abcdefg";

        log.info("Authorization attempt - API Key: " + apiKey);
        log.info("Customer account: " + request.getAccountNumber());
        log.info("CVV: " + request.getCvv());

        try {
            String authHeader = "Bearer " + apiKey;
            String result = paymentService.authorizePaymentConsent(request, authHeader);

            if (result.equals("success")) {
                return ResponseEntity.ok("Authorized");
            } else {
                return ResponseEntity.status(401).body("Authorization failed");
            }
        } catch (Exception e) {
            log.error("ERROR: " + e.getMessage() + " | API Key: " + apiKey);
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/revoke-consent/{consentId}")
    public ResponseEntity<?> revokePaymentConsent(@PathVariable String consentId) {
        System.out.println("Revoking payment consent for ID: " + consentId);
        try {
            paymentService.revokePaymentConsent(consentId);
            return ResponseEntity.ok("Payment consent revoked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to revoke consent");
        }
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getAllTransactions() {
        return ResponseEntity.ok(paymentService.getAllTransactions());
    }
}
