DELIMITER $$

DROP PROCEDURE IF EXISTS `proc_collect_bts_code`$$

CREATE PROCEDURE proc_collect_bts_code()

    BEGIN
	DELETE FROM dm_code WHERE group_code_id = 'BTS_ISSUE_PRIORITY';
	DELETE FROM dm_code WHERE group_code_id = 'BTS_ISSUE_TYPE';
	DELETE FROM dm_code WHERE group_code_id = 'BTS_ISSUE_STATUS';
	
	INSERT INTO dm_code (group_code_id, code_id, group_code_name, code_name, description, display_order, etl_create_date, etl_update_date)
		SELECT 'BTS_ISSUE_PRIORITY', id, '이슈우선순위', pname, description, sequence, NOW(), NOW()
		FROM priority;
		
	INSERT INTO dm_code (group_code_id, code_id, group_code_name, code_name, description, display_order, etl_create_date, etl_update_date)
		SELECT 'BTS_ISSUE_TYPE', id, '이슈유형', pname, description, sequence, NOW(), NOW()
		FROM issuetype;		

	INSERT INTO dm_code (group_code_id, code_id, group_code_name, code_name, description, display_order, etl_create_date, etl_update_date)
		SELECT 'BTS_ISSUE_STATUS', id, '이슈상태', pname, description, sequence, NOW(), NOW()
		FROM issuestatus;	

    END$$

DELIMITER ;