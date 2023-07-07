package com.example.PhoneContacts.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "email", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "contact_id"}))
@NoArgsConstructor
public class Email {

    public Email(String name, Contact contact){
        this.name = name;
        this.contact = contact;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;



    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

}