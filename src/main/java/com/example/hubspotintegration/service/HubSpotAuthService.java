package com.example.hubspotintegration.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.hubspotintegration.model.HubSpotToken;
import com.example.hubspotintegration.repository.HubSpotTokenRepository;
import com.example.hubspotintegration.dto.HubSpotAuthResponseDTO;
import java.time.LocalDateTime;

@Service
public class HubSpotAuthService {
    
    @Value("${hubspot.client.id}")
    private String clientId;
    
    @Value("${hubspot.client.secret}")
    private String clientSecret;
    
    @Value("${hubspot.redirect.uri}")
    private String redirectUri;
    
    private final RestTemplate restTemplate;
    private final HubSpotTokenRepository tokenRepository;
    
    public HubSpotAuthService(RestTemplate restTemplate, HubSpotTokenRepository tokenRepository) {
        this.restTemplate = restTemplate;
        this.tokenRepository = tokenRepository;
    }
    
    public String generateAuthUrl() {
        return String.format(
            "https://app.hubspot.com/oauth/authorize?client_id=%s&redirect_uri=%s&scope=contacts",
            clientId,
            redirectUri
        );
    }
    
    public HubSpotToken processCallback(String code) {
        String tokenUrl = String.format(
            "https://api.hubapi.com/oauth/v1/token?grant_type=authorization_code&client_id=%s&client_secret=%s&redirect_uri=%s&code=%s",
            clientId,
            clientSecret,
            redirectUri,
            code
        );
        
        HubSpotAuthResponseDTO response = restTemplate.postForObject(tokenUrl, null, HubSpotAuthResponseDTO.class);
        
        HubSpotToken token = new HubSpotToken();
        token.setAccessToken(response.getAccess_token());
        token.setRefreshToken(response.getRefresh_token());
        token.setExpiresIn(response.getExpires_in());
        token.setCreatedAt(LocalDateTime.now());
        
        return tokenRepository.save(token);
    }
}