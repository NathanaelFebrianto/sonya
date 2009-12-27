package org.firebird.service.impl;

import org.apache.log4j.Logger;
import org.firebird.dao.GenericDao;
import org.firebird.service.GenericManager;

public class GenericManagerImpl implements GenericManager {
    
	protected static Logger logger = Logger.getLogger(GenericManagerImpl.class.getName());
	
	/**
     * GenericDao instance, set by constructor of this class.
     */
    protected GenericDao genericDao;

    /**
     * Public constructor for creating a new GenericManagerImpl.
     * @param genericDao the GenericDao to use for persistence
     */
    public GenericManagerImpl(final GenericDao genericDao) {
        this.genericDao = genericDao;
    }
}
