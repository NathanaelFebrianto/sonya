package com.nhn.socialbuzz.me2day.dao;

import java.util.List;

import com.nhn.socialbuzz.me2day.GenericMapper;
import com.nhn.socialbuzz.me2day.model.Post;

/**
 * A interface for post mapper.
 * 
 * @author Younggue Bae
 */
public interface PostMapper extends GenericMapper {

	public List<Post> selectPosts(Post post);
	
	public void insertPost(Post post);
	
	public void updatePost(Post post);

}
