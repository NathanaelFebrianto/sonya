

SELECT id1, SUM(replyed_count_by_user2), SUM(retweeted_count_by_user2), SUM(mentioned_count_by_user2), 
SUM(positive_attitude_count_by_user2), SUM(negative_attitude_count_by_user2)
FROM relationship 
GROUP BY id1
HAVING id1 IN (SELECT id FROM USER)