package org.sonya.sample.service;

import java.util.List;

import org.sonya.service.GenericManager;
import org.sonya.sample.model.Person;

public interface PersonManager extends GenericManager<Person, Long> {
    public List<Person> findByLastName(String lastName);
}