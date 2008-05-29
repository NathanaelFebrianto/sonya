package com.sonya.user.model;

import java.io.Serializable;

import com.sonya.model.BaseObject;

/**
 * This class represents role object
 * 
 * @author YoungGue Bae
 */
public class Role extends BaseObject implements Serializable {
	private static final long serialVersionUID = -4260559184726325943L;
	private String sid;
	private String id;
	private String name;
	
	public String getSid() {
		return sid;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
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
