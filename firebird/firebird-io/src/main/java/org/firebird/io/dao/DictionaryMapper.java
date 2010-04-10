/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.dao;

import java.util.List;

import org.firebird.common.ibatis.GenericMapper;
import org.firebird.io.model.Dictionary;
import org.firebird.io.model.Edge;

/**
 * A interface for dictionary mapper.
 * 
 * @author Young-Gue Bae
 */
public interface DictionaryMapper extends GenericMapper {

    /**
     * Selects a dictionary term.
     *
     * @param dictionary the dictionary
     * @return Dictionary the dictionary term
     */
	public Dictionary selectTerm(Dictionary dict);
	
	/**
     * Inserts a dictionary term.
     *
     * @param dictionary the dictionary term
     */
	public void insertTerm(Dictionary dictTerm);
	
	/**
     * Deletes dictionary.
     *
     * @param websiteId the website id
     */
	public void deleteDictionary(int websiteId);
}
