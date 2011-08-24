package com.nhn.socialbuzz.me2day.service;

import java.util.List;

import com.nhn.socialbuzz.me2day.GenericManager;
import com.nhn.socialbuzz.me2day.model.Metoo;

/**
 * A interface for metoo manager.
 * 
 * @author Younggue Bae
 */
public interface MetooManager extends GenericManager {

	public List<Metoo> getMetoos(Metoo metoo);

	public void addMetoo(Metoo metoo);
	
	public void deleteMetoos(Metoo metoo);
	
}
