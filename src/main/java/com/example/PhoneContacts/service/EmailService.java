package com.example.PhoneContacts.service;

import com.example.PhoneContacts.model.Email;
import com.example.PhoneContacts.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;


    public Optional<Email> findEmailById(Long id){
        return emailRepository.findById(id);
    }

    public void deleteEmailByID(Long id){
        emailRepository.deleteById(id);
    }

    public void saveEmail(Email email){
        emailRepository.save(email);
    }
}
