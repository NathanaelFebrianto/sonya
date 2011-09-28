
/** tv_program_rank.csv 파일 생성 **/
SELECT a.*
	, a.positive_post_count/a.post_count AS positive_post_rate
	, a.negative_post_count/a.post_count AS negative_post_rate
	, a.positive_post_user_count/a.post_user_count AS positive_post_user_rate
	, a.negative_post_user_count/a.post_user_count AS negative_post_user_rate
	, a.negative_post_count/a.positive_post_count AS negative_positive_post_rate
	, a.negative_post_user_count/a.positive_post_user_count AS negative_positive_post_user_rate
	, b.nation as nation
	, b.category as category
	, b.channel as channel
	, b.air_cycle as air_cycle 
FROM tv_program_rank a JOIN tv_program b ON a.program_id = b.program_id


/** tv_program_watch_rate.csv 파일 생성 **/
SELECT a.*, b.category, b.channel, b.air_cycle 
FROM tv_program_watch_rate a JOIN tv_program b ON a.program_id = b.program_id


/** 인기도 추출 **/ 
SELECT c.start_date, c.end_date, c.site, b.nation, b.title, b.category, b.channel, b.air_cycle, c.post_count, c.metoo_count, c.comment_count, c.score, c.rank, a.watch_rate, a.rank 
FROM tv_program_watch_rate a JOIN tv_program b ON a.program_id = b.program_id 
	JOIN tv_program_rank c ON a.program_id = c.program_id AND a.start_date = c.start_date AND a.end_date = c.end_date
ORDER BY c.start_date, c.site, b.nation, c.rank


/** terms 추출 **/
SELECT a.program_id, b.title, a.start_date, a.end_date, a.term, a.tf
FROM term a JOIN tv_program b ON a.program_id = b.program_id
WHERE TYPE = "TERM"
ORDER BY a.program_id ASC, a.start_date ASC, a.tf DESC  

/** 일별 추이 **/
SELECT program_id, SUBSTR(publish_date, 1, 10), COUNT(*), SUM(comment_count), SUM(metoo_count) 
FROM post 
GROUP BY program_id, SUBSTR(publish_date, 1, 10)

SELECT program_id, SUBSTR(create_date, 1, 10), COUNT(*)
FROM tweet 
GROUP BY program_id, SUBSTR(create_date, 1, 10)
