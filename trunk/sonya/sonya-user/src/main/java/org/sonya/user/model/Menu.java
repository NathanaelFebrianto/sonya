package org.sonya.user.model;

import java.io.Serializable;

import org.sonya.model.BaseObject;

/**
 * This class represents menu object
 * 
 * @author YoungGue Bae (Louie)
 */
public class Menu extends BaseObject implements Serializable {
	private static final long serialVersionUID = -5098863812599029214L;
	private String sid;
	private String menuId;
	private String menuName;
	private String parentMenuId;
	private String depth;

	public String getSid() {
		return sid;
	}

	public String getMenuId() {
		return menuId;
	}

	public String getMenuName() {
		return menuName;
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

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
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
