package com.nhn.socialbuzz.me2day.dao;

import com.nhn.socialbuzz.common.GenericMapper;
import com.nhn.socialbuzz.me2day.model.TvProgram;

/**
 * A interface for tv program mapper.
 * 
 * @author Younggue Bae
 */
public interface TvProgramMapper extends GenericMapper {

	public TvProgram selectProgram(TvProgram program);	

}
