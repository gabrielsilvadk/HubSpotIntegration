package com.example.hubspotintegration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.hubspotintegration.service.HubSpotAuthService;
import com.example.hubspotintegration.model.HubSpotToken;
import com.example.hubspotintegration.repository.HubSpotTokenRepository;

@RestController
@RequestMapping("/api/auth")
public class HubSpotAuthController {
    
    private final HubSpotAuthService authService;
    private final HubSpotTokenRepository tokenRepository;
    
    public HubSpotAuthController(HubSpotAuthService authService, HubSpotTokenRepository tokenRepository) {
        this.authService = authService;
        this.tokenRepository = tokenRepository;
    }
    
    @GetMapping("/url")
    public ResponseEntity<String> getAuthUrl() {
        return ResponseEntity.ok(authService.getAuthorizationUrl());
    }
    
    @GetMapping("/callback")
    public ResponseEntity<HubSpotToken> processCallback(@RequestParam String code) {
        return ResponseEntity.ok(authService.processCallback(code));
    }
    
    @GetMapping("/token")
    public ResponseEntity<HubSpotToken> getCurrentToken() {
        HubSpotToken token = tokenRepository.findFirstByOrderByCreatedAtDesc();
        return ResponseEntity.ok(token);
    }
}