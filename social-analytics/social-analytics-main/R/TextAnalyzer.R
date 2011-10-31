# Analyzes documents.
# 
# Author: Younggue Bae
###############################################################################

setwd("D:/dev/workspace/social-analytics-main/bin/data")

library(RTextTools)
library(topicmodels)
library(tm)
#library(rKNLP)

###
# Gets the my stopwords.
###
GetStopwords <- function() {  
	my.stopwords.table = read.table(file("stopwords.txt", encoding = "UTF-8"),
			header = TRUE, comment.char = "#", sep = ",",
			stringsAsFactors = FALSE, na.strings = "")
	
	my.stopwords.subset = subset(my.stopwords.table, 
			select = c("type", "stopword"),
			subset = (my.stopwords.table$type %in% c("common", "tag")))
	my.stopwords <- my.stopwords.subset$stopword
	
	return (my.stopwords)
}

writeTermFrequency <- function(dtm, output.filename) { 
    # remove sparse terms to simplify the cluster plot
		# Note: tweak the sparse parameter to determine the number of words.
		# About 10-30 words is good.
		mydata.dtm <- removeSparseTerms(dtm, sparse = 0.96)
		nrow(mydata.dtm); ncol(mydata.dtm)
		
		# export dtm
		tf.matrix <- as.matrix(mydata.dtm)
    write.csv(tf.matrix, file=paste("dtm_", output.filename, ".csv", sep = ""), row.names = TRUE)

		v <- as.matrix(sort(colSums(tf.matrix), decreasing = TRUE)); v	
		w <- rownames(v); length(w); w
		colnames(v) <- c("tf")
		
		# export tf sum
		write.csv(v, file=paste("tf_", output.filename, ".csv", sep = ""), row.names = TRUE)
		
		PlotTermFreq(v, output.filename)
}

PlotTermFreq <- function (tf.matrix, name) {
  require(grDevices); # for colors
	#x <- sort(v[1:10,], decreasing=FALSE)
	x <- sort(tf.matrix[,], decreasing=FALSE)	
	title <- paste("Term Frequency - ", name, sep = "")
	barplot(x, horiz=TRUE, cex.names = 0.70, space = 1, las = 1, col = grey.colors(length(x)), main = title)
}


findTopics <- function(input.filename, output.filename) {
 	data = read.table(file(input.filename, encoding = "UTF-8"),
			header = TRUE, comment.char = "#", sep = "\t",
			stringsAsFactors = FALSE, na.strings = "")
	print(nrow(data))
	colnames(data)
	head(data)
	
	mydata.terms <- data.frame(textCol = data$text1) 
	print(nrow(mydata.terms))
	head(mydata.terms)
	mydata.terms = subset(mydata.terms, 
			subset = (textCol != ""))
	print(nrow(mydata.terms))
	mydata.source <- DataframeSource(mydata.terms)
	
	mydata.corpus <- Corpus(mydata.source)
	
	mydata.corpus <- tm_map(mydata.corpus, tolower)
	
	# remove punctuation
	mydata.corpus <- tm_map(mydata.corpus, removePunctuation)	
	
	# remove generic and custom stopwords
	all.stopwords <- c(stopwords('english'), GetStopwords())
	mydata.corpus <- tm_map(mydata.corpus, removeWords, all.stopwords)
	
	mydata.dtm <- DocumentTermMatrix(mydata.corpus,
			control = list(weighting = weightTf, minWordLength = 1,
					stopwords = TRUE))
	print(mydata.dtm)
	
	a <- mydata.dtm[1,1]
	str(a)
	
	# write term frequency
	writeTermFrequency(mydata.dtm, output.filename)
	
	k <- 6
	lda <- LDA(mydata.dtm, k)
	print(terms(lda, 10))
	print(topics(lda, 2))
}

# findTopics("androidmarket_navertalk.txt", "androidmarket_navertalk")
# findTopics("androidmarket_naverapp.txt", "androidmarket_naverapp")
 findTopics("androidmarket_kakaotalk.txt", "androidmarket_kakaotalk")
# findTopics("twitter_navertalk.txt", "twitter_navertalk")
# findTopics("twitter_naverapp.txt", "twitter_naverapp")
# findTopics("twitter_kakaotalk.txt", "twitter_kakaotalk")





