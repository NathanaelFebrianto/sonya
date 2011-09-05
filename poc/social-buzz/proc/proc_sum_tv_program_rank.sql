DELIMITER $$

DROP PROCEDURE IF EXISTS `proc_sum_tv_program_rank`$$

CREATE PROCEDURE proc_sum_tv_program_rank
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
	
	OPEN cursor1;
	IF error THEN
		SELECT 'OPEN failed'; LEAVE proc; 
	END IF;
	REPEAT
		FETCH cursor1 INTO v_program_id, v_post_count, v_post_user_count, v_comment_count, v_metoo_count;
		/*	
		IF error THEN
			SELECT 'FETCH failed'; LEAVE proc;
		END IF;		
		*/
		IF done = FALSE THEN
			/*
			SELECT COUNT(*) FROM tv_program_rank 
			WHERE program_id = v_program_id 
				AND publish_date >= STR_TO_DATE(p_start_date, '%Y%m%d') 
				AND publish_date <= STR_TO_DATE(p_end_date, '%Y%m%d')
			INTO v_exist_count;
			*/
			
			DELETE FROM tv_program_rank
			WHERE program_id = v_program_id 
				AND publish_date >= STR_TO_DATE(p_start_date, '%Y%m%d') 
				AND publish_date <= STR_TO_DATE(p_end_date, '%Y%m%d');
			
			INSERT INTO tv_program_rank (
				  program_id
				, start_date
				, end_date
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
		
	IF error THEN
		SELECT 'CLOSE failed';
	END IF;
				
    END$$

DELIMITER ;