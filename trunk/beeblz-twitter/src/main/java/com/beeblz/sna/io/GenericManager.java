/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.sna.io;


/**
 * A interface for generic manager.
 * 
 * @author Young-Gue Bae
 */
public interface GenericManager {

	/**
	 * Sets the data source.
	 * 
	 * @param mapper the dao mapper
	 */
	public void setDataSource(GenericMapper mapper);
}
