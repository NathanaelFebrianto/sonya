DELIMITER $$

DROP PROCEDURE IF EXISTS `proc_sum_tv_program_rank_me2day`$$

CREATE PROCEDURE proc_sum_tv_program_rank_me2day
(IN p_start_date VARCHAR(8), IN p_end_date VARCHAR(8))

    proc: 

    BEGIN
	DECLARE v_exist_count INT;
	DECLARE v_program_id VARCHAR(50);
	DECLARE v_post_count INT DEFAULT 0;
	DECLARE v_comment_count INT DEFAULT 0;
	DECLARE v_post_user_count INT DEFAULT 0;
	DECLARE v_comment_user_count INT DEFAULT 0;
	DECLARE v_metoo_count INT DEFAULT 0;
	DECLARE v_positive_post_count INT DEFAULT 0;
	DECLARE v_negative_post_count INT DEFAULT 0;
	DECLARE v_positive_comment_count INT DEFAULT 0;
	DECLARE v_negative_comment_count INT DEFAULT 0;
	DECLARE v_positive_post_user_count INT DEFAULT 0;
	DECLARE v_negative_post_user_count INT DEFAULT 0;
	DECLARE v_positive_comment_user_count INT DEFAULT 0;
	DECLARE v_negative_comment_user_count INT DEFAULT 0;
	DECLARE done, error BOOLEAN DEFAULT FALSE;
	
	
	/* define cusor */
	DECLARE cursor1 CURSOR FOR 
		SELECT program_id 
			, COUNT(*) AS post_count
			, COUNT(DISTINCT author_id) AS post_user_count
			, SUM(comment_count) AS comment_count
			, SUM(metoo_count) AS metoo_count
		FROM post 
		WHERE publish_date >= STR_TO_DATE(p_start_date, '%Y%m%d') AND publish_date <= STR_TO_DATE(p_end_date, '%Y%m%d')
		GROUP BY program_id;
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET error = TRUE;

	/* cursor */
	OPEN cursor1;	
	REPEAT
		FETCH cursor1 INTO v_program_id, v_post_count, v_post_user_count, v_comment_count, v_metoo_count;

		IF done = FALSE THEN
			DELETE FROM tv_program_rank
			WHERE program_id = v_program_id 
				AND start_date = p_start_date
				AND end_date = p_end_date
				AND site = "me2day";
			
			INSERT INTO tv_program_rank (
				  program_id
				, start_date
				, end_date
				, site
				, duration_type
				, post_count
				, comment_count
				, post_user_count
				, metoo_count
				, register_date
				, update_date

			) VALUES (
				  v_program_id
				, p_start_date
				, p_end_date
				, "me2day"
				, "weekly"
				, v_post_count
				, v_comment_count
				, v_post_user_count
				, v_metoo_count
				, NOW()
				, NOW()
			);			
		END IF;
		UNTIL done = TRUE
	END REPEAT;
	CLOSE cursor1;
	
	COMMIT;

	/* update comment user count */
	UPDATE tv_program_rank AS a INNER JOIN (
		SELECT post.program_id AS program_id
			, COUNT(DISTINCT comment.author_id) AS comment_user_count
		FROM COMMENT JOIN post ON comment.post_id = post.post_id 
		WHERE post.publish_date >= STR_TO_DATE(p_start_date, '%Y%m%d') 
			AND post.publish_date <= STR_TO_DATE(p_end_date, '%Y%m%d')
		GROUP BY post.program_id
		) AS b ON a.program_id = b.program_id
	SET	a.comment_user_count = b.comment_user_count
	WHERE a.start_date = p_start_date AND a.end_date = p_end_date AND a.site = "me2day";
	

	/* update positive post */
	UPDATE tv_program_rank AS a INNER JOIN (
		SELECT program_id AS program_id
			, COUNT(*) AS positive_post_count
			, COUNT(DISTINCT author_id) AS positive_post_user_count
		FROM post 
		WHERE publish_date >= STR_TO_DATE(p_start_date, '%Y%m%d') 
			AND publish_date <= STR_TO_DATE(p_end_date, '%Y%m%d')			
			AND liwc_positive > (liwc_negative + liwc_anger)
		GROUP BY program_id
		) AS b ON a.program_id = b.program_id
	SET	a.positive_post_count = b.positive_post_count,
		a.positive_post_user_count = b.positive_post_user_count
	WHERE a.start_date = p_start_date AND a.end_date = p_end_date AND a.site = "me2day";	

	/* update negative post */
	UPDATE tv_program_rank AS a INNER JOIN (
		SELECT program_id AS program_id
			, COUNT(*) AS negative_post_count
			, COUNT(DISTINCT author_id) AS negative_post_user_count
		FROM post 
		WHERE publish_date >= STR_TO_DATE(p_start_date, '%Y%m%d') 
			AND publish_date <= STR_TO_DATE(p_end_date, '%Y%m%d')
			AND liwc_positive < (liwc_negative + liwc_anger)
		GROUP BY program_id
		) AS b ON a.program_id = b.program_id
	SET	a.negative_post_count = b.negative_post_count,
		a.negative_post_user_count = b.negative_post_user_count
	WHERE a.start_date = p_start_date AND a.end_date = p_end_date AND a.site = "me2day";	

	/* update positive comment */
	UPDATE tv_program_rank AS a INNER JOIN (
		SELECT post.program_id AS program_id
			, COUNT(comment.comment_id) AS positive_comment_count
			, COUNT(DISTINCT comment.author_id) AS positive_comment_user_count
		FROM COMMENT JOIN post ON comment.post_id = post.post_id 
		WHERE post.publish_date >= STR_TO_DATE(p_start_date, '%Y%m%d') 
			AND post.publish_date <= STR_TO_DATE(p_end_date, '%Y%m%d')
			AND comment.liwc_positive > (comment.liwc_negative + comment.liwc_anger)
		GROUP BY post.program_id
		) AS b ON a.program_id = b.program_id
	SET	a.positive_comment_count = b.positive_comment_count,
		a.positive_comment_user_count = b.positive_comment_user_count
	WHERE	a.start_date = p_start_date AND a.end_date = p_end_date AND a.site = "me2day";
	
	
	/* update positive comment */
	UPDATE tv_program_rank AS a INNER JOIN (
		SELECT post.program_id AS program_id
			, COUNT(comment.comment_id) AS negative_comment_count
			, COUNT(DISTINCT comment.author_id) AS negative_comment_user_count
		FROM COMMENT JOIN post ON comment.post_id = post.post_id 
		WHERE post.publish_date >= STR_TO_DATE(p_start_date, '%Y%m%d') 
			AND post.publish_date <= STR_TO_DATE(p_end_date, '%Y%m%d')
			AND comment.liwc_positive < (comment.liwc_negative + comment.liwc_anger)
		GROUP BY post.program_id
		) AS b ON a.program_id = b.program_id
	SET	a.negative_comment_count = b.negative_comment_count,
		a.negative_comment_user_count = b.negative_comment_user_count
	WHERE	a.start_date = p_start_date AND a.end_date = p_end_date AND a.site = "me2day";
	
	
		
	IF error THEN
		SELECT 'CLOSE failed';
	END IF;
				
    END$$

DELIMITER ;