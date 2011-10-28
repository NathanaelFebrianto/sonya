package com.nhn.socialbuzz.me2day.service;

import java.util.List;

import com.nhn.socialbuzz.common.GenericManager;
import com.nhn.socialbuzz.me2day.model.Term;

/**
 * A interface for term manager.
 * 
 * @author Younggue Bae
 */
public interface TermManager extends GenericManager {

	public List<Term> getTerms(Term term);

	public void addTerm(Term term);
	
	public void deleteTerms(Term term);
	
}
