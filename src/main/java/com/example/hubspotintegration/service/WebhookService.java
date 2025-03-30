package com.example.hubspotintegration.service;

import org.springframework.stereotype.Service;
import com.example.hubspotintegration.dto.WebhookPayloadDTO;
import org.springframework.web.client.RestTemplate;
import com.example.hubspotintegration.model.HubSpotToken;
import com.example.hubspotintegration.repository.HubSpotTokenRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;

@Service
public class WebhookService {
    
    private final RestTemplate restTemplate;
    private final HubSpotTokenRepository tokenRepository;
    
    public WebhookService(RestTemplate restTemplate, HubSpotTokenRepository tokenRepository) {
        this.restTemplate = restTemplate;
        this.tokenRepository = tokenRepository;
    }
    
    public void processWebhook(WebhookPayloadDTO payload) {
        System.out.println("Processing webhook with " + payload.getEvents().size() + " events");
        
        for (WebhookPayloadDTO.WebhookEventDTO event : payload.getEvents()) {
            if ("contact.creation".equals(event.getEventType())) {
                System.out.println("Contact creation event detected");
                System.out.println("Contact ID: " + event.getObjectId());
                
                // Fetch contact details from HubSpot
                fetchContactDetails(event.getObjectId());
            }
        }
    }
    
    private void fetchContactDetails(String contactId) {
        try {
            HubSpotToken token = tokenRepository.findFirstByOrderByCreatedAtDesc();
            if (token == null) {
                throw new RuntimeException("Token not found");
            }
            
            String url = "https://api.hubapi.com/crm/v3/objects/contacts/" + contactId;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + token.getAccessToken().trim());
            headers.set("Accept", "application/json");
            
            HttpEntity<String> request = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            System.out.println("Contact details: " + response.getBody());
            
        } catch (Exception e) {
            System.out.println("Error fetching contact details: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 