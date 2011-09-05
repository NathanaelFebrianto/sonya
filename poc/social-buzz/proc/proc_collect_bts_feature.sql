DELIMITER $$

DROP PROCEDURE IF EXISTS `proc_collect_bts_feature`$$

CREATE PROCEDURE proc_collect_bts_feature
(IN p_project_id DECIMAL(18))
    proc: 
    
    BEGIN
	DECLARE v_project_key VARCHAR(255);
	DECLARE error BOOLEAN DEFAULT FALSE;
	
	/* get project_key */
	SELECT project_key FROM dm_project WHERE project_id = p_project_id INTO v_project_key;
	
	/* collect feature */
	DELETE FROM dm_feature WHERE project_id = p_project_id;	
	INSERT INTO dm_feature (project_id, feature_id, project_key, feature_key, creater, assignee, issue_type, 
		summary, priority, issue_status, create_date, update_date, due_date, 
		time_original_estimate, time_estimate, time_spent, etl_create_date, etl_update_date)
	SELECT project, id, v_project_key, pkey, reporter, assignee, issuetype,
		summary, priority, issuestatus, created, updated, duedate, 
		timeoriginalestimate, timeestimate, timespent, NOW(), NOW()
	FROM jiraissue
	WHERE jiraissue.project = p_project_id;
	IF error THEN
		SELECT 'INSERT INTO dm_feature failed'; LEAVE proc; 
	END IF;
	
	/* collect feature-component */
	DELETE FROM dm_feature_component WHERE project_id = p_project_id;		
	INSERT INTO dm_feature_component (feature_id, component_id, project_id, etl_create_date, etl_update_date)
	SELECT source_node_id, sink_node_id, p_project_id, NOW(), NOW()
	FROM nodeassociation
	WHERE source_node_entity = 'Issue' AND sink_node_entity = 'Component' AND association_type = 'IssueComponent'
	AND EXISTS (
		SELECT feature_id FROM dm_feature
		WHERE dm_feature.feature_id = nodeassociation.source_node_id AND dm_feature.project_id = p_project_id
	); 
	IF error THEN
		SELECT 'INSERT INTO dm_feature_component failed'; LEAVE proc; 
	END IF;
	
	/* collect feature-version */
	DELETE FROM dm_feature_version WHERE project_id = p_project_id;		
	INSERT INTO dm_feature_version (feature_id, version_id, version_type, project_id, etl_create_date, etl_update_date)
	SELECT source_node_id, sink_node_id, association_type, p_project_id, NOW(), NOW()
	FROM nodeassociation
	WHERE source_node_entity = 'Issue' AND sink_node_entity = 'Version'
	AND EXISTS (
		SELECT feature_id FROM dm_feature
		WHERE dm_feature.feature_id = nodeassociation.source_node_id AND dm_feature.project_id = p_project_id
	); 
	IF error THEN
		SELECT 'INSERT INTO dm_feature_version failed'; LEAVE proc; 
	END IF;
					
    END$$

DELIMITER ;