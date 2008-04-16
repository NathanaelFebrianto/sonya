package org.sonya.sample.dao;

import java.util.List;

import org.sonya.dao.GenericDao;
import org.sonya.sample.model.Person;

public interface PersonDao extends GenericDao<Person, Long> {
    public List<Person> findByLastName(String lastName);
}