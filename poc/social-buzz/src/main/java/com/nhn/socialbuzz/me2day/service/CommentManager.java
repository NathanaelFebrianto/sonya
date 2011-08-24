package com.nhn.socialbuzz.me2day.service;

import java.util.List;

import com.nhn.socialbuzz.me2day.GenericManager;
import com.nhn.socialbuzz.me2day.model.Comment;

/**
 * A interface for comment manager.
 * 
 * @author Younggue Bae
 */
public interface CommentManager extends GenericManager {

	public List<Comment> getComments(Comment comment);

	public void addComment(Comment comment);
	
	public void deleteComments(Comment comment);
	
}
