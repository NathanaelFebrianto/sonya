package com.nhn.socialbuzz.me2day.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nhn.socialbuzz.common.GenericManagerImpl;
import com.nhn.socialbuzz.me2day.dao.PostMapper;
import com.nhn.socialbuzz.me2day.model.Post;

/**
 * A implementation for post manager.
 * 
 * @author Younggue Bae
 */
public class PostManagerImpl extends GenericManagerImpl implements PostManager {

    public PostManagerImpl() { }
	
	public List<Post> getPosts(Post post) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		PostMapper mapper = session.getMapper(PostMapper.class);
    		List<Post> result = mapper.selectPosts(post);
    		return result;
    	} finally {
    		session.close();
    	}
	}
    
	public void addPost(Post post) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		PostMapper mapper = session.getMapper(PostMapper.class);
    		mapper.insertPost(post);
    		session.commit();
    	} finally {
    		session.close();
    	}
	}
	
	public void setPost(Post post) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		PostMapper mapper = session.getMapper(PostMapper.class);
    		mapper.updatePost(post);
    		session.commit();
    	} finally {
    		session.close();
    	}		
	}
}
