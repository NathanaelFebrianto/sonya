/*
Copyright (c) 2009-2010, Young-Gue Bae
All rights reserved.
*/
package org.firebird.service.impl;

import org.apache.log4j.Logger;
import org.firebird.dao.GenericDao;
import org.firebird.service.GenericManager;

/**
 * A implementation for generic manager.
 * 
 * @author Young-Gue Bae
 */
public class GenericManagerImpl implements GenericManager {
    
	protected static Logger logger = Logger.getLogger(GenericManagerImpl.class.getName());
	
	/**
     * GenericDao instance, set by constructor of this class.
     */
    protected GenericDao genericDao;

    /**
     * Public constructor for creating a new GenericManagerImpl.
     * 
     * @param genericDao the GenericDao to use for persistence
     */
    public GenericManagerImpl(GenericDao genericDao) {
        this.genericDao = genericDao;
    }
}
