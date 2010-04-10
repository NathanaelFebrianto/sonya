/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service;

import java.util.List;

import org.firebird.common.service.GenericManager;
import org.firebird.io.model.UserTerm;

/**
 * A interface for user term manager.
 * 
 * @author Young-Gue Bae
 */
public interface UserTermManager extends GenericManager {

    /**
     * Gets the users.
     *
     * @param websiteId the website id
     * @param term the term
     * @return List<UserTerm> the list of user term
     */
	public List<UserTerm> getUsers(int websiteId, String term);
	
	/**
     * Gets the user terms.
     *
     * @param websiteId the website id
     * @param userId the user id
     * @return List<UserTerm> the list of user term
     */
	public List<UserTerm> getTerms(int websiteId, String userId);
	
	/**
     * Adds a user term.
     *
     * @param userTerm the user term
     */
	public void addTerm(UserTerm userTerm);
	
	/**
     * Deletes users.
     *
     * @param websiteId the website id
     */
	public void deleteUsers(int websiteId);
	
}
