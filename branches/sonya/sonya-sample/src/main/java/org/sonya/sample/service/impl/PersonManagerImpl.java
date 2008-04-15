package org.sonya.sample.service.impl;

import java.util.List;

import org.sonya.service.impl.GenericManagerImpl;
import org.sonya.sample.dao.PersonDao;
import org.sonya.sample.model.Person;
import org.sonya.sample.service.PersonManager;

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