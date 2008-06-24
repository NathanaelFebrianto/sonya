package com.sonya.user.model;

import java.io.Serializable;

import javax.persistence.Transient;

import org.acegisecurity.GrantedAuthority;

import com.sonya.model.BaseObject;

/**
 * This class represents role object
 * 
 * @author YoungGue Bae
 */
public class Role extends BaseObject implements Serializable, GrantedAuthority {
	private static final long serialVersionUID = -4260559184726325943L;
	private String sid;
	private String id;
	private String name;
	private String description;
	
	public String getSid() {
		return sid;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
    /**
     * @see org.acegisecurity.GrantedAuthority#getAuthority()
     * @return the name property (getAuthority required by Acegi's GrantedAuthority interface)
     */
    @Transient
    public String getAuthority() {
        return getName();
    }

	public void setSid(String sid) {
		this.sid = sid;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
}
