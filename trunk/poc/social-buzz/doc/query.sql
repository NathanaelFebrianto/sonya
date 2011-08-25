SELECT a.program_id, b.title, b.category, b.channel, b.air_cycle, a.post_count, a.comment_count, a.metoo_count, b.watch_rate_source, b.watch_rate, b.search_keywords 
FROM ( SELECT program_id, COUNT(*) AS post_count, SUM(comment_count) AS comment_count, SUM(metoo_count) AS metoo_count 
	FROM post
	GROUP BY program_id
) AS a LEFT OUTER JOIN tv_program b ON a.program_id = b.program_id


