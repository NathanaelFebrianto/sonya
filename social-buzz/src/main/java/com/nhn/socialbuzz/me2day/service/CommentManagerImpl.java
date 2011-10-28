package com.nhn.socialbuzz.me2day.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nhn.socialbuzz.common.GenericManagerImpl;
import com.nhn.socialbuzz.me2day.dao.CommentMapper;
import com.nhn.socialbuzz.me2day.model.Comment;

/**
 * A implementation for comment manager.
 * 
 * @author Younggue Bae
 */
public class CommentManagerImpl extends GenericManagerImpl implements CommentManager {

    public CommentManagerImpl() { }
	
	public List<Comment> getComments(Comment comment) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		CommentMapper mapper = session.getMapper(CommentMapper.class);
    		List<Comment> result = mapper.selectComments(comment);
    		return result;
    	} finally {
    		session.close();
    	}
	}
    
	public void addComment(Comment comment) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		CommentMapper mapper = session.getMapper(CommentMapper.class);
    		mapper.insertComment(comment);
    		session.commit();
    	} finally {
    		session.close();
    	}
	}
	
	public void setComment(Comment comment) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		CommentMapper mapper = session.getMapper(CommentMapper.class);
    		mapper.updateComment(comment);
    		session.commit();
    	} finally {
    		session.close();
    	}
	}
	
	public void deleteComments(String postId) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		Comment comment = new Comment();
    		comment.setPostId(postId);
    		
    		CommentMapper mapper = session.getMapper(CommentMapper.class);
    		mapper.deleteComments(comment);
    		session.commit();
    	} finally {
    		session.close();
    	}		
	}
}
