# TODO: Add comment
# 
# Author: Louie
###############################################################################

setwd("D:/dev/workspace/beeblz/beeblz-twitter/R")

df_audiences = read.csv("twitter_audiences.csv")
df_audiences_by_tweet_type = read.csv("twitter_audiences_tweet_type.csv")
df_populars = read.csv("twitter_populars.csv")

colnames(df_audiences) <- c("user", "create_date", "a.total_tweet_count", "a.total_user_count", 
		"a.positive_tweet_count", "a.positive_user_count", "a.negative_tweet_count", "a.negative_user_count")
colnames(df_audiences_by_tweet_type) <- c("user", "tweet_type", "create_date", "a.total_tweet_count", "a.total_user_count", 
		"a.positive_tweet_count", "a.positive_user_count", "a.negative_tweet_count", "a.negative_user_count")
colnames(df_populars) <- c("user", "create_date", "p.total_tweet_count", "p.positive_tweet_count", "p.negative_tweet_count")

# Initialize df_all
df_all = NULL
df_all_tweet_type = NULL
df_all = merge(df_audiences, df_populars, by = c("user", "create_date"), all = FALSE)
df_all_tweet_type = merge(df_audiences_by_tweet_type, df_populars, by = c("user", "create_date"), all = FALSE)

CorrelationAll <- function(tweet.types) {
	
	cor_pos_test = cor.test(df_all$a.positive_tweet_count, df_all$p.positive_tweet_count)
	cor_neg_test = cor.test(df_all$a.positive_tweet_count, df_all$p.positive_tweet_count)
	
	cat("***** ALL *****", "\n")
	cat("> positive correlation", "\n")
	print(cor_pos_test)
	cat("> negative correlation ", "\n")
	print(cor_neg_test)
	
	for (tweet.type in tweet.types) {
		df_tweet_type = subset(df_all_tweet_type, 
				subset = (tweet_type == tweet.type))
		
		cor_pos_test_type = cor.test(df_tweet_type$a.positive_tweet_count, df_tweet_type$p.positive_tweet_count)
		cor_neg_test_type = cor.test(df_tweet_type$a.negative_tweet_count, df_tweet_type$p.negative_tweet_count)
		
		cat("***** ALL by Tweet type: ",  tweet.type, "*****", "\n")
		cat("> positive correlation", "\n")
		print(cor_pos_test_type)
		cat("> negative correlation ", "\n")
		print(cor_neg_test_type)	
	}

}

Correlation <- function(user_name) {
	df_user = subset(df_all, subset = (user == user_name))
	
	cor_pos = cor(df_user$a.positive_tweet_count, df_user$p.positive_tweet_count)	
	cor_neg = cor(df_user$a.negative_tweet_count, df_user$p.negative_tweet_count)
	cor_pos_test = cor.test(df_user$a.positive_tweet_count, df_user$p.positive_tweet_count)
	cor_neg_test = cor.test(df_user$a.negative_tweet_count, df_user$p.negative_tweet_count)
	
	cat("***** ", user_name, " *****", "\n")
	cat("> positive correlation == ", cor_pos, "\n")
	print(cor_pos_test)
	cat("> negative correlation == ", cor_neg, "\n")
	print(cor_neg_test)
}

CorrelationByTweetType <- function(user.name, tweet.types) {
	
#	user.name = "BarackObama"
#	tweet.types <- c("REPLY", "MENTION")	
	
	if (length(tweet.types) > 1) {
		df_user = subset(df_all_tweet_type, 
				subset = (user == user.name & (tweet_type == tweet.types[1] | tweet_type == tweet.types[2])))
	} else {
		df_user = subset(df_all_tweet_type, 
				subset = (user == user.name & tweet_type == tweet.types[1]))
	}
	
	nrow(df_user)
	
	cor_pos = cor(df_user$a.positive_tweet_count, df_user$p.positive_tweet_count)	
	cor_neg = cor(df_user$a.negative_tweet_count, df_user$p.negative_tweet_count)
	cor_pos_test = cor.test(df_user$a.positive_tweet_count, df_user$p.positive_tweet_count)
	cor_neg_test = cor.test(df_user$a.negative_tweet_count, df_user$p.negative_tweet_count)
	cat("***** ", user.name, " *****", "\n")
	cat("> tweet type == ", tweet.types, "\n")
	cat("> positive correlation == ", cor_pos, "\n")
	print(cor_pos_test)
	cat("> negative correlation == ", cor_neg, "\n")
	print(cor_neg_test)
	
}




