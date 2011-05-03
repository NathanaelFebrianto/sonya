/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.dao;

import java.util.List;

import com.beeblz.twitter.io.GenericMapper;
import com.beeblz.twitter.io.model.Relationship;

/**
 * A interface for relationship mapper.
 * 
 * @author Young-Gue Bae
 */
public interface RelationshipMapper extends GenericMapper {

	public List<Relationship> selectRelationships(Relationship relationship);
	
	public void insertRelationship(Relationship relationship);
	
	public void updateRelationship(Relationship relationship);

}