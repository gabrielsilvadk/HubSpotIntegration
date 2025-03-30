package com.example.hubspotintegration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.hubspotintegration.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    Contact findByEmail(String email);
}
