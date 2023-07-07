package com.example.PhoneContacts.dto;


import lombok.Data;

@Data
public class AuthenticationRequestDto {

    private String login;
    private String password;
}
