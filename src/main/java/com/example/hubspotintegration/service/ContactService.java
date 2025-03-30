package com.example.hubspotintegration.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.hubspotintegration.model.Contact;
import com.example.hubspotintegration.model.HubSpotToken;
import com.example.hubspotintegration.repository.ContactRepository;
import com.example.hubspotintegration.repository.HubSpotTokenRepository;
import com.example.hubspotintegration.dto.ContactDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class ContactService {
    
    private final RestTemplate restTemplate;
    private final ContactRepository contactRepository;
    private final HubSpotTokenRepository tokenRepository;
    
    public ContactService(RestTemplate restTemplate, ContactRepository contactRepository, HubSpotTokenRepository tokenRepository) {
        this.restTemplate = restTemplate;
        this.contactRepository = contactRepository;
        this.tokenRepository = tokenRepository;
    }
    
    public Contact createContact(ContactDTO contactDTO) {
        try {
            Contact contact = new Contact();
            contact.setEmail(contactDTO.getEmail());
            contact.setFirstName(contactDTO.getFirstName());
            contact.setLastName(contactDTO.getLastName());
            contact.setPhone(contactDTO.getPhone());
            
            contact = contactRepository.save(contact);
            System.out.println("Contact saved locally with ID: " + contact.getId());
            
            HubSpotToken token = tokenRepository.findFirstByOrderByCreatedAtDesc();
            if (token == null) {
                throw new RuntimeException("Token not found. Please authenticate again.");
            }
            
            System.out.println("Token found: " + token.getAccessToken());
            
            String url = "https://api.hubapi.com/crm/v3/objects/contacts";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + token.getAccessToken().trim());
            headers.set("Accept", "application/json");
            
            Map<String, Object> properties = new HashMap<>();
            properties.put("email", contactDTO.getEmail());
            properties.put("firstname", contactDTO.getFirstName());
            properties.put("lastname", contactDTO.getLastName());
            properties.put("phone", contactDTO.getPhone());
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("properties", properties);
            
            System.out.println("URL: " + url);
            System.out.println("Request Body: " + requestBody);
            System.out.println("Headers: " + headers);
            System.out.println("Authorization Header: " + "Bearer " + token.getAccessToken().trim());
            System.out.println("Token length: " + token.getAccessToken().length());
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
                System.out.println("Response Status: " + response.getStatusCode());
                System.out.println("Response Body: " + response.getBody());
            } catch (HttpClientErrorException e) {
                System.out.println("HTTP Error: " + e.getStatusCode());
                System.out.println("Response Body: " + e.getResponseBodyAsString());
                System.out.println("Sent Headers: " + headers);
                System.out.println("Authorization Header: " + "Bearer " + token.getAccessToken().trim());
                throw e;
            } catch (Exception e) {
                System.out.println("HTTP Request Error: " + e.getMessage());
                if (e.getCause() != null) {
                    System.out.println("Cause: " + e.getCause().getMessage());
                }
                throw e;
            }
            
            return contact;
        } catch (Exception e) {
            System.out.println("Error creating contact: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error creating contact in HubSpot: " + e.getMessage());
        }
    }
    
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }
    
    public Contact getContactByEmail(String email) {
        return contactRepository.findByEmail(email);
    }
}