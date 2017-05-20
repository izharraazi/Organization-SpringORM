package com.lab2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "PERSON")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private long id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email")
    private String email;

    @Column(name = "description")
    private String description;

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "org_id")
    private Organization org;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "FRIENDSHIP",
            joinColumns = {@JoinColumn(name = "person1_id", referencedColumnName = "person_id")},
            inverseJoinColumns = {@JoinColumn(name = "person2_id", referencedColumnName = "person_id")})
    private List<Person> friends;


    public Person() {
        this.friends = new ArrayList<>();
    }

    public Person(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.friends = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public Address getAddress() {
        return address;
    }

    @JsonProperty(value = "address")
    public String getAddressString() {
        if (address != null) {
            return address.getStreet() + ", " + address.getCity() + ", " + address.getState() + ", " + address.getZip();
        } else {
            return null;
        }
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Organization getOrganization() {
        return org;
    }

    public void setOrganization(Organization org) {
        this.org = org;
    }

    @JsonIgnore
    public List<Person> getFriends() {
        return friends;
    }

    @JsonProperty(value = "friends")
    public String getFriendsString() {
        if (!friends.isEmpty()) {
            String str = "";
            for (Person friend : friends) {
                str = str + friend.getFirstname() + " " + friend.getLastname() + ", ";
            }
            str = str.substring(0, str.length() - 2);
            return str;
        } else {
            return null;
        }
    }

    public void setFriends(List<Person> friends) {
        this.friends = friends;
    }

}

