# Analyzes the correlation between me2day and offline watch rate.
# 
# Author: Younggue Bae
###############################################################################


setwd("D:/workspace/social-buzz/data/analysis")

df.me2day.rank = read.csv("tv_program_rank.csv")
df.watch.rate = read.csv("tv_program_watch_rate.csv")

df.all = NULL
df.all = merge(df.me2day.rank, df.watch.rate, by = c("program_id", "start_date", "end_date"), all = FALSE)

df.programs = subset(df.all, subset =  (
   program_id == "kbs2_spy" |
   program_id == "mbc_gyebaek" |
   program_id == "mbc_fallinlove" |
   program_id == "sbs_baekdongsoo" |
   program_id == "sbs_boss" |
   program_id == "kbs2_gagcon" |
   program_id == "kbs2_happysunday_1bak2il" |
   program_id == "sbs_happytogether" |
   program_id == "mbc_challenge" |
   program_id == "mbc_wedding" |
   program_id == "mbc_three" |
   program_id == "mbc_sundaynight_nagasoo" |
   program_id == "sbs_strongheart" |
   program_id == "sbs_starking" |
   program_id == "sbs_newsunday"))				   
				   
# EDA and Plotting
plot(df.programs$post_count, df.programs$watch_rate, col = "red")
plot(df.programs$post_user_count, df.programs$watch_rate, col = "red")
plot(df.programs$metoo_count, df.programs$watch_rate, col = "red")
plot(df.programs$comment_count, df.programs$watch_rate, col = "red")


# Correlation
cor.post.count = cor(df.all$post_count, df.all$watch_rate)	
cat("cor.post.count == ", cor.post.count, "\n")

cor.post.user.count = cor(df.all$post_user_count, df.all$watch_rate)	
cat("cor.post.user.count == ", cor.post.user.count, "\n")

cor.all.count = cor(df.all$post_count + df.all$metoo_count + df.all$comment_count, df.all$watch_rate)	
cat("cor.all.count  == ", cor.all.count , "\n")

