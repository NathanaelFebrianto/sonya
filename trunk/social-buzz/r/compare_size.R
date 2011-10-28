# Compare size between me2day and twitter.
# 
# Author: Younggue Bae
###############################################################################


setwd("D:/dev/workspace/social-buzz/data/analysis")

df.post.weekly = read.csv("tv_program_rank.csv")


GetPostWeekly <- function(sns.site, durations, nations) {
	
	df.subset = subset(df.post.weekly, 
			select = c("program_id"
					, "post_count" 
					, "comment_count" 
					, "metoo_count"
					, "post_user_count"),
			subset =  (
						site == sns.site &
						(paste(start_date, "-", end_date, sep = "") == durations[1] |
							paste(start_date, "-", end_date, sep = "") == durations[2] |
							paste(start_date, "-", end_date, sep = "") == durations[3] |
							paste(start_date, "-", end_date, sep = "") == durations[4] |
							paste(start_date, "-", end_date, sep = "") == durations[5] |
							paste(start_date, "-", end_date, sep = "") == durations[6]) &
						(nation == nations[1] | nation == nations[2])))
	
	cat("df.post.weekly subset row count == ", nrow(df.subset), "\n")
	return (df.subset)
}

durations <- c(
		"20110815-20110821"
		, "20110822-20110828"
		, "20110905-20110911"
		, "20110919-20110925"
)

df.post.weekly.me2day = GetPostWeekly("me2day", durations, c("KO"))
df.post.weekly.twitter.ko = GetPostWeekly("twitter", durations, c("KO"))
df.post.weekly.twitter.us = GetPostWeekly("twitter", durations, c("US"))

boxplot(df.post.weekly.me2day$post_count, df.post.weekly.twitter.ko$post_count, names = c("Me2day", "Twitter"), col = "lightgreen")
boxplot(df.post.weekly.twitter.ko$post_count, df.post.weekly.twitter.us$post_count, names = c("Twitter Korea", "Twitter Global"), col = "lightgreen")

summary(df.post.weekly.me2day$post_count)
summary(df.post.weekly.twitter.ko$post_count)
summary(df.post.weekly.twitter.us$post_count)