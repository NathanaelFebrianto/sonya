package com.nhn.socialbuzz.me2day.service;

import java.util.List;

import com.nhn.socialbuzz.common.GenericManager;
import com.nhn.socialbuzz.me2day.model.TvProgram;

/**
 * A interface for tv program manager.
 * 
 * @author Younggue Bae
 */
public interface TvProgramManager extends GenericManager {

	public TvProgram getProgram(String programId);
	
	public List<TvProgram> getPrograms(TvProgram program);
	
}
