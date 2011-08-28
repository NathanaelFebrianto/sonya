# This R script is for BarackObama 
# 
# Author: Younggue Bae
###############################################################################

setwd("D:/dev/workspace/r-project/twitter")

#df_obama = read.csv("twitter_obama.csv")
df_obama = read.csv("twitter_obama_filtered.csv")

library(lmtest)

# Calculate z-score to normalize data.
Zscore <- function (x) {
	mu = mean(x)
	ro = sd(x)
	z = (x - mu) / ro
	return (z)
}

###
# Plot time series of sentiment and gallup data with one graph.
###
PlotTimeseriesSentiment <- function () {	
	
	# Compute the largest y value used in the data (or we could just use range again)
	#max_y <- max(df_obama$gallup_approve)
	max_y <- 1
	
	# Defines colors to be used 
	plot_colors <- c("blue", "red", "blue", "red")
	
	# Graph autos using y axis that ranges from 0 to max_y.
	# Turn off axes and annotations (axis labels) so we can 
	# specify them ourself
	plot(df_obama$gallup_approve, type = "l", col = plot_colors[1], lty = "dashed",
			ylim = c(0, max_y), axes = FALSE, ann = FALSE)
	
	# Make x axis
	axis(1, at = 1:length(df_obama$date), lab = df_obama$date)
	
	# Make y axis with horizontal labels that display ticks at 
	# every 0.1 marks.
	axis(2, las = 1, at = 0.1*10:max_y)
	
	# Create box around plot
	box()
	
	# Graph line of gallup disapprave
	lines(df_obama$gallup_disapprove, type = "l", pch = 22, lty = "dashed", 
			col = plot_colors[2])
	
	# Graph line of positve tweet rate
	lines(df_obama$positive_tweet_rate, type = "l", pch = 24, lty = "solid", 
			col = plot_colors[3])
	
	# Graph line of negative tweet rate
	lines(df_obama$negative_tweet_rate, type = "l", pch = 24, lty = "solid", 
			col = plot_colors[4])
	
	# Create a title with a red, bold/italic font
	title(main = "Gallup vs. Twitter for BarackObama", col.main = "black", font.main = 4)
	
	# Label the x and y axes
	title(xlab = "Date", col.lab = "black")
	title(ylab = "Rate", col.lab = "black")
	
	# Create a legend at (1, max_y) that is slightly smaller 
	# (cex) and uses the same line colors and points used by 
	# the actual plots
	legend(1, max_y, 
			c("Approve", "Disapprove", "Positive", "Negative"), 
			cex = 0.8, col = plot_colors, 
			pch = 21:24, lty = c("dashed", "dashed", "solid", "solid"))
}

