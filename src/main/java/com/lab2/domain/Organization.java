package com.lab2.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "ORGANIZATION")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "org")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Person> persons;

    public Organization() {
        this.persons = new ArrayList<>();
    }

    public Organization(String name) {
        this.name = name;
        this.persons = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @JsonIgnore
    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
