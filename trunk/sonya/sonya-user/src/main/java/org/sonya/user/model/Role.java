/*
 * Copyright Sonya, 2008. All Rights Reserved.
 */

package org.sonya.user.model;

import java.io.Serializable;

import org.sonya.model.BaseObject;

/**
 * This class represents role object
 * 
 * @author YoungGue Bae(Louie)
 */
public class Role extends BaseObject implements Serializable {
	private static final long serialVersionUID = -4260559184726325943L;
	private String sid;
	private String roleId;
	private String roleName;
	
	public String getSid() {
		return sid;
	}

	public String getRoleId() {
		return roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
