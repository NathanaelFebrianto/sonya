# Analyzes documents.
# 
# Author: Younggue Bae
###############################################################################

setwd("D:/dev/workspace/social-analytics-main/bin/data")

library(RTextTools)
library(topicmodels)
library(tm)
library(lsa)
#library(rKNLP)

###
# Gets the my stopwords.
###
getStopwords <- function() {  
	my.stopwords.table = read.table(file("stopwords.txt", encoding = "UTF-8"),
			header = TRUE, comment.char = "#", sep = ",",
			stringsAsFactors = FALSE, na.strings = "")
	
	my.stopwords.subset = subset(my.stopwords.table, 
			select = c("type", "stopword"),
			subset = (my.stopwords.table$type %in% c("common", "tag")))
	my.stopwords <- my.stopwords.subset$stopword
    
    print(my.stopwords)
	
	return (my.stopwords)
}

writeTermFrequency <- function(dtm, output.filename) { 
    # remove sparse terms to simplify the cluster plot
	# Note: tweak the sparse parameter to determine the number of words.
	# About 10-30 words is good.
  	dtm.new <- removeSparseTerms(dtm, sparse = 0.99)
	nrow(dtm.new); ncol(dtm.new)
		
	# export dtm
	tf.matrix <- as.matrix(dtm.new)
    write.csv(tf.matrix, file=paste("dtm_", output.filename, ".csv", sep = ""), row.names = TRUE)

	v <- as.matrix(sort(colSums(tf.matrix), decreasing = TRUE)); v	
	w <- rownames(v); length(w); w
	colnames(v) <- c("tf")
		
	# export tf sum
	write.csv(v, file=paste("tf_", output.filename, ".csv", sep = ""), row.names = TRUE)
	
	plotTermFreq(v, output.filename)
}

plotTermFreq <- function (tf.matrix, name) {
    require(grDevices); # for colors
	#x <- sort(v[1:10,], decreasing=FALSE)
	x <- sort(tf.matrix[,], decreasing=FALSE)	
	title <- paste("Term Frequency - ", name, sep = "")
	barplot(x, horiz=TRUE, cex.names = 0.70, space = 1, las = 1, col = grey.colors(length(x)), main = title)
}


runLDA <- function(k = 6, input.filename, output.filename) {
    
#     input.filename <- "androidmarket_navertalk.txt"
#     output.filename <- "androidmarket_navertalk"
    
 	data = read.table(file(input.filename, encoding = "UTF-8"),
			header = TRUE, sep = "\t", comment.char = "",
			stringsAsFactors = FALSE, na.strings = "")
	print(nrow(data))
	colnames(data)
	head(data)

	df.terms <- data.frame(textCol = data$subject) 
	print(nrow(df.terms))
	head(df.terms)
 	df.terms = subset(df.terms, 
 			subset = (textCol != ""))
	print(nrow(df.terms))
	dfs <- DataframeSource(df.terms)
	
	corpus <- Corpus(dfs)	
	corpus <- tm_map(corpus, tolower)
	
	corpus <- tm_map(corpus, removePunctuation)
    corpus <- tm_map(corpus, removeNumbers)
	all.stopwords <- c(stopwords('english'), getStopwords())
	corpus <- tm_map(corpus, removeWords, all.stopwords)

	dtm <- DocumentTermMatrix(corpus,
			control = list(weighting = weightTf 
                    , minWordLength = 2
					#, stopwords = TRUE
                    #, removeNumbers = TRUE, 
                    #, removePunctuation = TRUE
                    ))
	print(dtm)     
	print(dim(dtm))   
	print(summary(col_sums(dtm)))

    # remove rows with all zero elements
    dtm.nonzero = as.matrix(dtm)
    dtm.nonzero <- dtm.nonzero[rowSums(dtm.nonzero) != 0, ] 
     
	# write term frequency
	writeTermFrequency(dtm, output.filename)    

	lda <- LDA(dtm.nonzero, k)
    return(lda)
}


