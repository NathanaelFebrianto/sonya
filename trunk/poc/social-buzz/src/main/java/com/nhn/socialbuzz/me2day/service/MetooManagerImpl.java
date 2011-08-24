package com.nhn.socialbuzz.me2day.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nhn.socialbuzz.me2day.GenericManagerImpl;
import com.nhn.socialbuzz.me2day.dao.MetooMapper;
import com.nhn.socialbuzz.me2day.model.Metoo;

/**
 * A implementation for metoo manager.
 * 
 * @author Younggue Bae
 */
public class MetooManagerImpl extends GenericManagerImpl implements MetooManager {

    public MetooManagerImpl() { }
	
	public List<Metoo> getMetoos(Metoo metoo) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		MetooMapper mapper = session.getMapper(MetooMapper.class);
    		List<Metoo> result = mapper.selectMetoos(metoo);
    		return result;
    	} finally {
    		session.close();
    	}
	}
    
	public void addMetoo(Metoo metoo) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		MetooMapper mapper = session.getMapper(MetooMapper.class);
    		mapper.insertMetoo(metoo);
    		session.commit();
    	} finally {
    		session.close();
    	}
	}
	
	public void deleteMetoos(Metoo metoo) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		MetooMapper mapper = session.getMapper(MetooMapper.class);
    		mapper.deleteMetoos(metoo);
    		session.commit();
    	} finally {
    		session.close();
    	}		
	}
}
