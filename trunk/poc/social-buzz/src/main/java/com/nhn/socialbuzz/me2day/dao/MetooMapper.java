package com.nhn.socialbuzz.me2day.dao;

import java.util.List;

import com.nhn.socialbuzz.me2day.GenericMapper;
import com.nhn.socialbuzz.me2day.model.Metoo;

/**
 * A interface for metoo mapper.
 * 
 * @author Younggue Bae
 */
public interface MetooMapper extends GenericMapper {

	public List<Metoo> selectMetoos(Metoo metoo);
	
	public void insertMetoo(Metoo metoo);
	
	public void deleteMetoos(Metoo metoo);

}

