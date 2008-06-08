package com.sonya.user.model;

import java.io.Serializable;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;

import com.sonya.model.BaseObject;

/**
 * This class represents the basic "user" object that allows for authentication.
 *
 * @author YoungGue Bae
 */
public class User extends BaseObject implements Serializable, UserDetails {
	private static final long serialVersionUID = 619543912512248365L;
	
	private String id;
	private String sid;
	private String username;
	private String password;
	private String confirmPassword;
	private String roleId;
	private String firstName;
	private String lastName;	
	private String country;
	private String timeZone;
	private String company;
	private String department;
	private String jobPosition;
	private String postalCode;
	private String address;
	private String addressDetail;
	private String email;
	private String phoneNumber;
	private String mobileNumber;
	private String website;
	private boolean use;
	private boolean terminate;
	private String terminateDate;
	private String insertDate;
	private String updateDate;
	private String deleteDate;
	
	private boolean enabled;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean credentialsExpired;
	
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
	
	public String getConfirmPassword() {
        return confirmPassword;
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

	public String getCountry() {
		return country;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public String getCompany() {
		return company;
	}

	public String getDepartment() {
		return department;
	}

	public String getJobPosition() {
		return jobPosition;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
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

    /**
     * @see org.acegisecurity.userdetails.UserDetails#getAuthorities()
     * @return GrantedAuthority[] an array of roles.
     */
    public GrantedAuthority[] getAuthorities() {
        //return roles.toArray(new GrantedAuthority[0]);
        return null;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }
    
    /**
     * @see org.acegisecurity.userdetails.UserDetails#isAccountNonExpired()
     */
    public boolean isAccountNonExpired() {
        return !isAccountExpired();
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }
    
    /**
     * @see org.acegisecurity.userdetails.UserDetails#isAccountNonLocked()
     */
    public boolean isAccountNonLocked() {
        return !isAccountLocked();
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }
    
    /**
     * @see org.acegisecurity.userdetails.UserDetails#isCredentialsNonExpired()
     */
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
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
	
	public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

	public void setCountry(String country) {
		this.country = country;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
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

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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
	
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }
    
    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
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
