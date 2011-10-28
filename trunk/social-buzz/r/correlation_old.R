# Analyzes the correlation between me2day and offline watch rate.
# 
# Author: Younggue Bae
###############################################################################


setwd("D:/workspace/social-buzz/data/analysis")

df.me2day.rank = read.csv("tv_program_rank.csv")
df.watch.rate = read.csv("tv_program_watch_rate.csv")

df.all = NULL
df.all = merge(df.me2day.rank, df.watch.rate, by = c("program_id", "start_date", "end_date"), all = FALSE)

df.programs = subset(df.all, 
	select = c("post_count" 
			, "comment_count" 
			, "post_user_count"
			, "metoo_count"
			, "positive_post_count"
			, "negative_post_count"
			, "positive_post_user_count"
			, "negative_post_user_count"
			, "watch_rate"),
	subset =  (
	   program_id == "kbs1_greatking" |	
	   program_id == "kbs_homewomen" |	
	   program_id == "kbs2_princess" |	
	   program_id == "kbs2_spy" | 
	   program_id == "kbs2_ojakkyo" |	   
	   program_id == "mbc_gyebaek" |
	   program_id == "mbc_fallinlove" |
	   program_id == "mbc_urpretty" |
	   program_id == "mbc_thousand" |
	   program_id == "sbs_besideme" |
	   program_id == "sbs_dangsin" |
	   program_id == "sbs_baekdongsoo" |
	   program_id == "sbs_boss" |
	   program_id == "sbs_scent" |
	   program_id == "kbs2_gagcon" |
	   program_id == "kbs2_happysunday_1bak2il" |
	   program_id == "kbs2_happysunday_men" |
	   program_id == "sbs_happytogether" |
	   program_id == "mbc_challenge" |
	   program_id == "mbc_wedding" |
	   program_id == "mbc_three" |
	   program_id == "mbc_sundaynight_house" |
	   program_id == "mbc_sundaynight_nagasoo" |
	   program_id == "sbs_strongheart" |
	   program_id == "sbs_starking" |
	   program_id == "sbs_newsunday"
	   ))				   

# Test normality
shapiro.test(df.programs$watch_rate)
shapiro.test(df.programs$post_count)

# EDA and Plotting
pairs(df.programs)
boxplot(df.programs)
plot(df.programs$post_count, df.programs$watch_rate, col = "red")
plot(df.programs$post_user_count, df.programs$watch_rate, col = "red")
plot(df.programs$metoo_count, df.programs$watch_rate, col = "red")
plot(df.programs$comment_count, df.programs$watch_rate, col = "red")
plot(df.programs$positive_post_count, df.programs$watch_rate, col = "red")
plot(df.programs$positive_post_user_count, df.programs$watch_rate, col = "red")
plot(df.programs$negative_post_count, df.programs$watch_rate, col = "red")
plot(df.programs$negative_post_user_count, df.programs$watch_rate, col = "red")

plot(0.5*df.programs$post_count + 0.4*df.programs$metoo_count + 0.1*df.programs$comment_count, 
		df.programs$watch_rate, col = "red")

# Regression analysis
out = lm(df.programs$watch_rate ~ df.programs$post_count, data = df.programs)
par(mfrow = c(2,2))
plot(out)
qqnorm(resid(out))
qqline(resid(out))
shapiro.test(resid(out))


attach(df.programs)
out0 = lm(watch_rate ~ post_user_count+comment_count+metoo_count, data = df.programs)
anova(out0)
summary(out0)

out1 = lm(watch_rate ~ negative_post_user_count+positive_post_count+negative_post_count+post_user_count+positive_post_user_count+comment_count+post_count+metoo_count, data = df.programs)
anova(out1)

out2 = lm(watch_rate ~ negative_post_user_count+positive_post_count+negative_post_count+post_user_count+positive_post_user_count+comment_count+metoo_count, data = df.programs)
anova(out2, out1)

out3 = lm(watch_rate ~ negative_post_user_count+positive_post_count+negative_post_count+post_user_count+positive_post_user_count+metoo_count, data = df.programs)
anova(out3, out2, out1)

out4 = lm(watch_rate ~ negative_post_user_count+positive_post_count+post_user_count+positive_post_user_count+metoo_count, data = df.programs)
anova(out4, out3, out2, out1)

out5 = lm(watch_rate ~ negative_post_user_count+post_user_count+positive_post_user_count+metoo_count, data = df.programs)
anova(out5, out4, out3, out2, out1)

out6 = lm(watch_rate ~ negative_post_user_count+positive_post_user_count+metoo_count, data = df.programs)

out7 = lm(watch_rate ~ negative_post_user_count+post_user_count+metoo_count, data = df.programs)

out8 = lm(watch_rate ~ post_user_count+metoo_count, data = df.programs)

anova(out5)
summary(out8)
summary(out7)
summary(out6)
summary(out5)
summary(out4)
summary(out3)
summary(out2)
summary(out1)
par(mfrow = c(2,2))
plot(out1)



trees
# Correlation
cor.post.count = cor(df.all$post_count, df.all$watch_rate)	
cat("cor.post.count == ", cor.post.count, "\n")
cor.test(df.all$post_count, df.all$watch_rate)

cor.post.user.count = cor(df.all$post_user_count, df.all$watch_rate)	
cat("cor.post.user.count == ", cor.post.user.count, "\n")
cor.test(df.all$post_user_count, df.all$watch_rate)

cor.positive.post.count = cor(df.all$positive_post_count - df.all$negative_post_count, df.all$watch_rate)	
cat("cor.positive.post.count == ", cor.positive.post.count, "\n")
cor.test(df.all$positive_post_count - df.all$negative_post_count, df.all$watch_rate)

cor.positive.post.count = cor(df.all$positive_post_count - df.all$negative_post_count, df.all$watch_rate)	
cat("cor.positive.post.count == ", cor.positive.post.count, "\n")
cor.test(df.all$positive_post_count - df.all$negative_post_count, df.all$watch_rate)

cor.all.count = cor(df.all$post_count + df.all$metoo_count + df.all$comment_count, df.all$watch_rate)	
cat("cor.all.count  == ", cor.all.count , "\n")
cor.test(df.all$post_count + df.all$metoo_count + df.all$comment_count, df.all$watch_rate)

