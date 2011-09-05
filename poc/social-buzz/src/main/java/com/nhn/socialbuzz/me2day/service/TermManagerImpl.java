package com.nhn.socialbuzz.me2day.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nhn.socialbuzz.common.GenericManagerImpl;
import com.nhn.socialbuzz.me2day.dao.TermMapper;
import com.nhn.socialbuzz.me2day.model.Term;

/**
 * A implementation for term manager.
 * 
 * @author Younggue Bae
 */
public class TermManagerImpl extends GenericManagerImpl implements TermManager {

    public TermManagerImpl() { }
	
	public List<Term> getTerms(Term term) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TermMapper mapper = session.getMapper(TermMapper.class);
    		List<Term> result = mapper.selectTerms(term);
    		return result;
    	} finally {
    		session.close();
    	}
	}
    
	public void addTerm(Term term) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TermMapper mapper = session.getMapper(TermMapper.class);
    		mapper.insertTerm(term);
    		session.commit();
    	} finally {
    		session.close();
    	}
	}
	
	public void deleteTerms(Term term) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TermMapper mapper = session.getMapper(TermMapper.class);
    		mapper.deleteTerms(term);
    		session.commit();
    	} finally {
    		session.close();
    	}		
	}
}
