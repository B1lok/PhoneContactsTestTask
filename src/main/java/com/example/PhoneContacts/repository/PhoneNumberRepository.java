package com.example.PhoneContacts.repository;

import com.example.PhoneContacts.model.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
}
