package com.sonya.sample.dao;

import java.util.List;

import com.sonya.dao.GenericDao;
import com.sonya.sample.model.Person;

public interface PersonDao extends GenericDao<Person, Long> {
    public List<Person> findByLastName(String lastName);
}