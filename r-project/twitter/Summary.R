# This R script is for summary of popluar users 
# 
# Author: Younggue Bae
###############################################################################

setwd("D:/dev/workspace/r-project/twitter")

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
	
	plot.colors <- c("black", "gray")
	title <- "Tweets and Users of Audience"
	barplot2(data, main = title, names.arg = xlabs,
			cex.names = 0.80, cex.axis = 0.80, las = 2, space=c(0.2, 0.8),
			beside = TRUE, col = plot.colors)
	
	#box()
	
	legend("topright", c("Tweets", "Users"), cex = 0.8, fill = plot.colors)
}


PlotSentimentSize <- function() {	
	
	pos.users <- df_summary$positive_user_count
	neg.users <- df_summary$negative_user_count
	pos.users.rate <- df_summary$positive_user_count/df_summary$audience_count*100
	neg.users.rate <- df_summary$negative_user_count/df_summary$audience_count*100
	xlabs = df_summary$user
	data.rate <- rbind(pos.users.rate, neg.users.rate)
	data.count <- rbind(pos.users, neg.users)
	
	plot.colors <- c("black", "gray", "blue", "red")
	title <- "Positive and Negative Audience"

	par(mar = c(5, 4, 4, 5) + 0.1)
	barplot(data.rate, main = title, names.arg = xlabs, axes = FALSE, 
			#cex.names = 0.80, cex.axis = 0.80, 
			las = 2, 
			space=c(0.2, 0.8),
			beside = TRUE, 
			col = c(plot.colors[1], plot.colors[2]))
	axis(2, pretty(range(pos.users.rate), 10))
	mtext(2, text = "Rate(%)", line = 2)
	
	par(new = TRUE) # tell R to overwrite the first plot
	plot(pos.users, type = "o", pch = 21, lty = "solid", axes = FALSE, 
			col = plot.colors[3], xaxt = "n", yaxt = "n", xlab = "", ylab = "")

	axis(4, pretty(range(pos.users), 20))
	mtext(4, text = "Count", line = 2)
	
	par(new = TRUE)
	plot(neg.users, type = "o", pch = 22, lty = "solid", 
			col = plot.colors[4], xaxt = "n", yaxt = "n", xlab = "", ylab = "")


	
	legend("topleft", c("Positive users(%)", "Negative users(%)"), 			
			cex = 1, fill = c(plot.colors[1], plot.colors[2]))
	
	legend("topright", c("Positive users(#)", "Negative users(#)"), 
			pch = c(21, 22), lty = c("solid", "solid"),
			cex = 1, col = c(plot.colors[3], plot.colors[4]))
}

PlotSentimentSize1 <- function() {	
	
	pos.users <- df_summary$positive_user_count
	neg.users <- df_summary$negative_user_count
	pos.users.rate <- df_summary$positive_user_count/df_summary$audience_count*100
	neg.users.rate <- df_summary$negative_user_count/df_summary$audience_count*100
	xlabs = df_summary$user
	data.rate <- rbind(pos.users.rate, neg.users.rate)
	data.count <- rbind(pos.users, neg.users)
	
	plot.colors <- c("black", "gray", "blue", "red")
	title <- "Positive and Negative Audience"
	
	par(mar = c(5, 4, 4, 5) + 0.1)
	barplot(data.count, main = title, names.arg = xlabs, axes = FALSE, 
			#cex.names = 0.80, cex.axis = 0.80, 
			las = 2, 
			space=c(0.2, 0.8),
			beside = TRUE, 
			col = c(plot.colors[1], plot.colors[2]))
	axis(2, pretty(range(pos.users), 10))
	mtext(2, text = "Count", line = 2)
	
	par(new = TRUE) # tell R to overwrite the first plot
	plot(pos.users.rate, type = "o", pch = 21, lty = "solid", axes = FALSE, 
			col = plot.colors[3], xaxt = "n", yaxt = "n", xlab = "", ylab = "")
	
	axis(4, pretty(range(pos.users.rate), 20))
	mtext(4, text = "Rate(%)", line = 2)
	
	par(new = TRUE)
	plot(neg.users.rate, type = "o", pch = 22, lty = "solid",
			col = plot.colors[4], xaxt = "n", yaxt = "n", xlab = "", ylab = "")
	
	
	legend("topleft", c("Positive users(#)", "Negative users(#)"), 			
			cex = 1, fill = c(plot.colors[1], plot.colors[2]))
	
	legend("topright", c("Positive users(%)", "Negative users(%)"), 
			pch = c(21, 22), lty = c("solid", "solid"),
			cex = 1, col = c(plot.colors[3], plot.colors[4]))
}


PlotAudienceSize()

PlotSentimentSize()