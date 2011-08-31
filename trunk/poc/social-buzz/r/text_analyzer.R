# Analyzes documents of TV program.
# 
# Author: Younggue Bae
###############################################################################

setwd("D:/workspace/social-buzz/output")

library(tm)

###
# Gets the my stopwords.
# @param program.id
# @return vector of stopwords
###
GetStopwords <- function(program.id) {	
	my.stopwords.table = read.table(file("_stopwords.txt", encoding = "UTF-8"),
			header = TRUE, comment.char = "#", sep = ",",
			stringsAsFactors = FALSE, na.strings = "")
	
	my.stopwords.subset = subset(my.stopwords.table, 
			select = c("type", "stopword"),
			subset = (my.stopwords.table$type == "common" | 
					  my.stopwords.table$type == "tag" | 
					  my.stopwords.table$type == "me2day" | 
					  my.stopwords.table$type == program.id))
	my.stopwords <- my.stopwords.subset$stopword
	
	return (my.stopwords)
}

###
# Analyzes the document and plots the result.
# @param program.id
# @param filename - input file
###
AnalyzeDocument <- function(program.id, filename) {
#	filename <- "terms_mbc_challenge.txt"
#	program.id <- "mbc_challenge"
	
	mydata.table = read.table(file(filename, encoding = "UTF-8"),
			sep = "\t", header = TRUE, stringsAsFactors = TRUE)
	
	head(mydata.table)
	
	mydata.terms <- data.frame(textCol = mydata.table$terms)
	
	mydata.source <- DataframeSource(mydata.terms)
	
	mydata.corpus <- Corpus(mydata.source)
	#mydata.corpus <- Corpus(mydata.source, readerControl = list(reader = mydata.source$DefaultReader, language = "en"))
	
	# make each letter lowercase
	mydata.corpus <- tm_map(mydata.corpus, tolower)
		
	# remove punctuation
	mydata.corpus <- tm_map(mydata.corpus, removePunctuation)	
	
	# remove generic and custom stopwords
	all.stopwords <- c(stopwords('english'), GetStopwords(program.id))
	mydata.corpus <- tm_map(mydata.corpus, removeWords, all.stopwords)
	
	# build a term-document matrix
	#mydata.dtm = DocumentTermMatrix(mydata.corpus, control = list(stopwords = TRUE))
	
	mydata.dtm <- DocumentTermMatrix(mydata.corpus,
			control = list(weighting = weightTf, minWordLength = 2,
					stopwords = TRUE))
	print(mydata.dtm)
	
	#freq.terms = findFreqTerms(mydata.dtm, lowfreq = 10)
	#print(freq.terms)
	
	dict <- Dictionary(mydata.dtm)
	
	# write output files
	#write(freq.terms, paste("term_", program.id, ".txt", sep = ""), sep = "\t")
	write(dict, paste("dict_", program.id, ".txt", sep = ""), sep = "\t")
	
	#splot(mydata.dtm, terms = freq.terms[1:25], corThreshold = 0.5)
	
	# find words correlated with the themes
	#findAssocs(mydata.dtm, "cry", 0.20)
	
	
	
	
	# remove sparse terms to simplify the cluster plot
	# Note: tweak the sparse parameter to determine the number of words.
	# About 10-30 words is good.
	mydata.dtm2 <- removeSparseTerms(mydata.dtm, sparse = 0.98)
	nrow(mydata.dtm2); ncol(mydata.dtm2)
	
	# export dtm
	top2.matrix <- as.matrix(mydata.dtm2)
	write.csv(top2.matrix, file=paste("top_", program.id, ".csv", sep = ""), row.names = TRUE)
	
	# find most frequently mentioned terms
	#tags <- sort(findFreqTerms(mydata.dtm2, lowfreq = 1, highfreq = 200)); tags[1:10]
	
	
	#numwords <- ncol(mydata.dtm2)
	#v <- as.matrix(sort(sapply(top2.matrix, sum), decreasing = TRUE)[1:numwords], colnames = count); v[1:numwords]
	v <- as.matrix(sort(colSums(top2.matrix), decreasing = TRUE)); v	
	w <- rownames(v); length(w); w
	
	# export tf sum
	write.csv(v, file=paste("tf_", program.id, ".csv", sep = ""), row.names = TRUE)
	
	PlotTermFreq(program.id, v)
	
#	mydata.dtm3 <- removeSparseTerms(mydata.dtm, sparse = 0.8)
#	PlotDendrogram(mydata.dtm3)
	
}