###
# Plot time series of sentiment and gallup data with two graphs.
###
PlotTimeseriesSentiment1 <- function () {
	
	# Split the screen into two rows and one column, defining screens 1 and 2.	
	split.screen(figs = c(2, 1))	
	
	# Defines colors to be used 
	plot_colors <- c("blue", "red")
	
	###################################################
	screen(1)
	# Compute the largest y value used in the data (or we could just use range again)
	max_y <- max(df_obama$gallup_approve) + 0.1
	min_y <- min(df_obama$gallup_approve) - 0.1
	
	# Graph autos using y axis that ranges from 0 to max_y.
	# Turn off axes and annotations (axis labels) so we can 
	# specify them ourself
	plot(df_obama$gallup_approve, type = "l", col = plot_colors[1], lty = "solid",
			ylim = c(min_y, max_y), axes = FALSE, ann = FALSE)
	
	# Make x axis
	axis(1, at = 1:length(df_obama$date), lab = df_obama$date)
	
	# Make y axis with horizontal labels that display ticks at 
	# every 0.1 marks.
	axis(2, las = 1, at = 0.1*10:max_y)
	
	# Create box around plot
	box()
	
	# Graph line of gallup disapprave
	lines(df_obama$gallup_disapprove, type = "l", pch = 22, lty = "solid", 
			col = plot_colors[2])
	
	# Create a title with a red, bold/italic font
	title(main = "Gallup for Obama", col.main = "black", font.main = 4)
	
	# Label the x and y axes
	title(xlab = "Date", col.lab = "black")
	title(ylab = "Rate", col.lab = "black")
	
	# Create a legend at (1, max_y) that is slightly smaller 
	# (cex) and uses the same line colors and points used by 
	# the actual plots
	legend(1, max_y, 
			c("Approve", "Disapprove"), 
			cex = 0.8, col = plot_colors, 
			pch = 21:22, lty = c("dashed", "dashed"))
	
	###################################################
	screen(2)
	#max_y <- max(df_obama$positive_tweet_rate)
	max_y <- max(df_obama$positive_tweet_rate) + 0.1
	min_y <- min(df_obama$negative_tweet_rate) - 0.1
	
	# Graph autos using y axis that ranges from 0 to max_y.
	# Turn off axes and annotations (axis labels) so we can 
	# specify them ourself
	plot(df_obama$positive_tweet_rate, type = "l", col = plot_colors[1], lty = "solid",
			ylim = c(min_y, max_y), axes = FALSE, ann = FALSE)
	
	# Make x axis
	axis(1, at = 1:length(df_obama$date), lab = df_obama$date)
	
	# Make y axis with horizontal labels that display ticks at 
	# every 0.1 marks.
	axis(2, las = 1, at = 0.1*10:max_y)
	
	# Create box around plot
	box()
	
	# Graph line of negative tweet rate
	lines(df_obama$negative_tweet_rate, type = "l", pch = 22, lty = "solid", 
			col = plot_colors[2])
	
	# Create a title with a red, bold/italic font
	title(main = "Twitter Sentiment for BarackObama", col.main = "black", font.main = 4)
	
	# Label the x and y axes
	title(xlab = "Date", col.lab = "black")
	title(ylab = "Rate", col.lab = "black")
	
	# Create a legend at (1, max_y) that is slightly smaller 
	# (cex) and uses the same line colors and points used by 
	# the actual plots
	legend(1, max_y, 
			c("Positive", "Negative"), 
			cex = 0.8, col = plot_colors, 
			pch = 21:22, lty = c("solid", "solid"))
}

###
# Plot time series of positive sentiment and gallup data by zscore.
###
PlotTimeseriesZscorePositive <- function () {	
	
	# Compute the largest y value used in the data (or we could just use range again)
	max_y <- max(Zscore(df_obama$gallup_approve), 
			Zscore(df_obama$positive_user_rate)) + 0.5
	min_y <- min(Zscore(df_obama$gallup_approve), 
			Zscore(df_obama$positive_tweet_rate)) - 0.5
	
	# Defines colors to be used 
	plot_colors <- c("blue", "blue")
	
	# Graph autos using y axis that ranges from 0 to max_y.
	# Turn off axes and annotations (axis labels) so we can 
	# specify them ourself
	plot(Zscore(df_obama$gallup_approve), type = "l", col = plot_colors[1], lty = "dashed",
			ylim = c(min_y, max_y), axes = FALSE, ann = FALSE, lwd  = 2)
	
	# Make x axis
	axis(1, at = 1:length(df_obama$date), lab = df_obama$date)
	
	# Make y axis with horizontal labels that display ticks at 
	# every 0.5 marks.
	axis(2, las = 1, at = 0.5*-10:10)
	
	# Create box around plot
	box()
	
	# Graph line of positve tweet rate
	lines(Zscore(df_obama$positive_tweet_rate), type = "l", pch = 24, lty = "solid", 
			col = plot_colors[2], lwd  = 2)
	
	# Create a title with a red, bold/italic font
	title(main = "Gallup vs. Twitter for BarackObama", col.main = "black", font.main = 4)
	
	# Label the x and y axes
	title(xlab = "Date", col.lab = "black")
	title(ylab = "Rate", col.lab = "black")
	
	# Create a legend at (1, max_y) that is slightly smaller 
	# (cex) and uses the same line colors and points used by 
	# the actual plots
	legend(1, max_y, 
			c("Approve", "Positive"), 
			cex = 0.8, col = plot_colors, 
			#pch = 21:22, 
			lty = c("dashed", "solid"), lwd =2)
}

