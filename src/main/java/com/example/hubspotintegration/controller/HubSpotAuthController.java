package com.example.hubspotintegration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.hubspotintegration.service.HubSpotAuthService;
import com.example.hubspotintegration.model.HubSpotToken;

@RestController
@RequestMapping("/api/auth")
public class HubSpotAuthController {
    
    private final HubSpotAuthService authService;
    
    public HubSpotAuthController(HubSpotAuthService authService) {
        this.authService = authService;
    }
    
    @GetMapping("/url")
    public ResponseEntity<String> getAuthUrl() {
        return ResponseEntity.ok(authService.generateAuthUrl());
    }
    
    @GetMapping("/callback")
    public ResponseEntity<HubSpotToken> processCallback(@RequestParam String code) {
        return ResponseEntity.ok(authService.processCallback(code));
    }
}