PlotTermFreq <- function (progaram.id, tf.matrix) {
	require(grDevices); # for colors
	#x <- sort(v[1:10,], decreasing=FALSE)
	x <- sort(tf.matrix[,], decreasing=FALSE)	
	title <- paste("Frequency of Terms (", progaram.id, ")", sep = "")
	barplot(x, horiz=TRUE, cex.names = 0.70, space = 1, las = 1, col = grey.colors(length(x)), main = title)
	
	#install.packages('snippets',,'http://www.rforge.net/')
	#require(snippets)
	#v <- sapply(tf.matrix, sum); v
	#cloud(v, col = col.bbr(v, fit = TRUE))
}


PlotDendrogram <- function (dtm) {
	# convert the sparse term-document matrix to a standard data frame
	mydata.df <- as.data.frame(inspect(dtm))
	
	# inspect dimensions of the data frame
	nrow(mydata.df)
	ncol(mydata.df)
	
	mydata.df.scale <- scale(mydata.df)
	d <- dist(mydata.df.scale, method = "euclidean") # distance matrix
	fit <- hclust(d, method="ward")
	plot(fit) # display dendogram?
	
	groups <- cutree(fit, k=5) # cut tree into 5 clusters
	# draw dendogram with red borders around the 5 clusters
	rect.hclust(fit, k=5, border="red")
	
	## Plot associations between terms	
	#plot(mydata.dtm2, findFreqTerms(dtm, 6),
	#		attrs=list(graph=list(),
	#			node=list(shape="rectangle", fontsize="120", fixedsize="false")))
}


###
# Execute 
###

#AnalyzeDocument("kbs1_greatking", "terms_kbs1_greatking.txt")
#AnalyzeDocument("kbs2_ojakkyo", "terms_kbs2_ojakkyo.txt")
#AnalyzeDocument("mbc_thousand", "terms_mbc_thousand.txt")
#AnalyzeDocument("sbs_besideme", "terms_sbs_besideme.txt")
AnalyzeDocument("kbs2_princess", "terms_kbs2_princess.txt")
#AnalyzeDocument("mbc_fallinlove", "terms_mbc_fallinlove.txt")
#AnalyzeDocument("sbs_boss", "terms_sbs_boss.txt")
#AnalyzeDocument("kbs2_spy", "terms_kbs2_spy.txt")
#AnalyzeDocument("mbc_gyebaek", "terms_mbc_gyebaek.txt")
#AnalyzeDocument("sbs_baekdongsoo", "terms_sbs_baekdongsoo.txt")
#AnalyzeDocument("mbc_wedding", "terms_mbc_wedding.txt")
#AnalyzeDocument("mbc_challenge", "terms_mbc_challenge.txt")
#AnalyzeDocument("sbs_starking", "terms_sbs_starking.txt")
#AnalyzeDocument("kbs2_happysunday_1bak2il", "terms_kbs2_happysunday_1bak2il.txt")
#AnalyzeDocument("kbs2_happysunday_men", "terms_kbs2_happysunday_men.txt")
#AnalyzeDocument("mbc_sundaynight_nagasoo", "terms_mbc_sundaynight_nagasoo.txt")
#AnalyzeDocument("mbc_sundaynight_house", "terms_mbc_sundaynight_house.txt")
#AnalyzeDocument("sbs_newsunday", "terms_sbs_newsunday.txt")


