package com.nhn.socialbuzz.me2day.service;

import com.nhn.socialbuzz.me2day.GenericManager;
import com.nhn.socialbuzz.me2day.model.TvProgram;

/**
 * A interface for tv program manager.
 * 
 * @author Younggue Bae
 */
public interface TvProgramManager extends GenericManager {

	public TvProgram getProgram(TvProgram progrma);
	
}