###
# Plot time series of negative sentiment and gallup data by zscore.
###
PlotTimeseriesZscoreNegative <- function () {	
	
	# Compute the largest y value used in the data (or we could just use range again)
	max_y <- max(Zscore(df_obama$gallup_disapprove), 
			Zscore(df_obama$negative_tweet_rate)) + 0.5
	min_y <- min(Zscore(df_obama$gallup_disapprove), 
			Zscore(df_obama$negative_tweet_rate)) - 0.5
	
	# Defines colors to be used 
	plot_colors <- c("red", "red")
	
	# Graph autos using y axis that ranges from 0 to max_y.
	# Turn off axes and annotations (axis labels) so we can 
	# specify them ourself
	plot(Zscore(df_obama$gallup_disapprove), type = "l", col = plot_colors[1], lty = "dashed",
			ylim = c(min_y, max_y), axes = FALSE, ann = FALSE, lwd  = 2)
	
	# Make x axis
	axis(1, at = 1:length(df_obama$date), lab = df_obama$date)
	
	# Make y axis with horizontal labels that display ticks at 
	# every 0.5 marks.
	axis(2, las = 1, at = 0.5*-10:10)
	
	# Create box around plot
	box()
	
	# Graph line of negative tweet rate
	lines(Zscore(df_obama$negative_tweet_rate), type = "l", pch = 24, lty = "solid", 
			col = plot_colors[2], lwd  = 2)
	
	# Create a title with a red, bold/italic font
	title(main = "Gallup vs. Twitter for BarackObama", col.main = "black", font.main = 4)
	
	# Label the x and y axes
	title(xlab = "Date", col.lab = "black")
	title(ylab = "Rate", col.lab = "black")
	
	# Create a legend at (1, max_y) that is slightly smaller 
	# (cex) and uses the same line colors and points used by 
	# the actual plots
	legend(1, max_y, 
			c("Disapprove", "Negative"), 
			cex = 0.8, col = plot_colors, 
			#pch = 21:22, 
			lty = c("dashed", "solid"), lwd =2)
}

###
# Plot time series of gap of sentiment and gallup data by zscore.
###
PlotTimeseriesZscoreGap <- function () {	
	
	# Compute the largest y value used in the data (or we could just use range again)
	max_y <- max(Zscore(df_obama$gallup_approve - df_obama$gallup_disapprove), 
			Zscore(df_obama$positive_tweet_rate - df_obama$negative_tweet_rate)) + 0.5
	min_y <- min(Zscore(df_obama$gallup_approve - df_obama$gallup_disapprove), 
			Zscore(df_obama$positive_tweet_rate - df_obama$negative_tweet_rate)) + 0.5
	
	# Defines colors to be used 
	plot_colors <- c("forestgreen", "forestgreen")
	
	# Graph autos using y axis that ranges from 0 to max_y.
	# Turn off axes and annotations (axis labels) so we can 
	# specify them ourself
	plot(Zscore(df_obama$gallup_approve - df_obama$gallup_disapprove), type = "l", col = plot_colors[1], lty = "dashed",
			ylim = c(min_y, max_y), axes = FALSE, ann = FALSE, lwd  = 2)
	
	# Make x axis
	axis(1, at = 1:length(df_obama$date), lab = df_obama$date)
	
	# Make y axis with horizontal labels that display ticks at 
	# every 0.5 marks.
	axis(2, las = 1, at = 0.5*-10:10)
	
	# Create box around plot
	box()
	
	# Graph line of positve tweet rate
	lines(Zscore(df_obama$positive_tweet_rate - df_obama$negative_tweet_rate), type = "l", pch = 24, lty = "solid", 
			col = plot_colors[2], lwd  = 2)
	
	# Create a title with a red, bold/italic font
	title(main = "Gallup vs. Twitter for BarackObama", col.main = "black", font.main = 4)
	
	# Label the x and y axes
	title(xlab = "Date", col.lab = "black")
	title(ylab = "Rate", col.lab = "black")
	
	# Create a legend at (1, max_y) that is slightly smaller 
	# (cex) and uses the same line colors and points used by 
	# the actual plots
	legend(1, max_y, 
			c("Gallup (Approve - Disapprove)", "Twitter (Positive - Negative"), 
			cex = 0.8, col = plot_colors, 
			#pch = 21:22, 
			lty = c("dashed", "solid"), lwd =2)
}


