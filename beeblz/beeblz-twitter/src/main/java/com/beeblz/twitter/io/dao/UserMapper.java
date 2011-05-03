/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.dao;

import java.util.List;

import com.beeblz.twitter.io.GenericMapper;
import com.beeblz.twitter.io.model.User;

/**
 * A interface for user mapper.
 * 
 * @author Young-Gue Bae
 */
public interface UserMapper extends GenericMapper {

	public List<User> selectUsers(User user);
	
	public void insertUser(User user);
	
	public void updateUser(User user);

}
