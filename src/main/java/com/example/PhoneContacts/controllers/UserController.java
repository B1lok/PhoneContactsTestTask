package com.example.PhoneContacts.controllers;


import com.example.PhoneContacts.dto.ContactRequestDto;
import com.example.PhoneContacts.model.Contact;
import com.example.PhoneContacts.model.User;
import com.example.PhoneContacts.service.ContactService;
import com.example.PhoneContacts.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/myPage")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;


    private final ContactService contactService;



    @GetMapping("/myContacts")
    public ResponseEntity<Set<Contact>> getAllUserContacts(Authentication authentication){
        User user = userService.findUserByLogin(authentication.getName()).orElse(null);
        return ResponseEntity.ok(user.getContacts());
    }


    @PutMapping("/changeContactName/{contactId}/{newName}")
    public ResponseEntity<?> changeContactName(@PathVariable Long contactId, @PathVariable String newName, Authentication authentication){
        User user = userService.findUserByLogin(authentication.getName()).orElse(null);
        return contactService.changeContactName(contactId, user, newName);
    }


    @PutMapping("/addEmail/{contactId}/{emailId}/{newEmail}")
    public ResponseEntity<?> addNewEmail(@PathVariable Long contactId, @PathVariable Long emailId ,@PathVariable String newEmail, Authentication authentication){
        User user = userService.findUserByLogin(authentication.getName()).orElse(null);
        return contactService.addNewEmail(contactId, emailId,newEmail, user);
    }


    @PutMapping("/addPhoneNumber/{contactId}/{numberId}/{newNumber}")
    public ResponseEntity<?> addNewNumber(@PathVariable Long contactId, @PathVariable Long numberId ,@PathVariable String newNumber, Authentication authentication){
        User user = userService.findUserByLogin(authentication.getName()).orElse(null);
        return contactService.addNewPhoneNumber(contactId, numberId, newNumber, user);
    }


    @DeleteMapping("/deleteEmail/{contactId}/{emailId}")
    public ResponseEntity<?> deleteEmail(@PathVariable Long contactId, @PathVariable Long emailId, Authentication authentication){
        User user = userService.findUserByLogin(authentication.getName()).orElse(null);
        return contactService.deleteEmailByID(contactId, emailId, user);
    }


    @DeleteMapping("/deleteNumber/{contactId}/{numberId}")
    public ResponseEntity<?> deletePhoneNumber(@PathVariable Long contactId, @PathVariable Long numberId, Authentication authentication){
        User user = userService.findUserByLogin(authentication.getName()).orElse(null);
        return contactService.deletePhoneNumberByID(contactId,numberId,user);
    }




    @PostMapping("/addContact")
    public ResponseEntity<?> addContact(@RequestBody ContactRequestDto contact, Authentication authentication){

        User user = userService.findUserByLogin(authentication.getName()).orElse(null);

        return contactService.saveNewContact(contact, user);
    }


    @DeleteMapping("/deleteContact/{contactName}")
    @Transactional
    public ResponseEntity<?> deleteContactById(@PathVariable String contactName, Authentication authentication){
        User user = userService.findUserByLogin(authentication.getName()).orElse(null);
        for (Contact contact : user.getContacts()){
            if (contactName.equals(contact.getName())) {
                contactService.deleteContactByID(contact, user);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }



}
