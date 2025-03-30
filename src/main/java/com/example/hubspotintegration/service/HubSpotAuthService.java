package com.example.hubspotintegration.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.hubspotintegration.model.HubSpotToken;
import com.example.hubspotintegration.repository.HubSpotTokenRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

@Service
public class HubSpotAuthService {
    
    private final RestTemplate restTemplate;
    private final HubSpotTokenRepository tokenRepository;
    private final ObjectMapper objectMapper;
    
    @Value("${hubspot.client.id}")
    private String clientId;
    
    @Value("${hubspot.client.secret}")
    private String clientSecret;
    
    @Value("${hubspot.redirect.uri}")
    private String redirectUri;
    
    public HubSpotAuthService(RestTemplate restTemplate, HubSpotTokenRepository tokenRepository, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.tokenRepository = tokenRepository;
        this.objectMapper = objectMapper;
    }
    
    public String getAuthorizationUrl() {
        String scopes = "crm.objects.contacts.read crm.objects.contacts.write";
        return String.format(
            "https://app.hubspot.com/oauth/authorize?client_id=%s&redirect_uri=%s&scope=%s&response_type=code",
            clientId,
            redirectUri,
            scopes
        );
    }
    
    public HubSpotToken processCallback(String code) {
        System.out.println("Processing callback with code: " + code);
        System.out.println("Client ID: " + clientId);
        System.out.println("Client Secret: " + clientSecret);
        System.out.println("Redirect URI: " + redirectUri);
        
        String url = "https://api.hubapi.com/oauth/v1/token";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        
        System.out.println("Request parameters: " + params);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("HubSpot response: " + response.getBody());
            
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);
            
            HubSpotToken token = new HubSpotToken();
            token.setAccessToken((String) responseMap.get("access_token"));
            token.setRefreshToken((String) responseMap.get("refresh_token"));
            token.setExpiresIn(((Number) responseMap.get("expires_in")).longValue());
            token.setCreatedAt(LocalDateTime.now());
            
            HubSpotToken savedToken = tokenRepository.save(token);
            System.out.println("Token saved with ID: " + savedToken.getId());
            System.out.println("Access Token: " + savedToken.getAccessToken());
            
            return savedToken;
        } catch (Exception e) {
            System.out.println("Error processing callback: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error processing HubSpot callback", e);
        }
    }
}