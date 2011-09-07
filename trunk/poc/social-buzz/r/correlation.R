# Analyzes the correlation between me2day and offline watch rate.
# 
# Author: Younggue Bae
###############################################################################


setwd("D:/workspace/social-buzz/data/analysis")

df.me2day.rank = read.csv("tv_program_rank.csv")
df.watch.rate = read.csv("tv_program_watch_rate.csv")

df.all = NULL
df.all = merge(df.me2day.rank, df.watch.rate, by = c("program_id", "start_date", "end_date"), all = FALSE)

GetPrograms <- function(categories, air.cycles) {
	df.programs = subset(df.all, 
			select = c("post_count" 
					, "comment_count" 
					, "metoo_count"
					, "post_user_count"
					, "positive_post_count"
					, "negative_post_count"
					, "positive_post_user_count"
					, "negative_post_user_count"
					, "positive_post_rate"
					, "negative_post_rate"
					, "positive_post_user_rate"
					, "negative_post_user_rate"
					, "negative_positive_post_rate"
					, "negative_positive_post_user_rate"
					, "watch_rate"),
			subset =  (
						(category.x == categories[1] | 
						 category.x == categories[2]) &	
						(air_cycle.x == air.cycles[1] |
						 air_cycle.x == air.cycles[2] |
						 air_cycle.x == air.cycles[3] |
						 air_cycle.x == air.cycles[4] |
						 air_cycle.x == air.cycles[5] |
						 air_cycle.x == air.cycles[6] |
						 air_cycle.x == air.cycles[7] |
						 air_cycle.x == air.cycles[8])))	
 
	cat("df.programs row count == ", nrow(df.programs), "\n")
	return (df.programs)
}


PlotPairs <- function(df.programs) {
	attach(df.programs)
	# test normality
	shapiro.test(watch_rate)
	shapiro.test(post_count)
	
	# pairs
	pairs(df.programs, col = "red")
	
	detach(df.programs)
}


CorrelationAnalysis <- function(df.program) {
	attach(df.programs)
	
	out1 = cor(post_count, watch_rate, use = "pairwise.complete.obs")	
	cat("correlation [post_count <-> watch_rate]  == ", out1, "\n")
	out1 = cor.test(post_count, watch_rate)
	names(out1)
	cat("  p.value  == ", out1$p.value, "\n")
	
	out2 = cor(comment_count, watch_rate, use = "pairwise.complete.obs")	
	cat("correlation [comment_count <-> watch_rate]  == ", out2, "\n")
	out2 = cor.test(comment_count, watch_rate)
	cat("  p.value  == ", out2$p.value, "\n")
	
	out3 = cor(metoo_count, watch_rate, use = "pairwise.complete.obs")	
	cat("correlation [metoo_count <-> watch_rate]  == ", out3, "\n")
	out3 = cor.test(metoo_count, watch_rate)
	cat("  p.value  == ", out3$p.value, "\n")
	
	out4 = cor(post_user_count, watch_rate, use = "pairwise.complete.obs")	
	cat("correlation [post_user_count <-> watch_rate]  == ", out4, "\n")
	out4 = cor.test(post_user_count, watch_rate)
	cat("  p.value  == ", out4$p.value, "\n")
	
	out5 = cor(positive_post_count, watch_rate, use = "pairwise.complete.obs")	
	cat("correlation [positive_post_count <-> watch_rate]  == ", out5, "\n")
	out5 = cor.test(positive_post_count, watch_rate)
	cat("  p.value  == ", out5$p.value, "\n")
	
	out6 = cor(negative_post_count, watch_rate, use = "pairwise.complete.obs")	
	out6 = cat("correlation [negative_post_count <-> watch_rate]  == ", out6, "\n")
	cor.test(negative_post_count, watch_rate)
	cat("  p.value  == ", out6$p.value, "\n")
	
	out7 = cor(positive_post_user_count, watch_rate, use = "pairwise.complete.obs")	
	cat("correlation [positive_post_user_count <-> watch_rate]  == ", out7, "\n")
	out7 = cor.test(positive_post_user_count, watch_rate)
	cat("  p.value  == ", out7$p.value, "\n")
	
	out8 = cor(negative_post_user_count, watch_rate, use = "pairwise.complete.obs")	
	cat("correlation [negative_post_user_count <-> watch_rate]  == ", out8, "\n")
	out8 = cor.test(negative_post_user_count, watch_rate)
	cat("  p.value  == ", out8$p.value, "\n")
	
	detach(df.programs)
}

RegressionAnalysis <- function(df.program) {
	
	cat("regression analysis....................", "\n")
	attach(df.programs)
	
	cat(">>>>>> lm(watch_rate ~ ., data = df.programs)", "\n")
	out0 = lm(watch_rate ~ ., data = df.programs)
	print(anova(out0))
	
	cat(">>>>>> lm(watch_rate ~ comment_count+metoo_count+post_count, data = df.programs)", "\n")
	out1 = lm(watch_rate ~ comment_count+metoo_count+post_count, data = df.programs)
	print(anova(out1))
	print(summary(out1))
	
	par(mfrow = c(2,2))
	plot(out1)
	#qqnorm(resid(out1))
	#qqline(resid(out1))
	shapiro.test(resid(out1))
	
	detach(df.programs)
}


###
# Execute
###
categories <- c(
#		"drama"
		"entertain"
)

air.cycles <- c(
		"SAT,SUN"
		, "SUN"
		, "WED,THU"
		, "MON,TUE"
		, "MON~FRI"
		, "SAT"
		, "THU"
		, "TUE"
)


df.programs = GetPrograms(categories, air.cycles)

df.subset = subset(df.programs, 
		select = c("post_count" 
				, "comment_count" 
				, "metoo_count"
				, "post_user_count"
#				, "positive_post_count"
#				, "negative_post_count"
				, "positive_post_user_count"
				, "negative_post_user_count"
#				, "positive_post_rate"
#				, "negative_post_rate"
				, "positive_post_user_rate"
				, "negative_post_user_rate"
#				, "negative_positive_post_rate"
#				, "negative_positive_post_user_rate"
				, "watch_rate"))
PlotPairs(df.subset)

boxplot(df.programs)
plot(df.programs$post_count, df.programs$watch_rate, col = "red")
plot(df.programs$comment_count, df.programs$watch_rate, col = "red")
plot(df.programs$metoo_count, df.programs$watch_rate, col = "red")
plot(df.programs$post_user_count, df.programs$watch_rate, col = "red")
plot(df.programs$positive_post_count, df.programs$watch_rate, col = "red")
plot(df.programs$negative_post_count, df.programs$watch_rate, col = "red")
plot(df.programs$positive_post_user_count, df.programs$watch_rate, col = "red")
plot(df.programs$negative_post_user_count, df.programs$watch_rate, col = "red")
plot(df.programs$positive_post_rate, df.programs$watch_rate, col = "red")
plot(df.programs$negative_post_rate, df.programs$watch_rate, col = "red")
plot(df.programs$positive_post_user_rate, df.programs$watch_rate, col = "red")
plot(df.programs$negative_post_user_rate, df.programs$watch_rate, col = "red")
plot(df.programs$negative_positive_post_rate, df.programs$watch_rate, col = "red")
plot(df.programs$negative_positive_post_user_rate, df.programs$watch_rate, col = "red")

# correlation analysis
CorrelationAnalysis(df.programs)

# regression analysis
RegressionAnalysis(df.programs)


