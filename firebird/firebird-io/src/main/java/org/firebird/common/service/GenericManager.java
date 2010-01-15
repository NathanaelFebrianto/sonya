/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.common.service;

import org.firebird.common.ibatis.GenericMapper;

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
