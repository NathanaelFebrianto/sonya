package com.nhn.socialbuzz.me2day.service;

import org.apache.ibatis.session.SqlSession;

import com.nhn.socialbuzz.me2day.GenericManagerImpl;
import com.nhn.socialbuzz.me2day.dao.TvProgramMapper;
import com.nhn.socialbuzz.me2day.model.TvProgram;

/**
 * A implementation for tv program manager.
 * 
 * @author Younggue Bae
 */
public class TvProgramManagerImpl extends GenericManagerImpl implements TvProgramManager {

    public TvProgramManagerImpl() { }
	
	public TvProgram getProgram(String programId) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TvProgram program = new TvProgram();
    		program.setProgramId(programId);
    		
    		TvProgramMapper mapper = session.getMapper(TvProgramMapper.class);
    		TvProgram result = mapper.selectProgram(program);
    		return result;
    	} finally {
    		session.close();
    	}
	}

}
