/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.service;

import java.util.List;

import com.beeblz.twitter.io.GenericManager;
import com.beeblz.twitter.io.model.User;

/**
 * A interface for user manager.
 * 
 * @author Young-Gue Bae
 */
public interface UserManager extends GenericManager {

	public List<User> getUsers(User user);
	
	public void addUser(User user);
	
	public void setUser(User user);
	
}
