package com.sonya.user.model;

import java.io.Serializable;

import com.sonya.model.BaseObject;

/**
 *This class is used to represent available role resources.
 *  
 * @author YoungGue Bae
 */
public class RoleResource extends BaseObject implements Serializable {
	private static final long serialVersionUID = 1600068515732799430L;
	
	private String sid;
	private String roleId;
	private String menuId;
	private String authority;
	private boolean downloadable;
	private boolean printable;
	
	public String getSid() {
		return sid;
	}

	public String getRoleId() {
		return roleId;
	}

	public String getMenuId() {
		return menuId;
	}

	public String getAuthority() {
		return authority;
	}

	public boolean isDownloadable() {
		return downloadable;
	}

	public boolean isPrintable() {
		return printable;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public void setDownloadable(boolean downloadable) {
		this.downloadable = downloadable;
	}

	public void setPrintable(boolean printable) {
		this.printable = printable;
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

}
