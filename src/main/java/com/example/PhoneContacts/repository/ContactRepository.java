package com.example.PhoneContacts.repository;

import com.example.PhoneContacts.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

     void deleteById(Long id);


}
