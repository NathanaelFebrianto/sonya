/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service;

import java.util.List;

import org.firebird.common.service.GenericManager;
import org.firebird.io.model.Dictionary;

/**
 * A interface for dictionary manager.
 * 
 * @author Young-Gue Bae
 */
public interface DictionaryManager extends GenericManager {

    /**
     * Gets the dictionary term.
     *
     * @param websiteId the website id
     * @param term the term
     * @return Dictionary the dictionary term
     */
	public Dictionary getTerm(int websiteId, String term);
	
	/**
     * Adds a dictionary term.
     *
     * @param dictionary the dictionary term
     */
	public void addTerm(Dictionary dictTerm);
	
	/**
     * Deletes dictionary.
     *
     * @param websiteId the website id
     */
	public void deleteDictionary(int websiteId);
	
}
