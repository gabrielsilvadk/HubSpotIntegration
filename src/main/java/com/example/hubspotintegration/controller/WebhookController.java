package com.example.hubspotintegration.controller;

import org.springframework.web.bind.annotation.*;
import com.example.hubspotintegration.dto.WebhookPayloadDTO;
import com.example.hubspotintegration.service.WebhookService;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {
    
    private final WebhookService webhookService;
    
    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }
    
    @PostMapping("/hubspot")
    public ResponseEntity<Void> handleHubSpotWebhook(@RequestBody WebhookPayloadDTO payload) {
        System.out.println("Webhook recebido do HubSpot");
        webhookService.processWebhook(payload);
        return ResponseEntity.ok().build();
    }
} 