###
# EDA
###
Eda <- function () {
	attach(df_obama)
	lm(gallup_approve ~ positive_tweet_count, data = df_obama)
	lm(gallup_disapprove ~ negative_tweet_count, data = df_obama)
	
	plot(positive_tweet_count, gallup_approve)
	plot(negative_tweet_count, gallup_disapprove)
	plot(gallup_disapprove, gallup_approve)
	plot(negative_tweet_count, positive_tweet_count)
	plot(positive_tweet_count, total_tweet_count)
	plot(negative_tweet_count, total_tweet_count)
	plot(df_obama)
	

	qqnorm(gallup_approve)
	qqline(gallup_approve, col = "blue")
	qqnorm(positive_tweet_count)
	qqline(positive_tweet_count, col = "blue")
	qqnorm(negative_tweet_count)
	qqline(negative_tweet_count, col = "blue")
	
	# Shapiro-Wilk normality test
	shapiro.test(gallup_approve)
	shapiro.test(gallup_disapprove)
	shapiro.test(positive_tweet_count)
	shapiro.test(positive_user_count)
	shapiro.test(negative_tweet_count)
	shapiro.test(negative_user_count)
	shapiro.test(positive_tweet_rate)
	shapiro.test(positive_user_rate)
	
	shapiro.test(log(gallup_disapprove))
	shapiro.test(log(negative_tweet_count))
	shapiro.test(sqrt(positive_tweet_count))
	

	# Correlation Analysis
	cor(positive_tweet_rate, gallup_approve)
	cor(gallup_disapprove, gallup_approve)
	cor(negative_tweet_rate, gallup_disapprove)
	cor(negative_tweet_count, total_tweet_count)
	cor(positive_tweet_count, total_tweet_count)
	cor(negative_tweet_count, positive_tweet_count)
	
	cor(positive_tweet_rate, gallup_disapprove)
	cor(negative_tweet_rate, gallup_approve)
	
	# Regression
	out = lm(gallup_approve ~ positive_tweet_rate, data = df_obama)
	out
	summary(out)
	plot(gallup_approve ~ positive_tweet_rate, data = df_obama, col = "blue")
	abline(out, col = "red")
	
	plot(gallup_disapprove ~ negative_tweet_rate, data = df_obama, col = "blue")
	abline(out, col = "red")
	
	plot(gallup_approve ~ negative_tweet_rate, data = df_obama, col = "blue")
	abline(out, col = "red")
	
	plot(gallup_disapprove ~ positive_tweet_rate, data = df_obama, col = "blue")
	abline(out, col = "red")
	
	plot(sqrt(positive_tweet_rate) ~ gallup_approve, data = df_obama, col = "blue")
	abline(out, col = "red")
}

