package com.example.PhoneContacts.service;

import com.example.PhoneContacts.dto.ContactRequestDto;
import com.example.PhoneContacts.model.Contact;
import com.example.PhoneContacts.model.Email;
import com.example.PhoneContacts.model.PhoneNumber;
import com.example.PhoneContacts.model.User;
import com.example.PhoneContacts.repository.ContactRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PhoneNumberService phoneNumberService;

    @InjectMocks
    private ContactService contactService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteContactByID() {
        // Arrange
        Contact contact = new Contact();
        User user = new User();
        contact.setUser(user);

        contactService.deleteContactByID(contact, user);

        assertNull(contact.getUser());
        verify(contactRepository, times(1)).deleteById(contact.getId());
    }

    @Test
    public void testChangeContactName_ExistingName() {
        Long contactId = 1L;
        String newName = "John";
        User user = new User();
        Contact contact1 = new Contact();
        contact1.setId(contactId);
        contact1.setName("John");
        Contact contact2 = new Contact();
        contact2.setName(newName);
        user.setContacts(new LinkedHashSet<>(Arrays.asList(contact1, contact2)));

        when(contactRepository.findById(contactId)).thenReturn(Optional.of(contact1));

        ResponseEntity<?> responseEntity = contactService.changeContactName(contactId, user, newName);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        Assertions.assertEquals("Contact with this name already exists", responseEntity.getBody());
        Assertions.assertEquals("John", contact1.getName());
        verify(contactRepository, never()).save(contact1);
    }

    @Test
    public void testChangeContactName_NonExistingContact() {
        Long contactId = 1L;
        String newName = "John Doe";
        User user = new User();
        Contact contact = new Contact();
        contact.setId(contactId);
        contact.setName("John");
        user.setContacts(Collections.singleton(contact));

        when(contactRepository.findById(contactId)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = contactService.changeContactName(contactId, user, newName);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        Assertions.assertEquals("Contact does not exists", responseEntity.getBody());
        Assertions.assertEquals("John", contact.getName());
        verify(contactRepository, never()).save(contact);
    }

    @Test
    public void testChangeContactName_Success() {
        Long contactId = 1L;
        String newName = "John Doe";
        User user = new User();
        Contact contact = new Contact();
        contact.setId(contactId);
        contact.setName("John");
        user.setContacts(Collections.singleton(contact));

        when(contactRepository.findById(contactId)).thenReturn(Optional.of(contact));

        ResponseEntity<?> responseEntity = contactService.changeContactName(contactId, user, newName);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals("Name is changed to " + newName, responseEntity.getBody());
        Assertions.assertEquals(newName, contact.getName());
        verify(contactRepository, times(1)).save(contact);
    }

}