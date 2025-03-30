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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.hubspotintegration.repository.ContactRepository;

@Service
public class WebhookService {
    
    private final RestTemplate restTemplate;
    private final HubSpotTokenRepository tokenRepository;
    private final ContactRepository contactRepository;
    
    public WebhookService(RestTemplate restTemplate, HubSpotTokenRepository tokenRepository, ContactRepository contactRepository) {
        this.restTemplate = restTemplate;
        this.tokenRepository = tokenRepository;
        this.contactRepository = contactRepository;
    }
    
    public void processWebhook(WebhookPayloadDTO payload) {
        System.out.println("=== Webhook Recebido ===");
        System.out.println("Número de eventos: " + payload.getEvents().size());
        
        for (WebhookPayloadDTO.WebhookEventDTO event : payload.getEvents()) {
            System.out.println("Processando evento: " + event.getEventType());
            System.out.println("ID do objeto: " + event.getObjectId());
            
            switch (event.getEventType()) {
                case "contact.creation":
                    handleContactCreation(event);
                    break;
                case "contact.propertyChange":
                    handleContactPropertyChange(event);
                    break;
                case "contact.deletion":
                    handleContactDeletion(event);
                    break;
                default:
                    System.out.println("Tipo de evento não suportado: " + event.getEventType());
            }
        }
    }
    
    private void handleContactCreation(WebhookPayloadDTO.WebhookEventDTO event) {
        System.out.println("=== Processando Criação de Contato ===");
        try {
            Map<String, Object> contactDetails = fetchContactDetails(event.getObjectId());
            System.out.println("Detalhes do contato: " + contactDetails);
            
            // Aqui você pode adicionar lógica para salvar o contato no banco local
            // ou realizar outras ações necessárias
            
        } catch (Exception e) {
            System.out.println("Erro ao processar criação de contato: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleContactPropertyChange(WebhookPayloadDTO.WebhookEventDTO event) {
        System.out.println("=== Processando Mudança de Propriedade ===");
        System.out.println("Propriedade alterada: " + event.getPropertyName());
        System.out.println("Novo valor: " + event.getPropertyValue());
        
        try {
            Map<String, Object> contactDetails = fetchContactDetails(event.getObjectId());
            System.out.println("Detalhes atualizados do contato: " + contactDetails);
            
            // Aqui você pode adicionar lógica para atualizar o contato no banco local
            
        } catch (Exception e) {
            System.out.println("Erro ao processar mudança de propriedade: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleContactDeletion(WebhookPayloadDTO.WebhookEventDTO event) {
        System.out.println("=== Processando Deleção de Contato ===");
        System.out.println("ID do contato deletado: " + event.getObjectId());
        
        // Aqui você pode adicionar lógica para remover o contato do banco local
    }
    
    private Map<String, Object> fetchContactDetails(String contactId) {
        try {
            HubSpotToken token = tokenRepository.findFirstByOrderByCreatedAtDesc();
            if (token == null) {
                throw new RuntimeException("Token não encontrado");
            }
            
            String url = "https://api.hubapi.com/crm/v3/objects/contacts/" + contactId;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + token.getAccessToken().trim());
            headers.set("Accept", "application/json");
            
            HttpEntity<String> request = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            System.out.println("Resposta da API HubSpot: " + response.getBody());
            
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getBody(), Map.class);
            
        } catch (Exception e) {
            System.out.println("Erro ao buscar detalhes do contato: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar detalhes do contato", e);
        }
    }
} 