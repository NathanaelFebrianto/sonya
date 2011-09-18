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
PlotSentiment <- function() {	
	
	# Compute the largest y value used in the data (or we could just use range again)
	#max_y <- max(df_obama$gallup_approve)
	max_y <- 1
	
	# Defines colors to be used 
	plot_colors <- c("blue", "red", "blue", "red")
	
	# Graph autos using y axis that ranges from 0 to max_y.
	# Turn off axes and annotations (axis labels) so we can 
	# specify them ourself
	plot(df_obama$gallup_approve, type = "l", col = plot_colors[1], lty = "dashed",
			ylim = c(0, max_y), axes = FALSE, ann = FALSE, lwd  = 2)
	
	# Make x axis
	axis(1, at = 1:length(df_obama$date), lab = df_obama$date)
	
	# Make y axis with horizontal labels that display ticks at 
	# every 0.1 marks.
	axis(2, las = 1, at = 0.1*10:max_y)
	
	# Create box around plot
	box()
	
	# Graph line of gallup disapprave
	lines(df_obama$gallup_disapprove, type = "l", pch = 22, lty = "dashed", 
			col = plot_colors[2], lwd  = 2)
	
	# Graph line of positve tweet rate
	lines(df_obama$positive_tweet_rate, type = "l", pch = 24, lty = "solid", 
			col = plot_colors[3], lwd  = 2)
	
	# Graph line of negative tweet rate
	lines(df_obama$negative_tweet_rate, type = "l", pch = 24, lty = "solid", 
			col = plot_colors[4], lwd  = 2)
	
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
			pch = 21:24, lty = c("dashed", "dashed", "solid", "solid"), lwd =2)
}

###
# Plot time series of sentiment and gallup data with two graphs.
###
PlotSentiment1 <- function() {
	
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
			ylim = c(min_y, max_y), axes = FALSE, ann = FALSE, lwd  = 2)
	
	# Make x axis
	axis(1, at = 1:length(df_obama$date), lab = df_obama$date)
	
	# Make y axis with horizontal labels that display ticks at 
	# every 0.1 marks.
	axis(2, las = 1, at = 0.1*10:max_y)
	
	# Create box around plot
	box()
	
	# Graph line of gallup disapprave
	lines(df_obama$gallup_disapprove, type = "l", pch = 22, lty = "solid", 
			col = plot_colors[2], lwd  = 2)
	
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
			pch = 21:22, lty = c("solid", "solid"), lwd =2)
	
	###################################################
	screen(2)
	#max_y <- max(df_obama$positive_tweet_rate)
	max_y <- max(df_obama$positive_tweet_rate) + 0.1
	min_y <- min(df_obama$negative_tweet_rate) - 0.1
	
	# Graph autos using y axis that ranges from 0 to max_y.
	# Turn off axes and annotations (axis labels) so we can 
	# specify them ourself
	plot(df_obama$positive_tweet_rate, type = "l", col = plot_colors[1], lty = "solid",
			ylim = c(min_y, max_y), axes = FALSE, ann = FALSE, lwd  = 2)
	
	# Make x axis
	axis(1, at = 1:length(df_obama$date), lab = df_obama$date)
	
	# Make y axis with horizontal labels that display ticks at 
	# every 0.1 marks.
	axis(2, las = 1, at = 0.1*10:max_y)
	
	# Create box around plot
	box()
	
	# Graph line of negative tweet rate
	lines(df_obama$negative_tweet_rate, type = "l", pch = 22, lty = "solid", 
			col = plot_colors[2], lwd  = 2)
	
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
			pch = 21:22, lty = c("solid", "solid"), lwd =2)
}

