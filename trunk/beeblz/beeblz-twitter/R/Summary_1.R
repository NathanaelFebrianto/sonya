# This R script is for summary of popluar users 
# 
# Author: Younggue Bae
###############################################################################

setwd("D:/dev/workspace/beeblz/beeblz-twitter/R")

library(gplots)

df_summary = read.csv("twitter_summary.csv")

###
# Plots the audience size.
###
PlotAudienceSize <- function() {	

	tweets <- df_summary$total_tweet_count
	users <- df_summary$audience_count
	xlabs = df_summary$user
	data <- rbind(tweets, users)
	
	par(mar=c(8, 5, 2, 0.5))
	
	max_y <- max(tweets) + 100000
	
	plot.colors <- c("black", "gray")
	title <- "Tweets and Users of Audience"
	barplot2(data, names.arg = xlabs,
			ylim = c(0, max_y),
			cex.names = 1.0, cex.axis = 1.0, las = 2, space = c(0, 0.8), 			
			beside = TRUE, col = plot.colors)
	
	box()
	
	#legend(28,max_y-50000, c("# of Tweets", "# of Users"), cex = 1.0, fill = plot.colors)
	legend("topright", c("# of Tweets", "# of Users"), cex = 1.0, fill = plot.colors)
}


PlotSentimentSize <- function() {	
	
	pos.users <- df_summary$positive_user_count
	neg.users <- df_summary$negative_user_count
	pos.users.rate <- df_summary$positive_user_count/df_summary$audience_count*100
	neg.users.rate <- df_summary$negative_user_count/df_summary$audience_count*100
	xlabs = df_summary$user
	data.rate <- rbind(pos.users.rate, neg.users.rate)
	data.count <- rbind(pos.users, neg.users)
	
	#par(mar=c(8, 5, 2, 5))
	par(mar=c(8, 3, 2, 3))
	
	plot.colors <- c("black", "gray", "blue", "red")
	title <- "Positive and Negative Audience"

	max_y <- max(pos.users.rate) + 5

	barplot(data.rate, names.arg = xlabs, axes = FALSE, 
			#cex.names = 0.80, cex.axis = 0.80, 
			ylim = c(0, max_y),
			las = 2, 
			space = c(0, 0.8),
			beside = TRUE, 
			col = c(plot.colors[1], plot.colors[2]))
	axis(2, pretty(range(pos.users.rate), 10))
	#mtext(2, text = "Rate (%)", line = 3, cex = 1.5)
	mtext(2, text = "Rate (%)", line = 2, cex = 1)
	
	par(new = TRUE) # tell R to overwrite the first plot
	plot(pos.users, type = "o", pch = 21, lty = "solid", axes = FALSE, lwd = 2,
			col = plot.colors[3], xaxt = "n", yaxt = "n", xlab = "", ylab = "")

	axis(4, pretty(range(pos.users), 20))
	#mtext(4, text = "# of Users", line = 3, cex = 1.5)
	mtext(4, text = "# of Users", line = 2, cex = 1)
	
	par(new = TRUE)
	plot(neg.users, type = "o", pch = 22, lty = "solid", lwd = 2,
			col = plot.colors[4], xaxt = "n", yaxt = "n", xlab = "", ylab = "")
	
	legend("topleft", c("Pos. Users(%)", "Neg. Users(%)"), 			
			cex = 1, fill = c(plot.colors[1], plot.colors[2]))
	
	legend("topright", c("Pos. Users(#)", "Neg. Users(#)"), 
			pch = c(21, 22), lty = c("solid", "solid"),
			cex = 1, col = c(plot.colors[3], plot.colors[4]))
}
 

tiff(filename = "figure-1ab.tiff", width = 17.15, height = 8.25, units = "cm", pointsize = 7, res = 1200, compression = "lzw")
par(mfrow = c(1, 2)) 
PlotAudienceSize()
PlotSentimentSize()
dev.off()
