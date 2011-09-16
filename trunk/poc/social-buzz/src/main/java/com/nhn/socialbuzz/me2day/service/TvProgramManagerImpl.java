package com.nhn.socialbuzz.me2day.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nhn.socialbuzz.common.GenericManagerImpl;
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
    		List<TvProgram> list = mapper.selectPrograms(program);
     		return list.get(0);
    	} finally {
    		session.close();
    	}
	}
	
	public List<TvProgram> getPrograms(TvProgram program) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		TvProgramMapper mapper = session.getMapper(TvProgramMapper.class);
    		List<TvProgram> result = mapper.selectPrograms(program);
    		return result;
    	} finally {
    		session.close();
    	}		
	}

}
