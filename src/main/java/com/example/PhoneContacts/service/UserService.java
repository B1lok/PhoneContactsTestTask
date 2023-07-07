package com.example.PhoneContacts.service;


import com.example.PhoneContacts.model.User;
import com.example.PhoneContacts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public Optional<User> findUserByLogin(String login){
        return userRepository.findByLogin(login);
    }

    public User createUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public String findById(Long id){
        return userRepository.findById(id).get().getLogin();
    }

}
