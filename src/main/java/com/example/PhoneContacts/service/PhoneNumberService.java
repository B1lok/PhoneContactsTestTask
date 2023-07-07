package com.example.PhoneContacts.service;

import com.example.PhoneContacts.model.PhoneNumber;
import com.example.PhoneContacts.repository.PhoneNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhoneNumberService {

    private final PhoneNumberRepository phoneNumberRepository;


    public Optional<PhoneNumber> findNumberById(Long id){
        return phoneNumberRepository.findById(id);
    }


    public void saveNumber(PhoneNumber phoneNumber){
        phoneNumberRepository.save(phoneNumber);
    }

    public void deleteNumberById(Long id){
        phoneNumberRepository.deleteById(id);
    }
}
