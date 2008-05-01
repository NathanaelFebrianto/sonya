package org.sonya.user.model;

import java.io.Serializable;

import org.sonya.model.BaseObject;

/**
 * This class represents the basic "user" object that allows for authentication.
 *
 * @author YoungGue Bae (Louie)
 */
public class User extends BaseObject implements Serializable {
	private static final long serialVersionUID = 619543912512248365L;
	private String id;
	private String sid;
	private String username;
	private String password;
	private String roleId;
	private String firstName;
	private String lastName;	
	private String passwordHintQuestion;
	private String passwordHintAnswer;
	private String company;
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
	
	public String getId() {
		return id;
	}

	public String getSid() {
		return sid;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getRoleId() {
		return roleId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPasswordHintQuestion() {
		return passwordHintQuestion;
	}

	public String getPasswordHintAnswer() {
		return passwordHintAnswer;
	}

	public String getCompany() {
		return company;
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

	public void setId(String id) {
		this.id = id;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPasswordHintQuestion(String passwordHintQuestion) {
		this.passwordHintQuestion = passwordHintQuestion;
	}

	public void setPasswordHintAnswer(String passwordHintAnswer) {
		this.passwordHintAnswer = passwordHintAnswer;
	}

	public void setCompany(String company) {
		this.company = company;
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
