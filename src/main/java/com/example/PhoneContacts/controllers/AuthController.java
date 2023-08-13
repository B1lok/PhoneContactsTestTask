package com.example.PhoneContacts.controllers;


import com.example.PhoneContacts.dto.AuthenticationRequestDto;
import com.example.PhoneContacts.model.User;
import com.example.PhoneContacts.security.JwtProvider;
import com.example.PhoneContacts.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final UserService userService;

    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;


    private final PasswordEncoder passwordEncoder;



    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@RequestBody User user){

        if (userService.findUserByLogin(user.getLogin()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            return ResponseEntity.ok(userService.createUser(user));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto requestDto){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getLogin(), requestDto.getPassword()));
            User user = userService.findUserByLogin(requestDto.getLogin()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
            String token = jwtProvider.createToken(requestDto.getLogin(), requestDto.getPassword());
            Map<Object, Object> response = new HashMap<>();
            response.put("login", requestDto.getLogin());
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
        return ResponseEntity.ok("Logged out successfully");
    }



}
