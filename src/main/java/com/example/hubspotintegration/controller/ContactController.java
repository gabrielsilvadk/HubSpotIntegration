package com.example.hubspotintegration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.hubspotintegration.service.ContactService;
import com.example.hubspotintegration.model.Contact;
import com.example.hubspotintegration.dto.ContactDTO;
import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    
    private final ContactService contactService;
    
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }
    
    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody ContactDTO contactDTO) {
        return ResponseEntity.ok(contactService.createContact(contactDTO));
    }
    
    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts() {
        return ResponseEntity.ok(contactService.getAllContacts());
    }
    
    @GetMapping("/{email}")
    public ResponseEntity<Contact> getContactByEmail(@PathVariable String email) {
        return ResponseEntity.ok(contactService.getContactByEmail(email));
    }
    
    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody String payload) {
        // TODO: Implementar o processamento do webhook
        return ResponseEntity.ok().build();
    }
}