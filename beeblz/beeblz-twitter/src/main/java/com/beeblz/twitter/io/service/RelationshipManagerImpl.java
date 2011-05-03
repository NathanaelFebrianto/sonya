/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.service;

import java.util.List;

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

    public RelationshipManagerImpl() { }
	
	public List<Relationship> getRelationships(Relationship relationship) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		RelationshipMapper mapper = session.getMapper(RelationshipMapper.class);
    		List<Relationship> result = mapper.selectRelationships(relationship);
    		return result;
    	} finally {
    		session.close();
    	}
	}
    
	public void addRelationship(Relationship relationship) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		RelationshipMapper mapper = session.getMapper(RelationshipMapper.class);
    		mapper.insertRelationship(relationship);
    		session.commit();
    	} finally {
    		session.close();
    	}
	}
	
	public void setRelationship(Relationship relationship) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		RelationshipMapper mapper = session.getMapper(RelationshipMapper.class);
    		mapper.updateRelationship(relationship);
    		session.commit();
    	} finally {
    		session.close();
    	}		
	}
}
