SELECT a.program_id, b.title, b.category, b.channel, b.air_cycle, a.post_count, a.comment_count, a.metoo_count, b.watch_rate_source, b.watch_rate, b.search_keywords 
FROM ( SELECT program_id, COUNT(*) AS post_count, SUM(comment_count) AS comment_count, SUM(metoo_count) AS metoo_count 
	FROM post
	GROUP BY program_id
) AS a LEFT OUTER JOIN tv_program b ON a.program_id = b.program_id

SELECT program_id, COUNT(*), COUNT(DISTINCT author_id), SUM(comment_count), SUM(metoo_count),
	SUM(liwc_swear), SUM(liwc_qmark), SUM(liwc_exclam), 
	SUM(liwc_smile), SUM(liwc_cry), SUM(liwc_love), SUM(liwc_positive),
	SUM(liwc_negative), SUM(liwc_anger), SUM(liwc_anxiety), SUM(liwc_sadness)
FROM post 
WHERE publish_date >= '2011-08-15 00:00:00' AND publish_date <= '2011-08-21 23:59:59'
GROUP BY program_id
