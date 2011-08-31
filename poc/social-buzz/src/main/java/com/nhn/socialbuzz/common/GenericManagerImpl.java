package com.nhn.socialbuzz.common;

import org.apache.ibatis.session.SqlSessionFactory;


/**
 * A implementation for generic manager.
 * 
 * @author Younggue Bae
 */
public class GenericManagerImpl implements GenericManager {
    
	//protected static Logger logger = Logger.getLogger(GenericManagerImpl.class.getName());
	
	/** SQL Session factory */
	protected SqlSessionFactory sqlSessionFactory = null;
	
	/**
     * GenericMapper instance, set by constructor of this class.
     */
    protected GenericMapper mapper;

    /**
     * Constructor.
     * 
     */
    public GenericManagerImpl() {
    	sqlSessionFactory = GenericSqlSessionFactory.getSqlSessionFactory();;
    }
    
    /**
     * Constructor.
     * 
     * @param mapper the mapper to use for persistence
     */
    public GenericManagerImpl(GenericMapper mapper) {
        this.mapper = mapper;
    }
    
	/**
	 * Sets the data source.
	 * 
	 * @param mapper the dao mapper
	 */
	public void setDataSource(GenericMapper mapper) {
		this.mapper = mapper;
	}
}
