# TODO: Add comment
# 
# Author: Louie
###############################################################################

setwd("D:/dev/workspace/r-project/twitter")

df.britney = read.csv("twitter_britney.csv")
df.ladygaga = read.csv("twitter_ladygaga.csv")

pairs(df.britney)
pairs(df.ladygaga)

################################
# column names of df.britney
################################
#till_the_world_ends	
#femme_fatale	
#c_till_the_world_ends	
#c_femme_fatale	
#chart_sum	
#total_tweet_count	
#total_user_count	
#positive_tweet_count	
#negative_tweet_count	
#positive_user_count	
#negative_user_count	
#positive_tweet_rate	
#negative_tweet_rate	
#positive_user_rate	
#negative_user_rate

################################
# column names of df.ladygaga
################################
#born_this_way	
#judas	
#the_edge_of_glory	
#the_fame	
#c_born_this_way	
#c_judas	
#c_the_edge_of_glory	
#c_the_fame	
#chart_sum	
#total_tweet_count	
#total_user_count	
#positive_tweet_count	
#negative_tweet_count	
#positive_user_count	
#negative_user_count	
#positive_tweet_rate	
#negative_tweet_rate	
#positive_user_rate	
#negative_user_rate

###
# Converts string to date.
###
ConvertDate <- function (str) {
	format(as.Date(str), format = "%m/%d")	
}

# Calculates z-score to normalize data.
Zscore <- function (x) {
	mu = mean(x)
	ro = sd(x)
	z = (x - mu) / ro
	return (z)
}

###
# Plots ladygaga.
###
PlotLadygaga <- function(df.data) {	
	df.data <- df.ladygaga
	max.y <- 2
	min.y <- -2
	
	plot.colors <- c("darkgreen", "brown", "blue", "red")
	
	plot(Zscore(df.data$chart_sum), type = "o", col = plot.colors[1], lty = "solid",
			ylim = c(min.y, max.y), axes = FALSE, ann = FALSE, lwd  = 3)
	
	axis(1, at = 1:length(df.data$week), lab = lapply(df.data$week, ConvertDate))
	
	axis(2, las = 1, at = min.y:max.y)
	
	box()
	
	lines(Zscore(df.data$c_born_this_way), type = "o", pch = 21, lty = "dashed", 
			col = plot.colors[1])
	
	lines(Zscore(df.data$c_judas), type = "o", pch = 21, lty = "dashed", 
			col = plot.colors[1])
	
	lines(Zscore(df.data$c_the_edge_of_glory), type = "o", pch = 21, lty = "dashed", 
			col = plot.colors[1])

	lines(Zscore(df.data$c_the_fame), type = "o", pch = 21, lty = "dashed", 
			col = plot.colors[1])

	lines(Zscore(df.data$total_user_count), type = "o", pch = 22, lty = "solid", 
			col = plot.colors[2], lwd  = 3)
	
	lines(Zscore(df.data$positive_user_count), type = "o", pch = 23, lty = "dashed", 
			col = plot.colors[3])
	
	lines(Zscore(df.data$negative_user_count), type = "o", pch = 24, lty = "dashed", 
			col = plot.colors[4])	
		
	title(main = "ladygaga", col.main = "black", font.main = 4)
	
	title(xlab = "Week", col.lab = "black")
	title(ylab = "Z-score", col.lab = "black")	

	legend(4, max.y, 
			c("chart sum", "songs", "total tweet users", "positive users", "negative users"), 
			cex = 0.8, col = c(plot.colors[1], plot.colors[1], plot.colors[2], plot.colors[3], plot.colors[4]), 
			pch = c(21, 21, 22, 23, 24), 
			lty = c("solid", "dashed", "solid", "dashed", "dashed"),
			lwd = c(3, 1, 3, 1, 1))
}

# britney
cor.test(df.britney$chart_sum, df.britney$total_tweet_count)
cor.test(df.britney$chart_sum, df.britney$total_user_count)
cor.test(df.britney$chart_sum, df.britney$positive_tweet_count)
cor.test(df.britney$chart_sum, df.britney$negative_tweet_count)
cor.test(df.britney$chart_sum, df.britney$positive_user_count)
cor.test(df.britney$chart_sum, df.britney$negative_user_count)
cor.test(df.britney$chart_sum, df.britney$positive_tweet_rate)
cor.test(df.britney$chart_sum, df.britney$negative_tweet_rate)
cor.test(df.britney$chart_sum, df.britney$positive_user_rate)
cor.test(df.britney$chart_sum, df.britney$negative_user_rate)

# ladygaga
cor.test(df.ladygaga$chart_sum, df.ladygaga$total_tweet_count)
cor.test(df.ladygaga$chart_sum, df.ladygaga$total_user_count)
cor.test(df.ladygaga$chart_sum, df.ladygaga$positive_tweet_count)
cor.test(df.ladygaga$chart_sum, df.ladygaga$negative_tweet_count)
cor.test(df.ladygaga$chart_sum, df.ladygaga$positive_user_count)
cor.test(df.ladygaga$chart_sum, df.ladygaga$negative_user_count)
cor.test(df.ladygaga$chart_sum, df.ladygaga$positive_tweet_rate)
cor.test(df.ladygaga$chart_sum, df.ladygaga$negative_tweet_rate)
cor.test(df.ladygaga$chart_sum, df.ladygaga$positive_user_rate)
cor.test(df.ladygaga$chart_sum, df.ladygaga$negative_user_rate)

