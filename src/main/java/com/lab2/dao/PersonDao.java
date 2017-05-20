package com.lab2.dao;

import com.lab2.domain.Person;

import java.util.List;

public interface PersonDao {
    void store(Person person);

    void delete(long personId);

    Person findById(long personId);

    Person update(Person person);

    List<Person> findAll();
}
