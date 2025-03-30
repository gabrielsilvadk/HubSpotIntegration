package com.example.hubspotintegration.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.hubspotintegration.model.Contact;
import com.example.hubspotintegration.repository.ContactRepository;
import com.example.hubspotintegration.repository.HubSpotTokenRepository;
import com.example.hubspotintegration.dto.ContactDTO;
import java.util.List;

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
        // Ps saving locally
        Contact contact = new Contact();
        contact.setEmail(contactDTO.getEmail());
        contact.setFirstName(contactDTO.getFirstName());
        contact.setLastName(contactDTO.getLastName());
        contact.setPhone(contactDTO.getPhone());
        
        contact = contactRepository.save(contact);
        
        String hubspotToken = tokenRepository.findFirstByOrderByCreatedAtDesc().getAccessToken();
        String url = "https://api.hubapi.com/crm/v3/objects/contacts";
        
        // TODO: Implementar a chamada para o HubSpot com rate limit
        
        return contact;
    }
    
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }
    
    public Contact getContactByEmail(String email) {
        return contactRepository.findByEmail(email);
    }
}