/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.service;

import com.beeblz.twitter.io.GenericManager;
import com.beeblz.twitter.io.model.User;

/**
 * A interface for user manager.
 * 
 * @author Young-Gue Bae
 */
public interface UserManager extends GenericManager {

     /**
     * Adds a user.
     *
     * @param user the user
     */
	public void addUser(User user);
	
}
