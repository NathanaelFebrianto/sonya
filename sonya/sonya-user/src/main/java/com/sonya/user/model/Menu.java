package com.sonya.user.model;

import java.io.Serializable;

import com.sonya.model.BaseObject;

/**
 * This class represents menu object
 * 
 * @author YoungGue Bae
 */
public class Menu extends BaseObject implements Serializable {
	private static final long serialVersionUID = -5098863812599029214L;
	
	private String sid;
	private String id;
	private String name;
	private String parentMenuId;
	private String depth;

	public String getSid() {
		return sid;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getParentMenuId() {
		return parentMenuId;
	}

	public String getDepth() {
		return depth;
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

	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	public void setDepth(String depth) {
		this.depth = depth;
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