###
# Plot time series of positive sentiment and gallup data by zscore.
###
PlotPositiveZscore <- function() {	
	
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
			ylim = c(min_y, max_y), axes = FALSE, ann = FALSE, lwd  = 4)
	
	# Make x axis
	axis(1, at = 1:length(df_obama$date), lab = df_obama$date)
	
	# Make y axis with horizontal labels that display ticks at 
	# every 0.5 marks.
	axis(2, las = 1, at = 0.5*-10:10)
	
	# Create box around plot
	box()
	
	# Graph line of positve tweet rate
	lines(Zscore(df_obama$positive_tweet_rate), type = "l", pch = 24, lty = "solid", 
			col = plot_colors[2], lwd  = 4)
	
	# Create a title with a red, bold/italic font
	title(main = "Gallup Job Approve vs. Twitter Positive", col.main = "black", font.main = 4)
	
	# Label the x and y axes
	#title(xlab = "Date", col.lab = "black")
	title(ylab = "z-scores", col.lab = "black")
	
	# Create a legend at (1, max_y) that is slightly smaller 
	# (cex) and uses the same line colors and points used by 
	# the actual plots
	legend(1, max_y, 
			c("Gallup", "Twitter"), 
			cex = 0.8, col = plot_colors, 
			#pch = 21:22, 
			lty = c("dashed", "solid"), lwd =2)
}

###
# Plot time series of negative sentiment and gallup data by zscore.
###
PlotNegativeZscore <- function() {	
	
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
			ylim = c(min_y, max_y), axes = FALSE, ann = FALSE, lwd  = 4)
	
	# Make x axis
	axis(1, at = 1:length(df_obama$date), lab = df_obama$date)
	
	# Make y axis with horizontal labels that display ticks at 
	# every 0.5 marks.
	axis(2, las = 1, at = 0.5*-10:10)
	
	# Create box around plot
	box()
	
	# Graph line of negative tweet rate
	lines(Zscore(df_obama$negative_tweet_rate), type = "l", pch = 24, lty = "solid", 
			col = plot_colors[2], lwd  = 4)
	
	# Create a title with a red, bold/italic font
	title(main = "Gallup Job Dispprove vs. Twitter Negative", col.main = "black", font.main = 4)
	
	# Label the x and y axes
	#title(xlab = "Date", col.lab = "black")
	title(ylab = "z-scores", col.lab = "black")
	
	# Create a legend at (1, max_y) that is slightly smaller 
	# (cex) and uses the same line colors and points used by 
	# the actual plots
	legend(1, max_y, 
			c("Gallup", "Twitter"), 
			cex = 0.8, col = plot_colors, 
			#pch = 21:22, 
			lty = c("dashed", "solid"), lwd =2)
}

###
# Plot time series of gap of sentiment and gallup data by zscore.
###
PlotGapZscore <- function() {	
	
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
			ylim = c(min_y, max_y), axes = FALSE, ann = FALSE, lwd  = 4)
	
	# Make x axis
	axis(1, at = 1:length(df_obama$date), lab = df_obama$date)
	
	# Make y axis with horizontal labels that display ticks at 
	# every 0.5 marks.
	axis(2, las = 1, at = 0.5*-10:10)
	
	# Create box around plot
	box()
	
	# Graph line of positve tweet rate
	lines(Zscore(df_obama$positive_tweet_rate - df_obama$negative_tweet_rate), type = "l", pch = 24, lty = "solid", 
			col = plot_colors[2], lwd  = 4)
	
	# Create a title with a red, bold/italic font
	title(main = "Gallup Job Approve-Disapprove vs. Twitter Positive-Negative", col.main = "black", font.main = 4)
	
	# Label the x and y axes
	#title(xlab = "Date", col.lab = "black")
	title(ylab = "z-scores", col.lab = "black")
	
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
# Plot time series of positive/negative sentiment rate and gallup data by zscore.
###
PlotPNRateZscore <- function() {	
	
	# Compute the largest y value used in the data (or we could just use range again)
	max_y <- max(Zscore(df_obama$gallup_approve), 
			Zscore(df_obama$positive_tweet_count/df_obama$negative_tweet_count)) + 0.5
	min_y <- min(Zscore(df_obama$gallup_approve), 
			Zscore(df_obama$positive_tweet_count/df_obama$negative_tweet_count)) - 0.5
	
	# Defines colors to be used 
	plot_colors <- c("forestgreen", "forestgreen")
	
	# Graph autos using y axis that ranges from 0 to max_y.
	# Turn off axes and annotations (axis labels) so we can 
	# specify them ourself
	plot(Zscore(df_obama$gallup_approve), type = "l", col = plot_colors[1], lty = "dashed",
			ylim = c(min_y, max_y), axes = FALSE, ann = FALSE, lwd  = 4)
	
	# Make x axis
	axis(1, at = 1:length(df_obama$date), lab = df_obama$date)
	
	# Make y axis with horizontal labels that display ticks at 
	# every 0.5 marks.
	axis(2, las = 1, at = 0.5*-10:10)
	
	# Create box around plot
	box()
	
	# Graph line of positve tweet rate
	lines(Zscore(df_obama$positive_tweet_count/df_obama$negative_tweet_count), type = "l", pch = 24, lty = "solid", 
			col = plot_colors[2], lwd  = 4)
	
	# Create a title with a red, bold/italic font
	title(main = "Gallup Job Approve/Disapprove vs. Twitter Positive/Negative", col.main = "black", font.main = 4)
	
	# Label the x and y axes
	#title(xlab = "Date", col.lab = "black")
	title(ylab = "z-scores", col.lab = "black")
	
	# Create a legend at (1, max_y) that is slightly smaller 
	# (cex) and uses the same line colors and points used by 
	# the actual plots
	legend(1, max_y, 
			c("Gallup", "Twitter"), 
			cex = 0.8, col = plot_colors, 
			#pch = 21:22, 
			lty = c("dashed", "solid"), lwd =2)
}