###
# Execute
###

tweet.types <- c("REPLY", "MENTION", "RETWEET")
tweet.types1 <- c("REPLY", "MENTION")
tweet.types2 <- c("REPLY")
tweet.types3 <- c("MENTION")
tweet.types4 <- c("RETWEET")

CorrelationAll(tweet.types)

Correlation("aplusk")
#CorrelationByTweetType("aplusk", tweet.types1)
CorrelationByTweetType("aplusk", tweet.types2)
CorrelationByTweetType("aplusk", tweet.types3)
CorrelationByTweetType("aplusk", tweet.types4)

Correlation("BarackObama")
#CorrelationByTweetType("BarackObama", tweet.types1)
CorrelationByTweetType("BarackObama", tweet.types2)
CorrelationByTweetType("BarackObama", tweet.types3)
CorrelationByTweetType("BarackObama", tweet.types4)

Correlation("BBCBreaking")
#CorrelationByTweetType("BBCBreaking", tweet.types1)
CorrelationByTweetType("BBCBreaking", tweet.types2)
CorrelationByTweetType("BBCBreaking", tweet.types3)
CorrelationByTweetType("BBCBreaking", tweet.types4)

Correlation("BillGates")
#CorrelationByTweetType("BillGates", tweet.types1)
CorrelationByTweetType("BillGates", tweet.types2)
CorrelationByTweetType("BillGates", tweet.types3)
CorrelationByTweetType("BillGates", tweet.types4)

Correlation("britneyspears")
#CorrelationByTweetType("britneyspears", tweet.types1)
CorrelationByTweetType("britneyspears", tweet.types2)
CorrelationByTweetType("britneyspears", tweet.types3)
CorrelationByTweetType("britneyspears", tweet.types4)

Correlation("cnnbrk")
#CorrelationByTweetType("cnnbrk", tweet.types1)
CorrelationByTweetType("cnnbrk", tweet.types2)
CorrelationByTweetType("cnnbrk", tweet.types3)
CorrelationByTweetType("cnnbrk", tweet.types4)

Correlation("DalaiLama")
#CorrelationByTweetType("DalaiLama", tweet.types1)
CorrelationByTweetType("DalaiLama", tweet.types2)
CorrelationByTweetType("DalaiLama", tweet.types3)
CorrelationByTweetType("DalaiLama", tweet.types4)

Correlation("kingsthings")
#CorrelationByTweetType("kingsthings", tweet.types1)
CorrelationByTweetType("kingsthings", tweet.types2)
CorrelationByTweetType("kingsthings", tweet.types3)
CorrelationByTweetType("kingsthings", tweet.types4)

Correlation("ladygaga")
#CorrelationByTweetType("ladygaga", tweet.types1)
CorrelationByTweetType("ladygaga", tweet.types2)
CorrelationByTweetType("ladygaga", tweet.types3)
CorrelationByTweetType("ladygaga", tweet.types4)

Correlation("mashable")
#CorrelationByTweetType("mashable", tweet.types1)
CorrelationByTweetType("mashable", tweet.types2)
CorrelationByTweetType("mashable", tweet.types3)
CorrelationByTweetType("mashable", tweet.types4)

Correlation("Oprah")
#CorrelationByTweetType("Oprah", tweet.types1)
CorrelationByTweetType("Oprah", tweet.types2)
CorrelationByTweetType("Oprah", tweet.types3)
CorrelationByTweetType("Oprah", tweet.types4)

Correlation("realDonaldTrump")
#CorrelationByTweetType("realDonaldTrump", tweet.types1)
CorrelationByTweetType("realDonaldTrump", tweet.types2)
CorrelationByTweetType("realDonaldTrump", tweet.types3)
CorrelationByTweetType("realDonaldTrump", tweet.types4)

Correlation("TechCrunch")
#CorrelationByTweetType("TechCrunch", tweet.types1)
CorrelationByTweetType("TechCrunch", tweet.types2)
CorrelationByTweetType("TechCrunch", tweet.types3)
CorrelationByTweetType("TechCrunch", tweet.types4)




