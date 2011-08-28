/*******************************
    positive of audience
********************************/

/* positive 집계 : liwc_sad 제외 */
SELECT target_user, create_date, COUNT(id) AS positive_tweet_count, COUNT(DISTINCT USER) AS positive_user_count
FROM tweet
WHERE (liwc_posemo+liwc_posfeel+liwc_optim) > (liwc_negemo+liwc_anx+liwc_anger) 
AND target_user IN (SELECT id FROM USER)
GROUP BY target_user, create_date;


/*******************************
    negative of audience
********************************/

/* negative 집계 : liwc_sad 제외 */
SELECT target_user, create_date, COUNT(id) AS negative_tweet_count, COUNT(DISTINCT USER) AS negative_user_count
FROM tweet
WHERE (liwc_posemo+liwc_posfeel+liwc_optim) <= (liwc_negemo+liwc_anx+liwc_anger) 
AND ((liwc_negemo+liwc_anx+liwc_anger) > 0)
AND target_user IN (SELECT id FROM USER)
GROUP BY target_user, create_date;

/* negative tweet 상세 데이터 보기 */
SELECT id, USER, target_user, tweet_text, create_date, 
 liwc_posemo, liwc_posfeel, liwc_optim, liwc_negemo, liwc_anx, liwc_anger
FROM tweet
WHERE (liwc_posemo+liwc_posfeel+liwc_optim) <= (liwc_negemo+liwc_anx+liwc_anger) 
AND ((liwc_negemo+liwc_anx+liwc_anger) > 0)
AND target_user IN (SELECT id FROM USER);

/*******************************
    BarackObama 이상 데이터 제거
********************************/

/* 제외할 데이터 */
INSERT INTO tweet_exclude (
SELECT id, USER, target_user, create_date, tweet_text FROM tweet 
WHERE target_user = "BarackObama"
AND create_date = "2011-05-13 00:00:00" 
AND tweet_text = "RT @justinbieber: I think you're wrong. pretty sure President @BarackObama will keep this promise. #payitforward - http://bit.ly/jocrJy"
)

INSERT INTO tweet_exclude (
SELECT id, USER, target_user, create_date, tweet_text FROM tweet 
WHERE target_user = "BarackObama"
AND create_date = "2011-06-29 00:00:00" 
AND tweet_text = "RT @justinbieber: glad to help President @BarackObama keep his promise. glad he could be there too. #SWAG  http://twitpic.com/5ie0u1"
)

INSERT INTO tweet_exclude (
SELECT id, USER, target_user, create_date, tweet_text FROM tweet 
WHERE target_user = "BarackObama"
AND create_date = "2011-06-30 00:00:00" 
AND tweet_text = "RT @justinbieber: glad to help President @BarackObama keep his promise. glad he could be there too. #SWAG  http://twitpic.com/5ie0u1"
)

INSERT INTO tweet_exclude (
SELECT id, USER, target_user, create_date, tweet_text FROM tweet 
WHERE target_user = "BarackObama"
AND create_date = "2011-07-04 00:00:00" 
AND tweet_text = "RT @foxnewspolitics: BREAKING NEWS: President @BarackObama assassinated, 2 gunshot wounds have proved too much. It's a sad 4th for #america. #obamadead RIP"
)

/* positive 집계 */
SELECT target_user, create_date, COUNT(id) AS positive_tweet_count, COUNT(DISTINCT USER) AS positive_user_count
FROM tweet
WHERE (liwc_posemo+liwc_posfeel+liwc_optim) > (liwc_negemo+liwc_anx+liwc_anger) 
AND target_user = "BarackObama"
AND id NOT IN (SELECT tweet_id FROM tweet_exclude)
GROUP BY target_user, create_date;

/* negative 집계 */
SELECT target_user, create_date, COUNT(id) AS negative_tweet_count, COUNT(DISTINCT USER) AS negative_user_count
FROM tweet
WHERE (liwc_posemo+liwc_posfeel+liwc_optim) <= (liwc_negemo+liwc_anx+liwc_anger) 
AND ((liwc_negemo+liwc_anx+liwc_anger) > 0)
AND target_user = "BarackObama"
AND id NOT IN (SELECT tweet_id FROM tweet_exclude)
GROUP BY target_user, create_date;

/* total */
SELECT target_user, create_date, COUNT(id) AS total_tweet_count, COUNT(DISTINCT USER) AS total_user_count
FROM tweet
WHERE target_user = "BarackObama"
AND id NOT IN (SELECT tweet_id FROM tweet_exclude)
GROUP BY target_user, create_date;

