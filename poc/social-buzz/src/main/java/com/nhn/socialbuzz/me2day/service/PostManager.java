package com.nhn.socialbuzz.me2day.service;

import java.util.List;

import com.nhn.socialbuzz.me2day.GenericManager;
import com.nhn.socialbuzz.me2day.model.Post;

/**
 * A interface for post manager.
 * 
 * @author Younggue Bae
 */
public interface PostManager extends GenericManager {

	public List<Post> getPosts(Post post);

	public void addPost(Post post);
	
	public void setPost(Post post);
	
}
