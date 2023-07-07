package com.example.PhoneContacts.service;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


import com.example.PhoneContacts.dto.ContactRequestDto;
import com.example.PhoneContacts.model.Contact;
import com.example.PhoneContacts.model.Email;
import com.example.PhoneContacts.model.PhoneNumber;
import com.example.PhoneContacts.model.User;
import com.example.PhoneContacts.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
public class ContactService {


    private final ContactRepository contactRepository;

    private final EmailService emailService;


    private final PhoneNumberService phoneNumberService;



    public void deleteContactByID(Contact contact, User user){
        contact.setUser(null);
        contact.getEmails().forEach(email -> email.setContact(null));
        contact.getPhoneNumbers().forEach(phoneNumber -> phoneNumber.setContact(null));
        user.getContacts().remove(contact);
        contactRepository.deleteById(contact.getId());
    }

    private boolean isEmailValid(String email){
        Pattern pattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPhoneNumberValid(String phoneNumber){
        Pattern pattern = Pattern.compile("\\+\\d{12}");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public ResponseEntity<?> changeContactName(Long contactId, User user, String newName){
        boolean contactExist = false;

        for (Contact contact : user.getContacts()) {
            if (contact.getName().equals(newName)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Contact with this name already exists");
            }
            if (contact.getId() == contactId) contactExist = true;
        }
        if (!contactExist) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Contact does not exists");

        Contact contact = contactRepository.findById(contactId).orElse(null);
        contact.setName(newName);
        contactRepository.save(contact);
        return ResponseEntity.ok("Name is changed to " + newName);
    }

    public ResponseEntity<?> addNewEmail(Long contactId,Long emailId, String newEmail, User user){
        Contact contact = contactRepository.findById(contactId).orElse(null);
        if (!user.getContacts().contains(contact)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Contact does not exists");

        boolean emailExist = false;

        for (Email email : contact.getEmails()) {
            if (email.getName().equals(newEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email with this name already exists");
            }
            if (emailId == email.getId()) emailExist = true;
        }
        if (!emailExist) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email does not exists");

        if (!isEmailValid(newEmail)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email");

        Email email = emailService.findEmailById(emailId).orElse(null);
        email.setName(newEmail);
        emailService.saveEmail(email);
        return ResponseEntity.ok("Name is changed to " + newEmail);

    }

    public ResponseEntity<?> addNewPhoneNumber(Long contactId, Long numberId, String newNumber, User user){

        Contact contact = contactRepository.findById(contactId).orElse(null);
        if (!user.getContacts().contains(contact)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Contact does not exists");

        boolean numberExist = false;

        for (PhoneNumber number : contact.getPhoneNumbers()) {
            if (number.getName().equals(newNumber)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Number already exists");
            }
            if (numberId == number.getId()) numberExist = true;
        }
        if (!numberExist) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Number does not exists");

        if (!isPhoneNumberValid(newNumber)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid number");

        PhoneNumber phoneNumber = phoneNumberService.findNumberById(numberId).orElse(null);
        phoneNumber.setName(newNumber);
        phoneNumberService.saveNumber(phoneNumber);
        return ResponseEntity.ok("Number is changed to " + newNumber);




    }

    public ResponseEntity<?> deleteEmailByID(Long contactId, Long emailId, User user){
        Contact contact = contactRepository.findById(contactId).orElse(null);
        if (!user.getContacts().contains(contact)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Contact does not exists");
        Email email = emailService.findEmailById(emailId).orElse(null);
        if (!contact.getEmails().contains(email)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email does not exists");

        contact.getEmails().remove(email);
        emailService.deleteEmailByID(emailId);
        contactRepository.save(contact);
        return ResponseEntity.ok("Email " + email.getName() + " is deleted");
    }

    public ResponseEntity<?> deletePhoneNumberByID(Long contactId, Long numberId, User user){
        Contact contact = contactRepository.findById(contactId).orElse(null);
        if (!user.getContacts().contains(contact)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Contact does not exists");
        PhoneNumber phoneNumber = phoneNumberService.findNumberById(numberId).orElse(null);
        if (!contact.getPhoneNumbers().contains(phoneNumber)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Number does not exists");

        contact.getPhoneNumbers().remove(phoneNumber);
        phoneNumberService.deleteNumberById(numberId);
        contactRepository.save(contact);
        return ResponseEntity.ok("Number " + phoneNumber.getName() + " is deleted");
    }


    public ResponseEntity<?> saveNewContact(ContactRequestDto contactRequestDto,User user){

        for (String email : contactRequestDto.getEmails()) {
            if (!isEmailValid(email)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email");
        }
        for (String phoneNumber : contactRequestDto.getPhoneNumbers()){
            if (!isPhoneNumberValid(phoneNumber))  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid phoneNumber");
        }

        for (Contact contact : user.getContacts()) {
            if (contact.getName().equals(contactRequestDto.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Contact with this name already exists");
            }
        }


        Contact contact = new Contact();
        contact.setUser(user);
        contact.setName(contactRequestDto.getName());
        contactRequestDto.getEmails().forEach(email -> contact.addEmail(new Email(email, contact)));
        contactRequestDto.getPhoneNumbers().forEach(number -> contact.addPhoneNumber(new PhoneNumber(number, contact)));
        return ResponseEntity.ok(contactRepository.save(contact));
    }

}
