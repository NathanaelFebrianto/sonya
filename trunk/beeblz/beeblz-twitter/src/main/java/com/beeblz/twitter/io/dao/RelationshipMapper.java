/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.dao;

import com.beeblz.twitter.io.GenericMapper;
import com.beeblz.twitter.io.model.Relationship;

/**
 * A interface for relationship mapper.
 * 
 * @author Young-Gue Bae
 */
public interface RelationshipMapper extends GenericMapper {

    /**
     * Inserts a relationship.
     *
     * @param relationship the relationship
     */
	public void insertRelationship(Relationship relation);

}