###
# Plot time series of positive/negative sentiment rate and gallup data by zscore.
###
PlotPNRateZscore1 <- function() {	
	
	# Compute the largest y value used in the data (or we could just use range again)
	max_y <- max(Zscore(df_obama$gallup_approve/df_obama$gallup_disapprove), 
			Zscore(df_obama$positive_tweet_count/df_obama$negative_tweet_count)) + 0.5
	min_y <- min(Zscore(df_obama$gallup_approve/df_obama$gallup_disapprove), 
			Zscore(df_obama$positive_tweet_count/df_obama$negative_tweet_count)) - 0.5
	
	# Defines colors to be used 
	plot_colors <- c("forestgreen", "forestgreen")
	
	# Graph autos using y axis that ranges from 0 to max_y.
	# Turn off axes and annotations (axis labels) so we can 
	# specify them ourself
	plot(Zscore(df_obama$gallup_approve/df_obama$gallup_disapprove), type = "l", col = plot_colors[1], lty = "dashed",
			ylim = c(min_y, max_y), axes = FALSE, ann = FALSE, lwd  = 4)
	
	# Make x axis
	axis(1, at = 1:length(df_obama$date), lab = df_obama$date)
	
	# Make y axis with horizontal labels that display ticks at 
	# every 0.5 marks.
	axis(2, las = 1, at = 0.5*-10:10)
	
	# Create box around plot
	box()
	
	# Graph line of positve tweet rate
	lines(Zscore(df_obama$positive_tweet_count/df_obama$negative_tweet_count), type = "l", pch = 24, lty = "solid", 
			col = plot_colors[2], lwd  = 4)
	
	# Create a title with a red, bold/italic font
	title(main = "Gallup Job Approve/Disapprove vs. Twitter Positive/Negative", col.main = "black", font.main = 4)
	
	# Label the x and y axes
	#title(xlab = "Date", col.lab = "black")
	title(ylab = "z-scores", col.lab = "black")
	
	# Create a legend at (1, max_y) that is slightly smaller 
	# (cex) and uses the same line colors and points used by 
	# the actual plots
	legend(1, max_y, 
			c("Gallup", "Twitter"), 
			cex = 0.8, col = plot_colors, 
			#pch = 21:22, 
			lty = c("dashed", "solid"), lwd =2)
}

