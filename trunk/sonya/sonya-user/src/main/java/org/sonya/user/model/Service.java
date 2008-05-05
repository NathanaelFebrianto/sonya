package org.sonya.user.model;

import java.io.Serializable;

import org.sonya.model.BaseObject;

/**
 * This class represents service object
 * 
 * @author YoungGue Bae (Louie)
 */
public class Service extends BaseObject implements Serializable {
	private static final long serialVersionUID = 7604119003041240515L;
	
	private String sid;
	private String serviceType;
	private String serviceName;
	private String adminUserId;
	private String password;
	private boolean use;
	private boolean expire;
	private boolean terminate;
	private String insertDate;
	private String updateDate;
	private String deleteDate;	
	
	public String getSid() {
		return sid;
	}

	public String getServiceType() {
		return serviceType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getAdminUserId() {
		return adminUserId;
	}

	public String getPassword() {
		return password;
	}

	public boolean isUse() {
		return use;
	}

	public boolean isExpire() {
		return expire;
	}

	public boolean isTerminate() {
		return terminate;
	}

	public String getInsertDate() {
		return insertDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public String getDeleteDate() {
		return deleteDate;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setAdminUserId(String adminUserId) {
		this.adminUserId = adminUserId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUse(boolean use) {
		this.use = use;
	}

	public void setExpire(boolean expire) {
		this.expire = expire;
	}

	public void setTerminate(boolean terminate) {
		this.terminate = terminate;
	}

	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public void setDeleteDate(String deleteDate) {
		this.deleteDate = deleteDate;
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
