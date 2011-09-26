DELIMITER $$

DROP PROCEDURE IF EXISTS `proc_sum_tv_program_rank_twitter`$$

CREATE PROCEDURE proc_sum_tv_program_rank_twitter
(IN p_start_date VARCHAR(8), IN p_end_date VARCHAR(8))

    proc: 

    BEGIN
	DECLARE v_exist_count INT;
	DECLARE v_program_id VARCHAR(50);
	DECLARE v_post_count INT DEFAULT 0;
	DECLARE v_post_user_count INT DEFAULT 0;
	DECLARE v_positive_post_count INT DEFAULT 0;
	DECLARE v_negative_post_count INT DEFAULT 0;
	DECLARE v_positive_post_user_count INT DEFAULT 0;
	DECLARE v_negative_post_user_count INT DEFAULT 0;
	DECLARE done, error BOOLEAN DEFAULT FALSE;

	/* define cusor */
	DECLARE cursor1 CURSOR FOR 
		SELECT program_id 
			, COUNT(*) AS post_count
			, COUNT(DISTINCT user_no) AS post_user_count
		FROM tweet 
		WHERE create_date >= STR_TO_DATE(p_start_date, '%Y%m%d') AND create_date <= STR_TO_DATE(p_end_date, '%Y%m%d')
		GROUP BY program_id;
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET error = TRUE;	
	
	/* cursor */
	OPEN cursor1;	
	REPEAT
		FETCH cursor1 INTO v_program_id, v_post_count, v_post_user_count;

		IF done = FALSE THEN
			DELETE FROM tv_program_rank
			WHERE program_id = v_program_id 
				AND start_date = p_start_date
				AND end_date = p_end_date
				AND site = "twitter";
			
			INSERT INTO tv_program_rank (
				  program_id
				, start_date
				, end_date
				, site
				, duration_type
				, post_count
				, post_user_count
				, register_date
				, update_date

			) VALUES (
				  v_program_id
				, p_start_date
				, p_end_date
				, "twitter"
				, "weekly"
				, v_post_count
				, v_post_user_count
				, NOW()
				, NOW()
			);			
		END IF;
		UNTIL done = TRUE
	END REPEAT;
	CLOSE cursor1;
	
	COMMIT;

	/* update positive post */
	UPDATE tv_program_rank AS a INNER JOIN (
		SELECT program_id AS program_id
			, COUNT(*) AS positive_post_count
			, COUNT(DISTINCT user_no) AS positive_post_user_count
		FROM tweet 
		WHERE create_date >= STR_TO_DATE(p_start_date, '%Y%m%d') 
			AND create_date <= STR_TO_DATE(p_end_date, '%Y%m%d')			
			AND liwc_positive > (liwc_negative + liwc_anger)
		GROUP BY program_id
		) AS b ON a.program_id = b.program_id
	SET	a.positive_post_count = b.positive_post_count,
		a.positive_post_user_count = b.positive_post_user_count
	WHERE a.start_date = p_start_date AND a.end_date = p_end_date AND a.site = "twitter";	

	/* update negative post */
	UPDATE tv_program_rank AS a INNER JOIN (
		SELECT program_id AS program_id
			, COUNT(*) AS negative_post_count
			, COUNT(DISTINCT user_no) AS negative_post_user_count
		FROM tweet 
		WHERE create_date >= STR_TO_DATE(p_start_date, '%Y%m%d') 
			AND create_date <= STR_TO_DATE(p_end_date, '%Y%m%d')
			AND liwc_positive < (liwc_negative + liwc_anger)
		GROUP BY program_id
		) AS b ON a.program_id = b.program_id
	SET	a.negative_post_count = b.negative_post_count,
		a.negative_post_user_count = b.negative_post_user_count
	WHERE a.start_date = p_start_date AND a.end_date = p_end_date AND a.site = "twitter";	


	/* update popularity */
	UPDATE tv_program_rank SET
		score = post_count
	WHERE start_date = p_start_date AND end_date = p_end_date AND site = "twitter";
	
	/* update rank -- KO */
	UPDATE tv_program_rank AS a INNER JOIN (
		SELECT @rank:=@rank+1 rank_temp, sub.*
		FROM (SELECT @rank:=0) rank,
		(SELECT tv_program_rank.* FROM tv_program_rank JOIN tv_program ON tv_program_rank.program_id = tv_program.program_id
		WHERE tv_program.nation = "KO" AND start_date = p_start_date AND end_date = p_end_date AND site = "twitter" ORDER BY score DESC) sub
		) AS b ON a.program_id = b.program_id
	SET a.rank = b.rank_temp
	WHERE a.start_date = p_start_date AND a.end_date = p_end_date AND a.site = "twitter";

	/* update rank -- US */
	UPDATE tv_program_rank AS a INNER JOIN (
		SELECT @rank:=@rank+1 rank_temp, sub.*
		FROM (SELECT @rank:=0) rank,
		(SELECT tv_program_rank.* FROM tv_program_rank JOIN tv_program ON tv_program_rank.program_id = tv_program.program_id
		WHERE tv_program.nation = "US" AND start_date = p_start_date AND end_date = p_end_date AND site = "twitter" ORDER BY score DESC) sub
		) AS b ON a.program_id = b.program_id
	SET a.rank = b.rank_temp
	WHERE a.start_date = p_start_date AND a.end_date = p_end_date AND a.site = "twitter";
		
	IF error THEN
		SELECT 'CLOSE failed';
	END IF;
				
    END$$

DELIMITER ;