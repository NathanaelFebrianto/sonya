DELIMITER $$

DROP PROCEDURE IF EXISTS `proc_collect_bts_project`$$

CREATE PROCEDURE proc_collect_bts_project
(IN p_project_id DECIMAL(18))

    BEGIN

	/* collect project */
	DELETE FROM dm_project WHERE project_id = p_project_id;	
	INSERT INTO dm_project (project_id, project_name, leader, project_key, etl_create_date, etl_update_date)
		SELECT id, pname, lead, pkey, NOW(), NOW()
		FROM project
		WHERE project.id = p_project_id;
	
	/* collect component */
	DELETE FROM dm_component WHERE project_id = p_project_id;
	INSERT INTO dm_component (component_id, project_id, component_name, lead, etl_create_date, etl_update_date)
		SELECT id, project, cname, lead, NOW(), NOW()
		FROM component
		WHERE component.project = p_project_id;
		
	/* collect version */
	CALL proc_collect_bts_version(p_project_id);
	
	/* collect feature */
	CALL proc_collect_bts_feature(p_project_id);
					
    END$$

DELIMITER ;