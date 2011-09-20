# Analyzes the correlation between me2day and offline watch rate.
# 
# Author: Younggue Bae
###############################################################################


setwd("D:/workspace/social-buzz/data/analysis")

df.sns.rank = read.csv("tv_program_rank.csv")
df.watch.rate = read.csv("tv_program_watch_rate.csv")

df.all = NULL
df.all = merge(df.sns.rank, df.watch.rate, by = c("program_id", "start_date", "end_date"), all = FALSE)

categories.all <- c(
		"drama"
		, "entertain"
		, ""
)

air.cycles.all <- c(
		"SAT,SUN"
		, "SUN"
		, "WED,THU"
		, "MON,TUE"
		, "MON~FRI"
		, "SAT"
		, "THU"
		, "TUE"
		, "WED"
		, "TUE,WED"
)

GetPrograms <- function(durations, nations, categories, air.cycles) {
	
	df.programs = subset(df.all, 
			select = c("program_id"
					, "post_count" 
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
					, "watch_rate"
					, "rank.y"),
			subset =  (
						(paste(start_date, "-", end_date, sep = "") == durations[1] |
						 paste(start_date, "-", end_date, sep = "") == durations[2] |
						 paste(start_date, "-", end_date, sep = "") == durations[3] |
						 paste(start_date, "-", end_date, sep = "") == durations[4] |
						 paste(start_date, "-", end_date, sep = "") == durations[5] |
						 paste(start_date, "-", end_date, sep = "") == durations[6]) &
					    (nation.x == nations[1] | nation.x == nations[2]) &
						(category.x == categories[1] | 
						 category.x == categories[2] | 
						 category.x == categories[3]) &	
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


GetSignificantPrograms <- function(durations, nations) {
	df.programs = subset(df.all, 
			select = c("program_id"
					, "post_count" 
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
					, "watch_rate"
					, "rank.y"),
			subset =  (
						(paste(start_date, "-", end_date, sep = "") == durations[1] |
							paste(start_date, "-", end_date, sep = "") == durations[2] |
							paste(start_date, "-", end_date, sep = "") == durations[3] |
							paste(start_date, "-", end_date, sep = "") == durations[4] |
							paste(start_date, "-", end_date, sep = "") == durations[5] |
							paste(start_date, "-", end_date, sep = "") == durations[6]) &
						(nation.x == nations[1] | nation.x == nations[2]) &	
						(program_id != "kbs2_ojakkyo" &
						 program_id != "kbs_homewomen" &
						 program_id != "kbs1_greatking" &
						 program_id != "kbs2_happysunday_men" &
						 program_id != "mbc_fallinlove" &
						 program_id != "mbc_sundaynight_house" &
						 program_id != "kbs2_spy")))
	
	cat("df.programs row count == ", nrow(df.programs), "\n")
	return (df.programs)
}

GetAllPrograms <- function(durations, nations) {

	df.programs = GetPrograms(durations, nations, categories.all, air.cycles.all)
	
	return (df.programs)
} 

GetDramas <- function(durations, nations) {
	
	categories <- c(
			"drama"
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
	df.programs = GetPrograms(durations, nations, categories, air.cycles)
	
	return (df.programs)
} 

GetEntertains <- function(durations, nations) {
	
	categories <- c(
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
	df.programs = GetPrograms(durations, nations, categories, air.cycles)
	
	return (df.programs)
} 

GetMonToThuDramas <- function(durations, nations) {

	categories <- c(
			"drama"
	)
	
	air.cycles <- c(
			"WED,THU"
			, "MON,TUE"
	)
	df.programs = GetPrograms(durations, nations, categories, air.cycles)
	
	return (df.programs)
}

PlotPairs <- function(df.programs) {
	attach(df.programs)
	# test normality
	shapiro.test(watch_rate)
	shapiro.test(post_count)
	
	# pairs
	pairs(df.programs, col = "blue")
	
	detach(df.programs)
}


PearsonCorrelationAnalysis <- function(df.programs) {
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
	out6 = cor.test(negative_post_count, watch_rate)
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

SpearmanCorrelationAnalysis <- function(df.programs) {
	attach(df.programs)
	
	out1 = cor(post_count, rank.y, use = "pairwise.complete.obs", method = "spearman")	
	cat("correlation [post_count <-> watch_rate]  == ", out1, "\n")
	out1 = cor.test(post_count, watch_rate)
	names(out1)
	cat("  p.value  == ", out1$p.value, "\n")
	
	out2 = cor(comment_count, rank.y, use = "pairwise.complete.obs", method = "spearman")	
	cat("correlation [comment_count <-> watch_rate]  == ", out2, "\n")
	out2 = cor.test(comment_count, watch_rate)
	cat("  p.value  == ", out2$p.value, "\n")
	
	out3 = cor(metoo_count, rank.y, use = "pairwise.complete.obs", method = "spearman")	
	cat("correlation [metoo_count <-> watch_rate]  == ", out3, "\n")
	out3 = cor.test(metoo_count, watch_rate)
	cat("  p.value  == ", out3$p.value, "\n")
	
	out4 = cor(post_user_count, rank.y, use = "pairwise.complete.obs", method = "spearman")	
	cat("correlation [post_user_count <-> watch_rate]  == ", out4, "\n")
	out4 = cor.test(post_user_count, watch_rate)
	cat("  p.value  == ", out4$p.value, "\n")
	
	out5 = cor(positive_post_count, rank.y, use = "pairwise.complete.obs", method = "spearman")	
	cat("correlation [positive_post_count <-> watch_rate]  == ", out5, "\n")
	out5 = cor.test(positive_post_count, watch_rate)
	cat("  p.value  == ", out5$p.value, "\n")
	
	out6 = cor(negative_post_count, rank.y, use = "pairwise.complete.obs", method = "spearman")	
	out6 = cat("correlation [negative_post_count <-> watch_rate]  == ", out6, "\n")
	out6 = cor.test(negative_post_count, watch_rate)
	cat("  p.value  == ", out6$p.value, "\n")
	
	out7 = cor(positive_post_user_count, rank.y, use = "pairwise.complete.obs", method = "spearman")	
	cat("correlation [positive_post_user_count <-> watch_rate]  == ", out7, "\n")
	out7 = cor.test(positive_post_user_count, watch_rate)
	cat("  p.value  == ", out7$p.value, "\n")
	
	out8 = cor(negative_post_user_count, rank.y, use = "pairwise.complete.obs", method = "spearman")	
	cat("correlation [negative_post_user_count <-> watch_rate]  == ", out8, "\n")
	out8 = cor.test(negative_post_user_count, watch_rate)
	cat("  p.value  == ", out8$p.value, "\n")
	
	detach(df.programs)
}

RegressionAnalysis <- function(df.programs) {
	
	cat("regression analysis....................", "\n")
	attach(df.programs)
	
	cat(">>>>>> lm(watch_rate ~ ., data = df.programs)", "\n")
	out0 = lm(watch_rate ~ ., data = df.programs)
	print(anova(out0))
	
	cat(">>>>>> lm(watch_rate ~ +metoo_count+post_count, data = df.programs)", "\n")
	out1 = lm(watch_rate ~ metoo_count+post_count, data = df.programs)
	print(anova(out1))
	print(summary(out1))
	
	par(mfrow = c(2,2))
	plot(out1)
#	qqnorm(resid(out1))
#	qqline(resid(out1))
	shapiro.test(resid(out1))
	
	detach(df.programs)
}

RegressionAnalysis1 <- function(df.programs) {
	
	cat("regression analysis1....................", "\n")
	attach(df.programs)
	
	cat(">>>>>> lm(rank ~ ., data = df.programs)", "\n")
	out0 = lm(rank.y ~ ., data = df.programs)
	print(anova(out0))
	
	cat(">>>>>> lm(rank ~ +metoo_count+post_count, data = df.programs)", "\n")
	out1 = lm(rank.y ~ metoo_count+post_count, data = df.programs)
	print(anova(out1))
	print(summary(out1))
	
	par(mfrow = c(2,2))
	plot(out1)
#	qqnorm(resid(out1))
#	qqline(resid(out1))
	shapiro.test(resid(out1))
	
	detach(df.programs)
}

PlotWatchRateAndPostCount <- function(df.programs) {
	library(calibrate)
	
	q.watch.rate = quantile(df.programs$watch_rate)
	q.post.count = quantile(df.programs$post_count)
	
	df.programs.q1 = subset(df.programs, 
			subset =  (watch_rate < q.watch.rate["0%"] & post_count < q.post.count["0%"]))
	
	df.programs.q2 = subset(df.programs, 
			subset =  (watch_rate >= q.watch.rate["0%"] & watch_rate < q.watch.rate["25%"] 
						& post_count >=  q.post.count["0%"] & post_count < q.post.count["25%"]))
	
	df.programs.q3 = subset(df.programs, 
			subset =  (watch_rate >= q.watch.rate["25%"] & watch_rate < q.watch.rate["50%"] 
						& post_count >=  q.post.count["25%"] & post_count < q.post.count["50%"]))
	
	df.programs.q4 = subset(df.programs, 
			subset =  (watch_rate >= q.watch.rate["50%"] & watch_rate < q.watch.rate["75%"] 
						& post_count >=  q.post.count["50%"] & post_count < q.post.count["75%"]))
	
	df.programs.q5 = subset(df.programs, 
			subset =  (watch_rate >= q.watch.rate["75%"] & post_count >=  q.post.count["75%"]))
	
	plot(df.programs$post_count, df.programs$watch_rate, col = "blue")
	textxy(df.programs$post_count, df.programs$watch_rate, df.programs$program_id)
	abline(lm(df.programs$watch_rate ~ df.programs$post_count), col = "red")
	
}

PlotWatchRankAndPostCount <- function(df.programs) {
	library(calibrate)

	plot(df.programs$post_count, df.programs$rank.y, col = "blue")
	textxy(df.programs$post_count, df.programs$rank.y, df.programs$program_id)
	abline(lm(df.programs$rank.y ~ df.programs$post_count), col = "red")
	
}

GetDataForPlotPairs <- function (df.old) {
	
	df.new = subset(df.old, 
			select = c("post_count" 
					, "comment_count" 
					, "metoo_count"
					, "post_user_count"
					, "positive_post_count"
					, "negative_post_count"
					, "positive_post_user_count"
					, "negative_post_user_count"
#				, "positive_post_rate"
#				, "negative_post_rate"
#				, "positive_post_user_rate"
#				, "negative_post_user_rate"
#				, "negative_positive_post_rate"
#				, "negative_positive_post_user_rate"
					, "watch_rate"
					, "rank.y"))
	return (df.new)
}



###
# Execute
###

durations.all <- c(
		"20110815-20110821"
		, "20110822-20110828"
		, "20110905-20110911"
)

nations.all <- c("KO", "US")

durations1 <- c(
		"20110815-20110821"
		, "20110822-20110828"
)

nations.ko <- c("KO")

df.programs.all = GetAllPrograms(durations.all, nations.all)
df.programs.drama = GetDramas(durations1, nations.ko)
df.programs.entain = GetEntertains(durations1, nations.ko)
df.programs.drama.montothu = GetMonToThuDramas(durations1, nations.ko)
df.programs.sig = GetSignificantPrograms(durations1, nations.ko)

#df.programs <- df.programs.all
#df.programs <- df.programs.drama
#df.programs <- df.programs.entain
#df.programs <- df.programs.drama.montothu
df.programs <- df.programs.sig
nrow(df.programs)


PlotPairs(GetDataForPlotPairs(df.programs))
#boxplot(df.programs)
PlotWatchRateAndPostCount(df.programs)
PlotWatchRankAndPostCount(df.programs)

plot(df.programs$post_count, df.programs$rank.y, col = "blue")
plot(df.programs$post_count, df.programs$watch_rate, col = "blue")
plot(df.programs$comment_count, df.programs$watch_rate, col = "blue")
plot(df.programs$metoo_count, df.programs$watch_rate, col = "blue")
plot(df.programs$post_user_count, df.programs$watch_rate, col = "blue")
plot(df.programs$positive_post_count, df.programs$watch_rate, col = "blue")
plot(df.programs$negative_post_count, df.programs$watch_rate, col = "blue")
plot(df.programs$positive_post_user_count, df.programs$watch_rate, col = "blue")
plot(df.programs$negative_post_user_count, df.programs$watch_rate, col = "blue")
#plot(df.programs$positive_post_rate, df.programs$watch_rate, col = "blue")
#plot(df.programs$negative_post_rate, df.programs$watch_rate, col = "blue")
#plot(df.programs$positive_post_user_rate, df.programs$watch_rate, col = "blue")
#plot(df.programs$negative_post_user_rate, df.programs$watch_rate, col = "blue")
#plot(df.programs$negative_positive_post_rate, df.programs$watch_rate, col = "blue")
#plot(df.programs$negative_positive_post_user_rate, df.programs$watch_rate, col = "blue")

# correlation analysis
PearsonCorrelationAnalysis(df.programs)
SpearmanCorrelationAnalysis(df.programs)

# regression analysis
RegressionAnalysis(df.programs)
RegressionAnalysis1(df.programs)


##############################
durations.0905.0911 <- c("20110905-20110911")
df.programs.0905.0911 = GetPrograms(durations.0905.0911, nations.ko, categories.all, air.cycles.all)
PearsonCorrelationAnalysis(df.programs.0905.0911)

PlotPairs(GetDataForPlotPairs(df.programs.0905.0911))
PlotWatchRateAndPostCount(df.programs.0905.0911)


