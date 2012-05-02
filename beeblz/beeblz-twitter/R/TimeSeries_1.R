# This R script is for the sentiment analysis of audiences 
# of popular twitterers on Twitter.
# 
# Author: Younggue Bae
###############################################################################

setwd("D:/dev/workspace/beeblz/beeblz-twitter/R")

df_audiences = read.csv("twitter_audiences.csv")
df_populars = read.csv("twitter_populars.csv")
colnames(df_audiences) <- c("user", "create_date", "a.total_tweet_count", "a.total_user_count", 
		"a.positive_tweet_count", "a.positive_user_count", "a.negative_tweet_count", "a.negative_user_count")
colnames(df_populars) <- c("user", "create_date", "p.total_tweet_count", "p.positive_tweet_count", "p.negative_tweet_count")

# Initialize df_all
df_all = NULL
df_all = merge(df_audiences, df_populars, by = c("user", "create_date"), all = TRUE)


# Calculate z-score to normalize data.
Zscore <- function (x) {
	mu = mean(x, na.rm = TRUE)
	ro = sd(x, na.rm = TRUE)
	z = (x - mu) / ro
	return (z)
}


###
# Converts string to date.
###
ConvertDate <- function (str) {
	Sys.setlocale("LC_TIME", "C")
	format(as.Date(str), format = "%B %d")	
}



PlotAudienceByTweet <- function(user_name) {	
	# Get subset of the user
	df_user = subset(df_all, 
			select = c("create_date", 
					"a.positive_tweet_count",
					"a.negative_tweet_count"),
			subset = (user == user_name))
	
	# Compute the largest y value used in the data (or we could just use range again)
	max_y <- max(df_user$a.positive_tweet_count)
	max_x <- length(df_user$create_date) - 20
	
	par(mar=c(5, 6, 2, 2))
	
	# Defines colors to be used 
	plot_colors <- c("blue", "red")
	
	# Graph autos using y axis that ranges from 0 to max_y.
	# Turn off axes and annotations (axis labels) so we can 
	# specify them ourself
	plot(df_user$a.positive_tweet_count, type = "l", col = plot_colors[1], lty = "solid", lwd = 2,
			ylim = c(0, max_y), axes = FALSE, ann = FALSE)
	
	# Make x axis
	axis(1, at = 1:length(df_user$create_date), lab = lapply(df_user$create_date, ConvertDate))
	
	# Make y axis with horizontal labels that display ticks at 
	# every 2000 marks.
	#####################################################
#	axis(2, las = 1, at = 500*0:max_y)	#for dalailama
	axis(2, las = 1, at = 2000*0:max_y)	#for aplusk
	
	# Create box around plot
	box()
	
	# Graph line of negative tweet count
	lines(df_user$a.negative_tweet_count, type = "l", lty = "dashed", lwd = 2,
			col = plot_colors[2])
	
	# Create a title with a red, bold/italic font
	title(main = paste("Audience of", user_name), col.main = "black", font.main = 4)
	
	# Label the x and y axes
	mtext(1, text = "Date", line = 3, cex = 1.0)
	mtext(2, text = "# of Tweets", line = 4, cex = 1.0)
	
	# Create a legend at (1, max_y) that is slightly smaller 
	# (cex) and uses the same line colors and points used by 
	# the actual plots
	legend(max_x, max_y, 
			c("Positive", "Negative"), 
			col = plot_colors, 
			lty = c("solid", "dashed"), lwd = c(2, 2), cex = 1.0)
}


PlotPopularUser <- function(user_name) {
	# Get subset of the user
	df_user = subset(df_all, 
			select = c("create_date", 
					"p.positive_tweet_count",
					"p.negative_tweet_count"),
			subset = (user == user_name))
	
	# Compute the largest y value used in the data (or we could just use range again)
	max_y <- max(df_user$p.positive_tweet_count, na.rm = TRUE) + 1
	max_x <- length(df_user$create_date) - 20
	
	par(mar=c(5, 6, 2, 2))
	
	# Define colors to be used 
	plot_colors <- c("blue", "red")
	
	# Graph autos using y axis that ranges from 0 to max_y.
	# Turn off axes and annotations (axis labels) so we can 
	# specify them ourself
	plot(df_user$p.positive_tweet_count, type = "l", col = plot_colors[1], lty = "solid", lwd = 2,
			ylim = c(0, max_y), axes = FALSE, ann = FALSE)
	
	# Make x axis
	axis(1, at = 1:length(df_user$create_date), lab = lapply(df_user$create_date, ConvertDate))
	
	# Make y axis with horizontal labels that display ticks at 
	# every 2000 marks.
	#####################################################
#	axis(2, las = 1, at = 1*0:max_y)	#for dalailama
	axis(2, las = 1, at = 5*0:max_y)	#for aplusk
	
	# Create box around plot
	box()
	
	# Graph line of negative tweet count of popular user
	lines(df_user$p.negative_tweet_count, type = "l", lty = "dashed", lwd = 2,
			col=plot_colors[2])
	
	# Create a title with a red, bold/italic font
	title(main = user_name, col.main = "black", font.main = 4)
	
	# Label the x and y axes
	mtext(1, text = "Date", line = 3, cex = 1.0)
	mtext(2, text = "# of Tweets", line = 4, cex = 1.0)
	
	# Create a legend at (1, max_y) that is slightly smaller 
	# (cex) and uses the same line colors and points used by 
	# the actual plots
	legend(max_x, max_y, 
		c("Positive", "Negative"), 
		col = plot_colors, 
		lty = c("solid", "dashed"), lwd = c(2, 2), cex = 1.0)
}

