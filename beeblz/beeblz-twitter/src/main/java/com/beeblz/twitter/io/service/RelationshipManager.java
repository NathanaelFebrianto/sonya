/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.service;

import com.beeblz.twitter.io.GenericManager;
import com.beeblz.twitter.io.model.Relationship;

/**
 * A interface for relationship manager.
 * 
 * @author Young-Gue Bae
 */
public interface RelationshipManager extends GenericManager {

     /**
     * Adds a relationship.
     *
     * @param relationship the relationship
     */
	public void addRelationship(Relationship relationship);
	
}
