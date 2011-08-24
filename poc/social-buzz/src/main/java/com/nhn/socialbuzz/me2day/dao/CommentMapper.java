package com.nhn.socialbuzz.me2day.dao;

import java.util.List;

import com.nhn.socialbuzz.me2day.GenericMapper;
import com.nhn.socialbuzz.me2day.model.Comment;

/**
 * A interface for comment mapper.
 * 
 * @author Younggue Bae
 */
public interface CommentMapper extends GenericMapper {

	public List<Comment> selectComments(Comment comment);
	
	public void insertComment(Comment comment);
	
	public void deleteComments(Comment comment);

}
