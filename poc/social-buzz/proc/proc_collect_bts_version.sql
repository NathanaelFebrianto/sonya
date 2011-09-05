DELIMITER $$

DROP PROCEDURE IF EXISTS `proc_collect_bts_version`$$

CREATE PROCEDURE proc_collect_bts_version
(IN p_project_id DECIMAL(18))
    proc: 

    BEGIN
	DECLARE v_version_id DECIMAL(18);
	DECLARE v_version_name VARCHAR(500);
	DECLARE v_sequence DECIMAL(18);
	DECLARE v_released VARCHAR(10);
	DECLARE v_archived VARCHAR(10);
	DECLARE v_release_date DATETIME;
	DECLARE v_version_count INT DEFAULT 0;
	DECLARE done, error BOOLEAN DEFAULT FALSE;
	
	/* define cusor */
	DECLARE cursor1 CURSOR FOR 
		SELECT id, vname, sequence, released, archived, releasedate 
		FROM projectversion WHERE project = p_project_id;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET error = TRUE;
	OPEN cursor1;
	IF error THEN
		SELECT 'OPEN failed'; LEAVE proc; 
	END IF;
	REPEAT
		FETCH cursor1 INTO v_version_id, v_version_name, v_sequence, v_released, v_archived, v_release_date;
		IF error THEN
			SELECT 'FETCH failed'; LEAVE proc;
		END IF;		
		IF done = FALSE THEN
			SELECT COUNT(*) FROM dm_version WHERE version_id = v_version_id INTO v_version_count;
			/* dm_version 테이블에 이미 존재하는 경우 */			
			IF v_version_count > 0 THEN
				UPDATE dm_version SET 
					project_id = p_project_id, 
					version_name = v_version_name, 
					sequence = v_sequence, 
					released = v_released, 
					archived = v_archived, 
					release_date = v_release_date, 
					etl_update_date = NOW()
				WHERE version_id = v_version_id;

			/* dm_version 테이블에 존재하지 않는 경우 */			
			ELSE
				INSERT INTO dm_version (
					version_id,
					project_id,
					version_name,
					sequence,
					released,
					archived,
					release_date,
					etl_create_date,
					etl_update_date
				) VALUES (
					v_version_id,
					p_project_id,
					v_version_name,
					v_sequence, 
					v_released, 
					v_archived, 
					v_release_date, 
					NOW(),
					NOW()
				);
			END IF;
			
		END IF;
		UNTIL done = TRUE
	END REPEAT;
	CLOSE cursor1;
	
	/* projectversion에서 삭제된 경우, dm_version 테이블의 delete_yn 업데이트 */
	UPDATE dm_version SET 
		delete_yn = '1',
		etl_update_date = NOW()
	WHERE project_id = p_project_id AND NOT EXISTS (
		SELECT id FROM projectversion 
		WHERE dm_version.version_id = projectversion.id AND projectversion.project = p_project_id
	);
		
	IF error THEN
		SELECT 'CLOSE failed';
	END IF;
				
    END$$

DELIMITER ;