package com.sonya.sample.model;

import com.sonya.model.BaseObject;

public class Person extends BaseObject {

    private Long id;
    private String firstName;
    private String lastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String toString() {
        return "";
    }

    public boolean equals(Object o) {
        return false;
    }

    public int hashCode() {
        return 1;
    }
}