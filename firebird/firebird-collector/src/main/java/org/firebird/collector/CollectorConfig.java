/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.collector;

import java.io.Serializable;

/**
 * Configuration for collector.
 * 
 * @author Young-Gue Bae
 */
public class CollectorConfig implements Serializable {

	private static final long serialVersionUID = 3400641227817931444L;
	private boolean dbStorage = false;
	private boolean collectFriend = true;
	private boolean collectFollower = false;
	private boolean collectUserBlogEntry = false;
	private int levelLimit = 2;
	private int peopleLimit = 50;
	private int degreeLimit = 10;
	
	public boolean isDBStorage() {
		return dbStorage;
	}
	public void setDBStorage(boolean dbStorage) {
		this.dbStorage = dbStorage;
	}
	public boolean isCollectFriend() {
		return collectFriend;
	}
	public void setCollectFriend(boolean collectFriend) {
		this.collectFriend = collectFriend;
	}
	public boolean isCollectFollower() {
		return collectFollower;
	}
	public void setCollectFollower(boolean collectFollower) {
		this.collectFollower = collectFollower;
	}
	public boolean isCollectUserBlogEntry() {
		return collectUserBlogEntry;
	}
	public void setCollectUserBlogEntry(boolean collectUserBlogEntry) {
		this.collectUserBlogEntry = collectUserBlogEntry;
	}
	public int getLevelLimit() {
		return levelLimit;
	}
	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}
	public int getPeopleLimit() {
		return peopleLimit;
	}
	public void setPeopleLimit(int peopleLimit) {
		this.peopleLimit = peopleLimit;
	}
	public int getDegreeLimit() {
		return degreeLimit;
	}
	public void setDegreeLimit(int degreeLimit) {
		this.degreeLimit = degreeLimit;
	}
}