PlotAudienceByTweetZscore <- function(user_name) {	
	
	# Get subset of the user
	df_user = subset(df_all, 
			select = c("create_date", 
					"a.positive_tweet_count",
					"a.negative_tweet_count"),
			subset = (user == user_name))
	
	# Compute the largest y value used in the data (or we could just use range again)
	max_y <- max(Zscore(df_user$a.positive_tweet_count), na.rm = TRUE) + 0.5
	min_y <- min(Zscore(df_user$a.positive_tweet_count), na.rm = TRUE) - 0.5
	max_x <- length(df_user$create_date) - 20
	
	par(mar=c(5, 6, 2, 2))
	
	# Defines colors to be used 
	plot_colors <- c("blue", "red")
	
	# Graph autos using y axis that ranges from 0 to max_y.
	# Turn off axes and annotations (axis labels) so we can 
	# specify them ourself
	plot(Zscore(df_user$a.positive_tweet_count), type = "l", col = plot_colors[1], lty = "solid", lwd = 2,
			ylim = c(min_y, max_y), axes = FALSE, ann = FALSE)
	
	# Make x axis
	axis(1, at = 1:length(df_user$create_date), lab = lapply(df_user$create_date, ConvertDate))
	
	# Make y axis with horizontal labels that display ticks at 
	# every 2000 marks.
	axis(2, las = 1, at = 1.0*-10:10)
	
	# Create box around plot
	box()
	
	# Graph line of negative tweet count
	lines(Zscore(df_user$a.negative_tweet_count), type = "l", lty = "dashed", lwd = 2,
			col = plot_colors[2])
	
	# Create a title with a red, bold/italic font
	title(main = paste("Audience of", user_name), col.main = "black", font.main = 4)
	
	# Label the x and y axes
	mtext(1, text = "Date", line = 3, cex = 1.0)
	mtext(2, text = "z-score", line = 4, cex = 1.0)
	
	# Create a legend at (1, max_y) that is slightly smaller 
	# (cex) and uses the same line colors and points used by 
	# the actual plots
	legend(max_x, max_y, 
			c("Positive", "Negative"), 
			col = plot_colors, 
			lty = c("solid", "dashed"), lwd = c(2, 2), cex = 1.0)
}

PlotPopularUserZscore <- function(user_name) {
	
	# Get subset of the user
	df_user = subset(df_all, 
			select = c("create_date", 
					"p.positive_tweet_count",
					"p.negative_tweet_count"),
			subset = (user == user_name))
	
	# Compute the largest y value used in the data (or we could just use range again)
	#####################################################
#	max_y <- max(Zscore(df_user$p.positive_tweet_count), na.rm = TRUE) + 3		#for dalailama
	max_y <- max(Zscore(df_user$p.positive_tweet_count), na.rm = TRUE) + 0.5	#for aplusk
	min_y <- min(Zscore(df_user$p.positive_tweet_count), na.rm = TRUE) - 0.5
	max_x <- length(df_user$create_date) - 20
	
	par(mar=c(5, 6, 2, 2))
	
	# Define colors to be used 
	plot_colors <- c("blue", "red")
	
	# Graph autos using y axis that ranges from 0 to max_y.
	# Turn off axes and annotations (axis labels) so we can 
	# specify them ourself
	plot(Zscore(df_user$p.positive_tweet_count), type = "l", col = plot_colors[1], lty = "solid", lwd = 2,
			ylim = c(min_y, max_y), axes = FALSE, ann = FALSE)
	
	# Make x axis
	axis(1, at = 1:length(df_user$create_date), lab = lapply(df_user$create_date, ConvertDate))
	
	# Make y axis with horizontal labels that display ticks at 
	# every 2000 marks.
	axis(2, las = 1, at = 1.0*-10:10)
	
	# Create box around plot
	box()
	
	# Graph line of negative tweet count of popular user
	lines(Zscore(df_user$p.negative_tweet_count), type = "l", lty = "dashed", lwd = 2,
			col=plot_colors[2])
	
	# Create a title with a red, bold/italic font
	title(main = user_name, col.main = "black", font.main = 4)
	
	# Label the x and y axes
	mtext(1, text = "Date", line = 3, cex = 1.0)
	mtext(2, text = "z-score", line = 4, cex = 1.0)
	
	# Create a legend at (1, max_y) that is slightly smaller 
	# (cex) and uses the same line colors and points used by 
	# the actual plots
	legend(max_x, max_y, 
			c("Positive", "Negative"), 
			col = plot_colors, 
			lty = c("solid", "dashed"), lwd = c(2, 2), cex = 1.0)
}


###
# Execute
###
# Split the screen into two rows and one column, defining screens 1 and 2.
user <- "aplusk"
#user <- "DalaiLama"

tiff(filename = "figure-2a-2col.tiff", width = 17.15, height = 11, units = "cm", pointsize = 8, res = 600, compression = "lzw")
par(mfrow = c(2, 2)) 
PlotPopularUser(user)
PlotPopularUserZscore(user)
PlotAudienceByTweet(user)
PlotAudienceByTweetZscore(user)
dev.off()