###
# Granger Casuality analysis
###
AnalyzeGrangerCasualty <- function ()  {
		
	grangertest(gallup_approve ~ positive_tweet_rate, order = 1, data = df_obama)
	grangertest(gallup_approve ~ positive_tweet_rate, order = 2, data = df_obama)
	grangertest(gallup_approve ~ positive_tweet_rate, order = 3, data = df_obama)
	grangertest(gallup_approve ~ positive_tweet_rate, order = 4, data = df_obama)
	grangertest(gallup_approve ~ positive_tweet_rate, order = 5, data = df_obama)
	grangertest(gallup_approve ~ positive_tweet_rate, order = 6, data = df_obama)
	grangertest(gallup_approve ~ positive_tweet_rate, order = 7, data = df_obama)
	
	grangertest(positive_tweet_rate ~ gallup_approve, order = 1, data = df_obama)
	grangertest(positive_tweet_rate ~ gallup_approve, order = 2, data = df_obama)
	grangertest(positive_tweet_rate ~ gallup_approve, order = 3, data = df_obama)
	grangertest(positive_tweet_rate ~ gallup_approve, order = 4, data = df_obama)
	grangertest(positive_tweet_rate ~ gallup_approve, order = 5, data = df_obama)
	grangertest(positive_tweet_rate ~ gallup_approve, order = 6, data = df_obama)
	grangertest(positive_tweet_rate ~ gallup_approve, order = 7, data = df_obama)
	
	grangertest(gallup_disapprove ~ negative_tweet_rate, order = 1, data = df_obama)
	grangertest(gallup_disapprove ~ negative_tweet_rate, order = 2, data = df_obama)
	grangertest(gallup_disapprove ~ negative_tweet_rate, order = 3, data = df_obama)
	grangertest(gallup_disapprove ~ negative_tweet_rate, order = 4, data = df_obama)
	grangertest(gallup_disapprove ~ negative_tweet_rate, order = 5, data = df_obama)
	grangertest(gallup_disapprove ~ negative_tweet_rate, order = 6, data = df_obama)
	grangertest(gallup_disapprove ~ negative_tweet_rate, order = 7, data = df_obama)
	
	grangertest(negative_tweet_rate ~ gallup_disapprove, order = 1, data = df_obama)
	grangertest(negative_tweet_rate ~ gallup_disapprove, order = 2, data = df_obama)
	grangertest(negative_tweet_rate ~ gallup_disapprove, order = 3, data = df_obama)
	grangertest(negative_tweet_rate ~ gallup_disapprove, order = 4, data = df_obama)
	grangertest(negative_tweet_rate ~ gallup_disapprove, order = 5, data = df_obama)
	grangertest(negative_tweet_rate ~ gallup_disapprove, order = 6, data = df_obama)
	grangertest(negative_tweet_rate ~ gallup_disapprove, order = 7, data = df_obama)
	
	#######################
	grangertest(Zscore(positive_tweet_rate) ~ Zscore(gallup_approve), order = 1, data = df_obama)
	grangertest(Zscore(positive_tweet_rate) ~ Zscore(gallup_approve), order = 2, data = df_obama)
	grangertest(Zscore(positive_tweet_rate) ~ Zscore(gallup_approve), order = 3, data = df_obama)
	grangertest(Zscore(positive_tweet_rate) ~ Zscore(gallup_approve), order = 4, data = df_obama)
	grangertest(Zscore(positive_tweet_rate) ~ Zscore(gallup_approve), order = 5, data = df_obama)
	grangertest(Zscore(positive_tweet_rate) ~ Zscore(gallup_approve), order = 6, data = df_obama)
	grangertest(Zscore(positive_tweet_rate) ~ Zscore(gallup_approve), order = 7, data = df_obama)
	
	grangertest(Zscore(gallup_approve) ~ Zscore(positive_tweet_rate), order = 1, data = df_obama)
	grangertest(Zscore(gallup_approve) ~ Zscore(positive_tweet_rate), order = 2, data = df_obama)
	grangertest(Zscore(gallup_approve) ~ Zscore(positive_tweet_rate), order = 3, data = df_obama)
	grangertest(Zscore(gallup_approve) ~ Zscore(positive_tweet_rate), order = 4, data = df_obama)
	grangertest(Zscore(gallup_approve) ~ Zscore(positive_tweet_rate), order = 5, data = df_obama)
	grangertest(Zscore(gallup_approve) ~ Zscore(positive_tweet_rate), order = 6, data = df_obama)
	grangertest(Zscore(gallup_approve) ~ Zscore(positive_tweet_rate), order = 7, data = df_obama)
	
	grangertest(Zscore(gallup_approve) ~ Zscore(positive_user_rate), order = 1, data = df_obama)
	grangertest(Zscore(gallup_approve) ~ Zscore(positive_user_rate), order = 2, data = df_obama)
	grangertest(Zscore(gallup_approve) ~ Zscore(positive_user_rate), order = 3, data = df_obama)
	grangertest(Zscore(gallup_approve) ~ Zscore(positive_user_rate), order = 4, data = df_obama)
	grangertest(Zscore(gallup_approve) ~ Zscore(positive_user_rate), order = 5, data = df_obama)
	grangertest(Zscore(gallup_approve) ~ Zscore(positive_user_rate), order = 6, data = df_obama)
	grangertest(Zscore(gallup_approve) ~ Zscore(positive_user_rate), order = 7, data = df_obama)
	
	grangertest(Zscore(gallup_disapprove) ~ Zscore(negative_tweet_rate), order = 1, data = df_obama)
	grangertest(Zscore(gallup_disapprove) ~ Zscore(negative_tweet_rate), order = 2, data = df_obama)
	grangertest(Zscore(gallup_disapprove) ~ Zscore(negative_tweet_rate), order = 3, data = df_obama)
	grangertest(Zscore(gallup_disapprove) ~ Zscore(negative_tweet_rate), order = 4, data = df_obama)
	grangertest(Zscore(gallup_disapprove) ~ Zscore(negative_tweet_rate), order = 5, data = df_obama)
	grangertest(Zscore(gallup_disapprove) ~ Zscore(negative_tweet_rate), order = 6, data = df_obama)
	grangertest(Zscore(gallup_disapprove) ~ Zscore(negative_tweet_rate), order = 7, data = df_obama)
	
	grangertest(Zscore(gallup_approve - gallup_disapprove) ~ Zscore(positive_tweet_rate - negative_tweet_rate), order = 1, data = df_obama)
	grangertest(Zscore(gallup_approve - gallup_disapprove) ~ Zscore(positive_tweet_rate - negative_tweet_rate), order = 2, data = df_obama)
	grangertest(Zscore(gallup_approve - gallup_disapprove) ~ Zscore(positive_tweet_rate - negative_tweet_rate), order = 3, data = df_obama)
	grangertest(Zscore(gallup_approve - gallup_disapprove) ~ Zscore(positive_tweet_rate - negative_tweet_rate), order = 4, data = df_obama)
	grangertest(Zscore(gallup_approve - gallup_disapprove) ~ Zscore(positive_tweet_rate - negative_tweet_rate), order = 5, data = df_obama)
	grangertest(Zscore(gallup_approve - gallup_disapprove) ~ Zscore(positive_tweet_rate - negative_tweet_rate), order = 6, data = df_obama)
	grangertest(Zscore(gallup_approve - gallup_disapprove) ~ Zscore(positive_tweet_rate - negative_tweet_rate), order = 7, data = df_obama)
}

