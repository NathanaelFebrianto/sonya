package com.sonya.sample.service.impl;

import java.util.List;

import com.sonya.sample.dao.PersonDao;
import com.sonya.sample.model.Person;
import com.sonya.sample.service.PersonManager;
import com.sonya.service.impl.GenericManagerImpl;

public class PersonManagerImpl extends GenericManagerImpl<Person, Long> implements PersonManager {
    PersonDao personDao;

    public PersonManagerImpl(PersonDao personDao) {
        super(personDao);
        this.personDao = personDao;
    }

    public List<Person> findByLastName(String lastName) {
        return personDao.findByLastName(lastName);
    }
}