package com.sonya.sample.service;

import java.util.List;

import com.sonya.sample.model.Person;
import com.sonya.service.GenericManager;

public interface PersonManager extends GenericManager<Person, Long> {
    public List<Person> findByLastName(String lastName);
}