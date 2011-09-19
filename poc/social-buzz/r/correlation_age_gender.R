# Analyzes the correlation between me2day and offline watch rate.
# 
# Author: Younggue Bae
###############################################################################


setwd("D:/workspace/social-buzz/data/analysis")

df.me2day.rank = read.csv("tv_program_rank_0911.csv")
df.watch.rate = read.csv("tv_program_watch_rate_age_gender.csv")


GetMergedData <- function(start.date, end.date, watch.age, watch.gender) {
	df.me2day.rank.subset = subset(df.me2day.rank,
			subset = (start_date == start.date & end_date == end.date))
	
	df.watch.rate.subset = subset(df.watch.rate,
			subset = (start_date == start.date & end_date == end.date) &
					(age == watch.age & gender == watch.gender))

	df.all = NULL
	df.all = merge(df.me2day.rank.subset, df.watch.rate.subset, by = c("program_id"), all = TRUE)
	
	return (df.all)	
}


CorrelationAnalysis <- function(start.date, end.date, age, gender) {	
	df.data = NULL
	df.data = GetMergedData(start.date, end.date, age, gender)
	
	out = cor(df.data$post_count, df.data$watch_rate, use = "pairwise.complete.obs")	
	cat(age, "-", gender, " : correlation [post_count <-> watch_rate]  == ", out, "\n")
}

###
# Execute
###
start.date <- "20110905"
end.date <- "20110911"

CorrelationAnalysis(start.date, end.date, "00s", "F")
CorrelationAnalysis(start.date, end.date, "10s", "F")
CorrelationAnalysis(start.date, end.date, "20s", "F")
CorrelationAnalysis(start.date, end.date, "30s", "F")
CorrelationAnalysis(start.date, end.date, "40s", "F")
CorrelationAnalysis(start.date, end.date, "50s", "F")
CorrelationAnalysis(start.date, end.date, "60s", "F")

CorrelationAnalysis(start.date, end.date, "00s", "M")
CorrelationAnalysis(start.date, end.date, "10s", "M")
CorrelationAnalysis(start.date, end.date, "20s", "M")
CorrelationAnalysis(start.date, end.date, "30s", "M")
CorrelationAnalysis(start.date, end.date, "40s", "M")
CorrelationAnalysis(start.date, end.date, "50s", "M")
CorrelationAnalysis(start.date, end.date, "60s", "M")



