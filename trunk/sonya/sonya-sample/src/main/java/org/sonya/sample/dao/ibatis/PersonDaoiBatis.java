package org.sonya.sample.dao.ibatis;

import java.util.List;

import org.sonya.dao.ibatis.GenericDaoiBatis;
import org.sonya.sample.dao.PersonDao;
import org.sonya.sample.model.Person;

public class PersonDaoiBatis extends GenericDaoiBatis<Person, Long> implements PersonDao {

    public PersonDaoiBatis() {
        super(Person.class);
    }

    @SuppressWarnings("unchecked") 
    public List<Person> findByLastName(String lastName) {
        return  (List<Person>) getSqlMapClientTemplate().queryForList("findByLastName", lastName);
    }
}