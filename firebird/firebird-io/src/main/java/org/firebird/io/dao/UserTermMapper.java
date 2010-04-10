/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.dao;

import java.util.List;

import org.firebird.common.ibatis.GenericMapper;
import org.firebird.io.model.UserTerm;

/**
 * A interface for user term mapper.
 * 
 * @author Young-Gue Bae
 */
public interface UserTermMapper extends GenericMapper {

    /**
     * Selects users.
     *
     * @param param the user term
     * @return List<UserTerm> the list of user term
     */
	public List<UserTerm> selectUsers(UserTerm param);
	
	/**
     * Selects user terms.
     *
     * @param param the user term
     * @return List<UserTerm> the list of user term
     */
	public List<UserTerm> selectTerms(UserTerm param);
	
	/**
     * Inserts a user term.
     *
     * @param userTerm the user term
     */
	public void insertTerm(UserTerm userTerm);
	
	/**
     * Deletes users.
     *
     * @param websiteId the website id
     */
	public void deleteUsers(int websiteId);
	
}
