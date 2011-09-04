# This R script is for the sentiment analysis of audiences 
# of popular twitterers on Twitter.
# 
# Author: Younggue Bae
###############################################################################

setwd("D:/dev/workspace/r-project/twitter")

df_audiences = read.csv("twitter_audiences.csv")
df_populars = read.csv("twitter_populars.csv")
colnames(df_audiences) <- c("user", "create_date", "a.total_tweet_count", "a.total_user_count", 
		"a.positive_tweet_count", "a.positive_user_count", "a.negative_tweet_count", "a.negative_user_count")
colnames(df_populars) <- c("user", "create_date", "p.total_tweet_count", "p.positive_tweet_count", "p.negative_tweet_count")

# Initialize df_all
df_all = NULL
df_all = merge(df_audiences, df_populars, by = c("user", "create_date"), all = TRUE)


###
# Convert data
###
convertDate <- function (str) {
	format(as.Date(str), format = "%m/%d")	
}


###
# Plot the audience sentiment time series of tweet count for the specified popular user
###
PlotAudienceByTweet <- function (user_name) {	
	# Get subset of the user
	df_user = subset(df_all, 
			select = c("create_date", 
					"a.total_tweet_count",  
					"a.positive_tweet_count",
					"a.negative_tweet_count"),
			subset = (user == user_name))
	
	# Compute the largest y value used in the data (or we could just use range again)
	max_y <- max(df_user$a.total_tweet_count)
	
	# Defines colors to be used 
	plot_colors <- c("forestgreen", "blue", "red")
	
	# Graph autos using y axis that ranges from 0 to max_y.
	# Turn off axes and annotations (axis labels) so we can 
	# specify them ourself
	plot(df_user$a.total_tweet_count, type = "o", col = plot_colors[1], lty = "dashed",
			ylim = c(0, max_y), axes = FALSE, ann = FALSE)
	
	# Make x axis
	axis(1, at = 1:length(df_user$create_date), lab = lapply(df_user$create_date, convertDate))
	
	# Make y axis with horizontal labels that display ticks at 
	# every 2000 marks.
	axis(2, las = 1, at = 2000*0:max_y)
	
	# Create box around plot
	box()
	
	# Graph line of positive tweet count
	lines(df_user$a.positive_tweet_count, type = "o", pch = 22, lty = "solid", 
			col=plot_colors[2])
	
	# Graph line of negative tweet count
	lines(df_user$a.negative_tweet_count, type = "o", pch = 23, lty = "solid", 
			col=plot_colors[3])
	
	# Create a title with a red, bold/italic font
	title(main = paste("Audience of", user_name), col.main = "black", font.main = 4)
	
	# Label the x and y axes
	title(xlab = "Date", col.lab = "black")
	title(ylab = "Tweets", col.lab = "black")
	
	# Create a legend at (1, max_y) that is slightly smaller 
	# (cex) and uses the same line colors and points used by 
	# the actual plots
	legend(1, max_y, 
			c("Total", "Positive", "Negative"), 
			cex = 0.8, col = plot_colors, 
			pch = 21:23, lty = c("dashed", "solid", "solid"))
}


###
# Plot the audience sentiment time series of user count for the specified popular user
###
PlotAudienceByUser <- function (user_name) {
	# Get subset of the user
	df_user = subset(df_all, 
			select = c("create_date", 
					"a.total_user_count",  
					"a.positive_user_count",
					"a.negative_user_count"),
			subset = (user == user_name))
	
	# Compute the largest y value used in the data (or we could just use range again)
	max_y <- max(df_user$a.total_user_count)
	
	# Defines colors to be used 
	plot_colors <- c("forestgreen", "blue", "red")
	
	# Graph autos using y axis that ranges from 0 to max_y.
	# Turn off axes and annotations (axis labels) so we can 
	# specify them ourself
	plot(df_user$a.total_user_count, type = "o", col = plot_colors[1], lty = "dashed",
			ylim = c(0, max_y), axes = FALSE, ann = FALSE)
	
	# Make x axis
	axis(1, at = 1:length(df_user$create_date), lab = lapply(df_user$create_date, convertDate))
	
	# Make y axis with horizontal labels that display ticks at 
	# every 2000 marks.
	axis(2, las = 1, at = 2000*0:max_y)
	
	# Create box around plot
	box()
	
	# Graph line of positive user count
	lines(df_user$a.positive_user_count, type = "o", pch = 22, lty = "solid", 
			col=plot_colors[2])
	
	# Graph line of negative user count
	lines(df_user$a.negative_user_count, type = "o", pch = 23, lty = "solid", 
			col=plot_colors[3])
	
	# Create a title with a red, bold/italic font
	title(main = paste("Audience of", user_name), col.main = "black", font.main = 4)
	
	# Label the x and y axes
	title(xlab = "Date", col.lab = "black")
	title(ylab = "Users", col.lab = "black")
	
	# Create a legend at (1, max_y) that is slightly smaller 
	# (cex) and uses the same line colors and points used by 
	# the actual plots
	legend(1, max_y, 
			c("Total", "Positive", "Negative"), 
			cex = 0.8, col = plot_colors, 
			pch = 21:23, lty = c("dashed", "solid", "solid"))
}


###
# Plot the popular user's sentiment time series for the specified popular user
###
PlotPopularUser <- function (user_name) {
	# Get subset of the user
	df_user = subset(df_all, 
			select = c("create_date", 
					"p.total_tweet_count",
					"p.positive_tweet_count",
					"p.negative_tweet_count"),
			subset = (user == user_name))
	
	# Compute the largest y value used in the data (or we could just use range again)
	max_y <- max(df_user$p.total_tweet_count, na.rm = TRUE)
	
	# Define colors to be used 
	plot_colors <- c("forestgreen", "blue", "red")
	
	# Graph autos using y axis that ranges from 0 to max_y.
	# Turn off axes and annotations (axis labels) so we can 
	# specify them ourself
	plot(df_user$p.total_tweet_count, type = "o", col = plot_colors[1], lty = "dashed",
			ylim = c(0, max_y), axes = FALSE, ann = FALSE)
	
	# Make x axis
	axis(1, at = 1:length(df_user$create_date), lab = lapply(df_user$create_date, convertDate))
	
	# Make y axis with horizontal labels that display ticks at 
	# every 2000 marks.
	axis(2, las = 1, at = 5*0:max_y)
	
	# Create box around plot
	box()
	
	# Graph line of positive tweet count of audience
	lines(df_user$p.positive_tweet_count, type = "o", pch = 22, lty = "solid", 
			col=plot_colors[2])
	
	# Graph line of negative tweet count of popular user
	lines(df_user$p.negative_tweet_count, type = "o", pch = 23, lty = "solid", 
			col=plot_colors[3])
	
	# Create a title with a red, bold/italic font
	title(main = paste("Tweets of", user_name), col.main = "black", font.main = 4)
	
	# Label the x and y axes
	title(xlab = "Date", col.lab = "black")
	title(ylab = "Tweets", col.lab = "black")
	
	# Create a legend at (1, max_y) that is slightly smaller 
	# (cex) and uses the same line colors and points used by 
	# the actual plots
	legend(1, max_y, 
			c("Total", "Positive", "Negative"), 
			cex = 0.8, col = plot_colors, 
			pch = 21:23, lty = c("dashed", "solid", "solid"))	
}


###
# Execute
###
# Split the screen into two rows and one column, defining screens 1 and 2.	
split.screen(figs = c(2, 1))
screen(1)
PlotPopularUser("aplusk")
screen(2)
PlotAudienceByTweet("aplusk")
#PlotAudienceByUser("aplusk")




