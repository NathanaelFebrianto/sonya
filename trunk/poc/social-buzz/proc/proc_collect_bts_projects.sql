DELIMITER $$

DROP PROCEDURE IF EXISTS `proc_collect_bts_projects`$$

CREATE PROCEDURE proc_collect_bts_projects()
    proc:

    BEGIN
	DECLARE v_project_id DECIMAL(18);
	DECLARE done, error BOOLEAN DEFAULT FALSE;
	
	/* define cusor */
	DECLARE cursor1 CURSOR FOR 
		SELECT id FROM project INNER JOIN dm_target_project ON project.pkey = dm_target_project.project_key;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET error = TRUE;
	OPEN cursor1;
	IF error THEN
		SELECT 'OPEN failed'; LEAVE proc; 
	END IF;
	REPEAT		
		FETCH cursor1 INTO v_project_id;
		IF error THEN
			SELECT 'FETCH failed'; LEAVE proc;
		END IF;
		IF done = FALSE THEN
			CALL proc_collect_bts_project(v_project_id);
			IF error THEN
				SELECT 'proc_collect_bts_project() failed';
			END IF;
		END IF;	
		UNTIL done = TRUE
	END REPEAT;
	CLOSE cursor1;
	IF error THEN
		SELECT 'CLOSE failed';
	END IF;
				
    END$$

DELIMITER ;