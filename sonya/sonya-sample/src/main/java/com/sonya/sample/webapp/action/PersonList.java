package com.sonya.sample.webapp.action;

import java.io.Serializable;
import java.util.List;

import com.sonya.service.GenericManager;
import com.sonya.webapp.action.BaseBean;

public class PersonList extends BaseBean implements Serializable {
    private GenericManager personManager;

    public void setPersonManager(GenericManager manager) {
        this.personManager = manager;
    }

    public PersonList() {
        setSortColumn("id"); // sets the default sort column
    }

    public List getPersons() {
        return sort(personManager.getAll());
    }
}