/*******************************
      total of audience
********************************/

SELECT target_user, create_date, COUNT(id) AS total_tweet_count, COUNT(DISTINCT USER) AS total_user_count
FROM tweet
WHERE target_user IN (SELECT id FROM USER)
GROUP BY target_user, create_date;


/*******************************************************
      popular user의 positive & negative
********************************************************/
/* positive 집계 : liwc_sad 제외 */
SELECT USER, create_date, COUNT(id) AS positive_tweet_count
FROM tweet
WHERE (liwc_posemo+liwc_posfeel+liwc_optim) > (liwc_negemo+liwc_anx+liwc_anger) 
AND USER IN (SELECT id FROM USER)
GROUP BY USER, create_date;

/* negative 집계 : liwc_sad 포함  */
SELECT USER, create_date, COUNT(id) AS negative_tweet_count
FROM tweet
WHERE (liwc_posemo+liwc_posfeel+liwc_optim) <= (liwc_negemo+liwc_anx+liwc_anger+liwc_sad) 
AND ((liwc_negemo+liwc_anx+liwc_anger+liwc_sad) > 0)
AND USER IN (SELECT id FROM USER)
GROUP BY USER, create_date;

SELECT USER, create_date, COUNT(id) AS total_tweet_count
FROM tweet
WHERE USER IN (SELECT id FROM USER)
GROUP BY USER, create_date;


/*******************************************************
  relationship
********************************************************/

SELECT id1, id2, replyed_count_by_user2, retweeted_count_by_user2, mentioned_count_by_user2, 
liwc_posemo, liwc_posfeel, liwc_optim, liwc_negemo, liwc_anx, liwc_anger
FROM relationship 
WHERE id1="BarackObama" OR id2="BarackObama"


/*******************************************************
  Popular User에 대한 tweet, replied, retweeted, mentioned 수
********************************************************/
/**
 * 1. Popular User에 대한 tweet, replied, retweeted, mentioned 수 및 LIWC 합계 
 */
SELECT 
 id1, 
 COUNT(DISTINCT id2) AS audience_count, 
 SUM(replyed_count_by_user2) AS replied_tweet_count, 
 SUM(retweeted_count_by_user2) AS retweeted_tweet_count, 
 SUM(mentioned_count_by_user2) AS mentioned_tweet_count, 
 (SUM(liwc_posemo) + SUM(liwc_posfeel) + SUM(liwc_optim)) AS liwc_positive,
 (SUM(liwc_negemo) + SUM(liwc_anx) + SUM(liwc_anger)) AS liwc_negative
FROM relationship 
WHERE id1 IN (SELECT id FROM USER)
GROUP BY id1;


/**
 * 2. Popular User에 대한 tweet, replied, retweeted, mentioned한 유저수 및 LIWC 유저수 
 */

/* 1.replied 유저수 */
SELECT id1, COUNT(DISTINCT id2) AS replied_user_count
FROM relationship 
WHERE replyed_count_by_user2 > 0 AND id1 IN (SELECT id FROM USER)
GROUP BY id1;

/* 2.retweeted 유저수 */
SELECT id1, COUNT(DISTINCT id2) AS retweeted_user_count
FROM relationship 
WHERE retweeted_count_by_user2 > 0 AND id1 IN (SELECT id FROM USER)
GROUP BY id1;

/* 3.mentioned 유저수 */
SELECT id1, COUNT(DISTINCT id2) AS mentioned_user_count
FROM relationship 
WHERE mentioned_count_by_user2 > 0 AND id1 IN (SELECT id FROM USER)
GROUP BY id1;

/* 4.postive 유저수 */
SELECT id1, COUNT(DISTINCT id2) AS positive_user_count
FROM relationship 
WHERE (liwc_posemo+liwc_posfeel+liwc_optim) > (liwc_negemo+liwc_anx+liwc_anger) 
 AND id1 IN (SELECT id FROM USER)
GROUP BY id1;

/* 5.negative 유저수 */
SELECT id1, COUNT(DISTINCT id2) AS negative_user_count
FROM relationship 
WHERE (liwc_posemo+liwc_posfeel+liwc_optim) <= (liwc_negemo+liwc_anx+liwc_anger) 
 AND ((liwc_negemo+liwc_anx+liwc_anger) > 0)
 AND id1 IN (SELECT id FROM USER)
GROUP BY id1;

