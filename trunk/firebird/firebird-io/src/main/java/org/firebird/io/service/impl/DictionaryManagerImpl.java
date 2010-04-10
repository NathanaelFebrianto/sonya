/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service.impl;

import org.apache.ibatis.session.SqlSession;
import org.firebird.common.service.GenericManagerImpl;
import org.firebird.io.dao.DictionaryMapper;
import org.firebird.io.model.Dictionary;
import org.firebird.io.service.DictionaryManager;

/**
 * A implementation for dictionary manager.
 * 
 * @author Young-Gue Bae
 */
public class DictionaryManagerImpl extends GenericManagerImpl implements DictionaryManager {

	/**
     * Constructor.
     *
     */
    public DictionaryManagerImpl() {
    }

    /**
     * Gets the dictionary term.
     *
     * @param websiteId the website id
     * @param term the term
     * @return Dictionary the dictionary term
     */
	public Dictionary getTerm(int websiteId, String term) {
		Dictionary param = new Dictionary();
		param.setWebsiteId(websiteId);
		param.setTerm(term);		
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		DictionaryMapper mapper = session.getMapper(DictionaryMapper.class);
    		Dictionary dictTerm = mapper.selectTerm(param);
    		return dictTerm;
    	} finally {
    		session.close();
    	}
	}
	
	/**
     * Adds a dictionary term.
     *
     * @param dictionary the dictionary term
     */
	public void addTerm(Dictionary dictTerm) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		DictionaryMapper mapper = session.getMapper(DictionaryMapper.class);
    		mapper.insertTerm(dictTerm);
    		session.commit();
    	} finally {
    		session.close();
    	}		
	}
	
	/**
     * Deletes dictionary.
     *
     * @param websiteId the website id
     */
	public void deleteDictionary(int websiteId) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		DictionaryMapper mapper = session.getMapper(DictionaryMapper.class);
    		mapper.deleteDictionary(websiteId);
    		session.commit();
    	} finally {
    		session.close();
    	}
	}
    
}
