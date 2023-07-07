package com.example.PhoneContacts.dto;


import com.example.PhoneContacts.model.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class ContactRequestDto {




    private  String name;



    private Set<String> emails = new LinkedHashSet<>();

    private Set<String> phoneNumbers = new LinkedHashSet<>();

}
