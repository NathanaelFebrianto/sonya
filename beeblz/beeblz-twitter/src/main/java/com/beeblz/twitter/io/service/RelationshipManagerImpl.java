/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.service;

import org.apache.ibatis.session.SqlSession;

import com.beeblz.twitter.io.GenericManagerImpl;
import com.beeblz.twitter.io.dao.RelationshipMapper;
import com.beeblz.twitter.io.model.Relationship;

/**
 * A implementation for relationship manager.
 * 
 * @author Young-Gue Bae
 */
public class RelationshipManagerImpl extends GenericManagerImpl implements RelationshipManager {

	//private RelationshipMapper mapper;
	
	/**
     * Constructor.
     *
     */
    public RelationshipManagerImpl() {
    }
	
	/**
     * Constructor.
     *
     * @param mapper the relationship mapper
     */
    public RelationshipManagerImpl(RelationshipMapper mapper) {
        //this.mapper = mapper;
    }
    
    /**
     * Adds a relationship.
     *
     * @param relationship the relationship
     */
	public void addRelationship(Relationship relationship) {
		//mapper.insertUser(user);

		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		RelationshipMapper mapper = session.getMapper(RelationshipMapper.class);
    		mapper.insertRelationship(relationship);
    		session.commit();
    	} finally {
    		session.close();
    	}
	}
	
}