runLSA <- function(input.filename, output.filename) {
    
#   input.filename <- "androidmarket_navertalk.txt"
#   output.filename <- "androidmarket_navertalk"
    
    data = read.table(file(input.filename, encoding = "UTF-8"),
			header = TRUE, sep = "\t", comment.char = "",
			stringsAsFactors = FALSE, na.strings = "")

    print(nrow(data))
	colnames(data)
	head(data)
    
    df.terms <- data.frame(textCol = data$text3) 
	print(nrow(df.terms))
	head(df.terms)
 	df.terms = subset(df.terms, 
 			subset = (textCol != ""))
	print(nrow(df.terms))
	dfs <- DataframeSource(df.terms)
 	
	corpus <- Corpus(dfs)	
	corpus <- tm_map(corpus, tolower)
	
	corpus <- tm_map(corpus, removePunctuation)
    corpus <- tm_map(corpus, removeNumbers)
	all.stopwords <- c(stopwords('english'), getStopwords())
	corpus <- tm_map(corpus, removeWords, all.stopwords)

	dtm <- TermDocumentMatrix(corpus,
			control = list(weighting = weightTf 
                    , minWordLength = 2 
					#, stopwords = TRUE
                    #, removeNumbers = TRUE, 
                    #, removePunctuation = TRUE
                    ))

    print(dtm)     
	print(dim(dtm))   
	print(summary(col_sums(dtm)))
    
    lsa.space = lsa(dtm, dims = dimcalc_raw())
    round(as.textmatrix(lsa.space), 2)
    lsa.space = lsa(dtm, dims = dimcalc_share())
    
    mymatrix = as.textmatrix(lsa.space)
    print(mymatrix)
#   unlink(dtm, recursive=TRUE)
    
    return(mymatrix)
}

runLSA_1 <- function(input.filename, output.filename) {
    
    input.filename <- "androidmarket_navertalk.txt"
    output.filename <- "androidmarket_navertalk"
    
    data = read.table(file(input.filename, encoding = "UTF-8"),
    		header = TRUE, sep = "\t", comment.char = "",
			stringsAsFactors = FALSE, na.strings = "")
	print(nrow(data))
	colnames(data)
	head(data)
    
    td = tempfile()
    dir.create(td)
    
    for (i in 1:nrow(data)) {
        write(data$text1, file = paste(td, paste("D", i, sep = ""), sep = "/"))
    }
    
    dtm = textmatrix(td, minWordLength = 2)

    print(dtm)     
	print(dim(dtm))   
	print(summary(col_sums(dtm)))
    
    lsa.space = lsa(dtm, dims = dimcalc_raw())
    round(as.textmatrix(lsa.space), 2)
    lsa.space = lsa(dtm, dims = dimcalc_share())
    
    mymatrix = as.textmatrix(lsa.space)
    print(mymatrix)
#   unlink(dtm, recursive=TRUE)
    return(mymatrix)
}




######################################
# LDA(Latent Dirichlet Allocation)
######################################

# k: number of topics
k <- 6

#####################
# Android Market
#####################
lda <- runLDA(k, "androidmarket_navertalk.txt", "androidmarket_navertalk")
lda <-runLDA(k,"androidmarket_kakaotalk.txt", "androidmarket_kakaotalk", )
lda <-runLDA(k,"androidmarket_naverapp.txt", "androidmarket_naverapp")

#####################
# Twitter
#####################
lda <-runLDA(k,"twitter_navertalk.txt", "twitter_navertalk")
lda <-runLDA(k,"twitter_kakaotalk.txt", "twitter_kakaotalk")
lda <-runLDA(k,"twitter_naverapp.txt", "twitter_naverapp")

#####################
# Me2DAY
#####################
lda <- runLDA(k, "me2day_navertalk.txt", "me2day_navertalk")
lda <- runLDA(k, "me2day_kakaotalk.txt", "me2day_kakaotalk")
lda <- runLDA(k, "me2day_naverapp.txt", "me2day_naverapp")

terms(lda, 15)
topics(lda, 2)


######################################
# LSA(Latent Semantic Analysis)
######################################

#####################
# Android Market
#####################
lsa <- runLSA("androidmarket_navertalk.txt", "androidmarket_navertalk")
associate(lsa, "³×ÀÌ¹öÅå", threshold = 0.7)
associate(lsa, "µðÀÚÀÎ", threshold = 0.7)

#####################
# Twitter
#####################
lsa <- runLSA("twitter_navertalk.txt", "androidmarket_navertalk")
associate(lsa, "³×ÀÌ¹öÅå", threshold = 0.7)

#####################
# Me2DAY
#####################
lsa <- runLSA("me2day_navertalk.txt", "me2day_navertalk")
associate(lsa, "³×ÀÌ¹öÅå", threshold = 0.7)

lsa <- runLSA("me2day_kakaotalk.txt", "me2day_kakaotalk")
associate(lsa, "Ä«Ä«¿ÀÅå", threshold = 0.7)

lsa <- runLSA("me2day_naverapp.txt", "me2day_naverapp")
associate(lsa, "³×ÀÌ¹ö¾Û", threshold = 0.7)

