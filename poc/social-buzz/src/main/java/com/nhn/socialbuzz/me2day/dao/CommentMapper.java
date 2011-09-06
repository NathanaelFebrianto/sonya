package com.nhn.socialbuzz.me2day.dao;

import java.util.List;

import com.nhn.socialbuzz.common.GenericMapper;
import com.nhn.socialbuzz.me2day.model.Comment;
import com.nhn.socialbuzz.me2day.model.Post;

/**
 * A interface for comment mapper.
 * 
 * @author Younggue Bae
 */
public interface CommentMapper extends GenericMapper {

	public List<Comment> selectComments(Comment comment);
	
	public void insertComment(Comment comment);
	
	public void updateComment(Comment comment);
	
	public void deleteComments(Comment comment);

}
