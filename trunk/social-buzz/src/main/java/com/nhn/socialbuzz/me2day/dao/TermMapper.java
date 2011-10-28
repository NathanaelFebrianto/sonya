package com.nhn.socialbuzz.me2day.dao;

import java.util.List;

import com.nhn.socialbuzz.common.GenericMapper;
import com.nhn.socialbuzz.me2day.model.Term;

/**
 * A interface for term mapper.
 * 
 * @author Younggue Bae
 */
public interface TermMapper extends GenericMapper {

	public List<Term> selectTerms(Term term);
	
	public void insertTerm(Term term);
	
	public void deleteTerms(Term term);

}

