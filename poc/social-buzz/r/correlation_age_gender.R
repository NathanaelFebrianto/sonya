# Analyzes the correlation between me2day and offline watch rate.
# 
# Author: Younggue Bae
###############################################################################


setwd("D:/workspace/social-buzz/data/analysis")

df.sns.progams.rank = read.csv("tv_program_rank.csv")
df.watch.rate = read.csv("tv_program_watch_rate_age_gender.csv")


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

GetSnsProgramRanks <- function(sns.site, durations, nations, categories, air.cycles) {
	
	df.subset = subset(df.sns.progams.rank, 
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
					, "score"
					, "rank"),
			subset =  (
						site == sns.site &
						(paste(start_date, "-", end_date, sep = "") == durations[1] |
							paste(start_date, "-", end_date, sep = "") == durations[2] |
							paste(start_date, "-", end_date, sep = "") == durations[3] |
							paste(start_date, "-", end_date, sep = "") == durations[4] |
							paste(start_date, "-", end_date, sep = "") == durations[5] |
							paste(start_date, "-", end_date, sep = "") == durations[6]) &
						(nation == nations[1] | nation == nations[2]) &
						(category == categories[1] | 
							category == categories[2] | 
							category == categories[3]) &	
						(air_cycle == air.cycles[1] |
							air_cycle == air.cycles[2] |
							air_cycle == air.cycles[3] |
							air_cycle == air.cycles[4] |
							air_cycle == air.cycles[5] |
							air_cycle == air.cycles[6] |
							air_cycle == air.cycles[7] |
							air_cycle == air.cycles[8])))	
	
	cat("df.sns.programs.rank subset row count == ", nrow(df.subset), "\n")
	return (df.subset)
}

GetMergedData <- function(df.sns.programs.rank.subset, watch.age, watch.gender) {
	df.watch.rate.subset = subset(df.watch.rate,
			subset = (age == watch.age & gender == watch.gender))
	
	df.all = NULL
	df.all = merge(df.sns.programs.rank.subset, df.watch.rate.subset, by = c("program_id"), all = TRUE)
	
	return (df.all)	
}


CorrelationAnalysis <- function(df.sns.programs.rank.subset, age, gender) {	
	df.data = NULL
	df.data = GetMergedData(df.sns.programs.rank.subset, age, gender)
	
	#out = cor(df.data$post_count, df.data$rank.y, use = "pairwise.complete.obs", method = "spearman")	
	out = cor(df.data$post_count, df.data$watch_rate, use = "pairwise.complete.obs", method = "pearson")	
	cat(age, "-", gender, " : correlation [post_count <-> watch_rate]  == ", out, "\n")
	
	out = cor.test(df.data$post_count, df.data$watch_rate)
	cat("  p.value  == ", out$p.value, "\n")	
	
}

###
# Execute
###
sns.site <- "me2day"

nations <- c("KO")

durations <- c(
		"20110905-20110911"
)

df.sns.rank = GetSnsProgramRanks(sns.site, durations, nations, categories.all, air.cycles.all)

CorrelationAnalysis(df.sns.rank, "00s", "F")
CorrelationAnalysis(df.sns.rank, "10s", "F")
CorrelationAnalysis(df.sns.rank, "20s", "F")
CorrelationAnalysis(df.sns.rank, "30s", "F")
CorrelationAnalysis(df.sns.rank, "40s", "F")
CorrelationAnalysis(df.sns.rank, "50s", "F")
CorrelationAnalysis(df.sns.rank, "60s", "F")

CorrelationAnalysis(df.sns.rank, "00s", "M")
CorrelationAnalysis(df.sns.rank, "10s", "M")
CorrelationAnalysis(df.sns.rank, "20s", "M")
CorrelationAnalysis(df.sns.rank, "30s", "M")
CorrelationAnalysis(df.sns.rank, "40s", "M")
CorrelationAnalysis(df.sns.rank, "50s", "M")
CorrelationAnalysis(df.sns.rank, "60s", "M")



