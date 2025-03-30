package com.example.hubspotintegration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HubSpotConfig {
    
    @Value("${hubspot.client.id:810da275-fab9-404a-8779-c78486ed69fc}")
    private String clientId;
    
    @Value("${hubspot.client.secret:b43d492a-04cf-4d84-847f-b6bfb31c5492}")
    private String clientSecret;
    
    @Value("${hubspot.redirect.uri:http://localhost:8080/api/auth/callback}")
    private String redirectUri;
    
    public String getClientId() {
        return clientId;
    }
    
    public String getClientSecret() {
        return clientSecret;
    }
    
    public String getRedirectUri() {
        return redirectUri;
    }
}