###
# Correlation analysis
###
AnalyzeCorrelation <- function () {
	
	cor.positive = cor(Zscore(df_obama$gallup_approve), Zscore(df_obama$positive_tweet_rate))
	cor.negative = cor(Zscore(df_obama$gallup_disapprove), Zscore(df_obama$negative_tweet_rate))
	cor.gap = cor(Zscore(df_obama$gallup_approve - df_obama$gallup_disapprove), Zscore(df_obama$positive_tweet_rate - df_obama$negative_tweet_rate))
	
#	cor.positive = cor(df_obama$gallup_approve, df_obama$positive_tweet_rate)
#	cor.negative = cor(df_obama$gallup_disapprove, df_obama$negative_tweet_rate)
#	cor.gap = cor(df_obama$gallup_approve - df_obama$gallup_disapprove, df_obama$positive_tweet_rate - df_obama$negative_tweet_rate)
		
	cat("correlation between twitter positive and gallup approve == ", cor.positive, "\n")
	cat("correlation between twitter negative and gallup disapprove == ", cor.negative, "\n")
	cat("correlation between twitter gap and gallup gap == ", cor.gap, "\n")
	
	
	cor.test(Zscore(df_obama$gallup_approve), Zscore(df_obama$positive_tweet_rate))
	cor.test(Zscore(df_obama$gallup_disapprove), Zscore(df_obama$negative_tweet_rate))
	cor.test(Zscore(df_obama$gallup_approve - df_obama$gallup_disapprove), Zscore(df_obama$positive_tweet_rate - df_obama$negative_tweet_rate))
}



###
# Test
###
PlotTimeseriesSentiment()
PlotTimeseriesSentiment1()
PlotTimeseriesZscorePositive()
PlotTimeseriesZscoreNegative()
PlotTimeseriesZscoreGap()

#5/13, 5/20, 5/24, 6/29, 6/30, 7/4, 7/7, 7/8 데이터 이상치 데이터 없는지 확인 
# SELECT * FROM tweet WHERE target_user = "BarackObama" AND create_date = "2011-06-30 00:00:00"
# SELECT tweet_text, COUNT(*) FROM tweet WHERE target_user = "BarackObama" AND create_date = "2011-07-07 00:00:00" GROUP BY tweet_text ORDER BY COUNT(*) DESC 
# SELECT tweet_text, COUNT(DISTINCT user) FROM tweet WHERE target_user = "BarackObama" AND create_date = "2011-07-07 00:00:00" GROUP BY tweet_text ORDER BY COUNT(*) DESC 
#
# 5/13
#	4379 positive --- RT @justinbieber: I think you're wrong. pretty sure President @BarackObama will keep this promise. #payitforward - http://bit.ly/jocrJy
# 5/20
# 5/24
# 6/29
#	3827 positive --- RT @justinbieber: glad to help President @BarackObama keep his promise. glad he could be there too. #SWAG  http://twitpic.com/5ie0u1
# 6/30
#	2708 positive --- RT @justinbieber: glad to help President @BarackObama keep his promise. glad he could be there too. #SWAG  http://twitpic.com/5ie0u1
# 7/4
#	1873 negative? --- RT @foxnewspolitics: BREAKING NEWS: President @BarackObama assassinated, 2 gunshot wounds have proved too much. It's a sad 4th for #america. #obamadead RIP
#            <- http://www.munhwa.com/news/view.html?no=2011070501032932301002
# 7/7
# 7/8