###
# EDA
###
Eda <- function() {
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
AnalyzeGrangerCasualty <- function()  {
	
	##############################	
	# 1. tweet rate -> gallup 
	##############################	
	
	# 1-1. positive
	for (lag in 1:7) {
		out = grangertest(Zscore(gallup_approve) ~ Zscore(positive_tweet_rate), order = lag, data = df_obama)
		print(out)
	}
	
	# 1-2. negative
	for (lag in 1:7) {
		out = grangertest(Zscore(gallup_disapprove) ~ Zscore(negative_tweet_rate), order = lag, data = df_obama)
		print(out)
	}
	
	# 1-3. gap(positive - negative)	
	for (lag in 1:7) {
		out = grangertest(Zscore(gallup_approve - gallup_disapprove) ~ Zscore(positive_tweet_rate - negative_tweet_rate), order = lag, data = df_obama)
		print(out)
	}
	
	##############################	
	# 2. gallup -> tweet rate
	##############################	
	
	# 2-1. positive
	for (lag in 1:7) {
		out = grangertest(Zscore(positive_tweet_rate) ~ Zscore(gallup_approve), order = lag, data = df_obama)
		print(out)
	}
	
	# 2-2. negative
	for (lag in 1:7) {
		out = grangertest(Zscore(negative_tweet_rate) ~ Zscore(gallup_disapprove), order = lag, data = df_obama)
		print(out)
	}
	
	# 2-3. gap(positive - negative)	
	for (lag in 1:7) {
		out = grangertest(Zscore(positive_tweet_rate - negative_tweet_rate) ~ Zscore(gallup_approve - gallup_disapprove), order = lag, data = df_obama)
		print(out)
	}	
	
	##############################	
	# 3. user rate -> gallup 
	##############################	
	
	# 3-1. positive
	for (lag in 1:7) {
		out = grangertest(Zscore(gallup_approve) ~ Zscore(positive_user_rate), order = lag, data = df_obama)
		print(out)
	}
	
	# 3-2. negative
	for (lag in 1:7) {
		out = grangertest(Zscore(gallup_disapprove) ~ Zscore(negative_user_rate), order = lag, data = df_obama)
		print(out)
	}
	
	# 3-3. gap(positive - negative)	
	for (lag in 1:7) {
		out = grangertest(Zscore(gallup_approve - gallup_disapprove) ~ Zscore(positive_user_rate - negative_user_rate), order = lag, data = df_obama)
		print(out)
	}
	
	##############################	
	# 4. gallup -> user rate
	##############################	
	
	# 4-1. positive
	for (lag in 1:7) {
		out = grangertest(Zscore(positive_user_rate) ~ Zscore(gallup_approve), order = lag, data = df_obama)
		print(out)
	}
	
	# 4-2. negative
	for (lag in 1:7) {
		out = grangertest(Zscore(negative_user_rate) ~ Zscore(gallup_disapprove), order = lag, data = df_obama)
		print(out)
	}
	
	# 4-3. gap(positive - negative)	
	for (lag in 1:7) {
		out = grangertest(Zscore(positive_user_rate - negative_user_rate) ~ Zscore(gallup_approve - gallup_disapprove), order = lag, data = df_obama)
		print(out)
	}
	
	##############################	
	# 5. positive/negative rate -> gallup 
	##############################	
	
	# 5-1. positive/negative tweet rate -> gallup approve
	for (lag in 1:7) {
		out = grangertest(Zscore(gallup_approve) ~ Zscore(positive_tweet_count/negative_tweet_count), order = lag, data = df_obama)
		print(out)
	}
	
	# 5-2. positive/negative user rate -> gallup approve
	for (lag in 1:7) {
		out = grangertest(Zscore(gallup_approve) ~ Zscore(positive_user_count/negative_user_count), order = lag, data = df_obama)
		print(out)
	}

	# 5-3. positive/negative tweet rate -> gallup approve/disapprove rate
	for (lag in 1:7) {
		out = grangertest(Zscore(gallup_approve/gallup_disapprove) ~ Zscore(positive_tweet_count/negative_tweet_count), order = lag, data = df_obama)
		print(out)
	}
	
	# 5-4. positive/negative user rate -> gallup approve/disapprove rate
	for (lag in 1:7) {
		out = grangertest(Zscore(gallup_approve/gallup_disapprove) ~ Zscore(positive_user_count/negative_user_count), order = lag, data = df_obama)
		print(out)
	}
	
}



###
# Correlation analysis
###
AnalyzeCorrelation <- function() {
	
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
#PlotSentiment()
PlotSentiment1()
PlotPositiveZscore()
PlotNegativeZscore()
PlotGapZscore()
PlotPNRateZscore()
PlotPNRateZscore1()

