/*
 * Copyright Sonya, 2008. All Rights Reserved.
 */

package org.sonya.user.model;

import java.io.Serializable;

import org.sonya.model.BaseObject;

/**
 * This class represents the basic "user" object that allows for authentication.
 *
 * @author YoungGue Bae(Louie)
 */
public class User extends BaseObject implements Serializable {
	private static final long serialVersionUID = 619543912512248365L;
	private String userId;
	private String sid;
	private String roleId;
	private String username;
	private String firstUsername;
	private String lastUsername;
	private String password;
	private String passwordHintQuestion;
	private String passwordHintAnswer;
	private String companyName;
	private String department;
	private String jobLevel;
	private String postalCode;
	private String address;
	private String addressDetail;
	private String email;
	private String phoneNo;
	private String mobileNo;
	private String website;
	private boolean use;
	private boolean terminate;
	private String terminateDate;
	private String insertDate;
	private String updateDate;
	private String deleteDate;	

	
	public String getUserId() {
		return userId;
	}
	public String getSid() {
		return sid;
	}
	public String getRoleId() {
		return roleId;
	}
	public String getUsername() {
		return username;
	}
	public String getFirstUsername() {
		return firstUsername;
	}
	public String getLastUsername() {
		return lastUsername;
	}
	public String getPassword() {
		return password;
	}
	public String getPasswordHintQuestion() {
		return passwordHintQuestion;
	}
	public String getPasswordHintAnswer() {
		return passwordHintAnswer;
	}
	public String getCompanyName() {
		return companyName;
	}
	public String getDepartment() {
		return department;
	}
	public String getJobLevel() {
		return jobLevel;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public String getAddress() {
		return address;
	}
	public String getAddressDetail() {
		return addressDetail;
	}
	public String getEmail() {
		return email;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public String getWebsite() {
		return website;
	}
	public boolean isUse() {
		return use;
	}
	public boolean isTerminate() {
		return terminate;
	}
	public String getTerminateDate() {
		return terminateDate;
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
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setFirstUsername(String firstUsername) {
		this.firstUsername = firstUsername;
	}
	public void setLastUsername(String lastUsername) {
		this.lastUsername = lastUsername;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setPasswordHintQuestion(String passwordHintQuestion) {
		this.passwordHintQuestion = passwordHintQuestion;
	}
	public void setPasswordHintAnswer(String passwordHintAnswer) {
		this.passwordHintAnswer = passwordHintAnswer;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public void setJobLevel(String jobLevel) {
		this.jobLevel = jobLevel;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public void setUse(boolean use) {
		this.use = use;
	}
	public void setTerminate(boolean terminate) {
		this.terminate = terminate;
	}
	public void setTerminateDate(String terminateDate) {
		this.terminateDate = terminateDate;
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
