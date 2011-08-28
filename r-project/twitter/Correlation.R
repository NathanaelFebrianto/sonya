# TODO: Add comment
# 
# Author: Louie
###############################################################################


df_audiences = read.csv("twitter_audiences.csv")
df_populars = read.csv("twitter_populars.csv")
colnames(df_audiences) <- c("user", "create_date", "a.total_tweet_count", "a.total_user_count", 
		"a.positive_tweet_count", "a.positive_user_count", "a.negative_tweet_count", "a.negative_user_count")
colnames(df_populars) <- c("user", "create_date", "p.total_tweet_count", "p.positive_tweet_count", "p.negative_tweet_count")

# Initialize df_all
df_all = NULL
df_all = merge(df_audiences, df_populars, by = c("user", "create_date"), all = FALSE)

Correlation <- function (user_name) {
	df_user = subset(df_all, subset = (user == user_name))
	
	cor_pos = cor(df_user$a.positive_tweet_count, df_user$p.positive_tweet_count)	
	cor_neg = cor(df_user$a.negative_tweet_count, df_user$p.negative_tweet_count)
	cat(user_name, "\n")
	cat("positive correlation == ", cor_pos, "\n")
	cat("negative correlation == ", cor_neg, "\n")
}

Correlation("BarackObama")
Correlation("Oprah")
Correlation("aplusk")
Correlation("BBCBreaking")
Correlation("BillGates")
Correlation("britneyspears")
Correlation("cnnbrk")
Correlation("DalaiLama")
Correlation("kingsthings")
Correlation("ladygaga")
Correlation("mashable")
Correlation("realDonaldTrump")
Correlation("TechCrunch")


