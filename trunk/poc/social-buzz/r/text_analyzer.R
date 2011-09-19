# Analyzes documents of TV program.
# 
# Author: Younggue Bae
###############################################################################

setwd("D:/workspace/social-buzz/data")

library(tm)

###
# Gets the my stopwords.
# @param program.id
# @return vector of stopwords
###
GetStopwords <- function(program.id) {	
	my.stopwords.table = read.table(file("stopwords.txt", encoding = "UTF-8"),
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

PlotTermFreq <- function (progaram.id, tf.matrix, title) {
	require(grDevices); # for colors
	#x <- sort(v[1:10,], decreasing=FALSE)
	x <- sort(tf.matrix[,], decreasing=FALSE)	
	title <- paste(title, " (", progaram.id, ")", sep = "")
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
# Analyzes the term frequency and plots the result.
# @param program.id
# @param dir
# @param filename - input file
###
AnalyzeTermFrequency <- function(program.id, dir, filename) {

#	program.id <- "sbs_besideme"
#	dir <- "./20110822-20110828/"
#	filename <- "src_sbs_besideme.txt"
	
	filename <- paste(dir, filename, sep = "")
	
	mydata.table = read.table(file(filename, encoding = "UTF-8"),
			sep = "\t", header = TRUE, stringsAsFactors = TRUE)
	
	if (nrow(mydata.table) > 0) { 
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
		write(dict, paste(dir, "dic_", program.id, ".txt", sep = ""), sep = "\t")
		
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
		write.csv(top2.matrix, file=paste(dir, "dtm_", program.id, ".csv", sep = ""), row.names = TRUE)
		
		# find most frequently mentioned terms
		#tags <- sort(findFreqTerms(mydata.dtm2, lowfreq = 1, highfreq = 200)); tags[1:10]
		
		
		#numwords <- ncol(mydata.dtm2)
		#v <- as.matrix(sort(sapply(top2.matrix, sum), decreasing = TRUE)[1:numwords], colnames = count); v[1:numwords]
		v <- as.matrix(sort(colSums(top2.matrix), decreasing = TRUE)); v	
		w <- rownames(v); length(w); w
		colnames(v) <- c("tf")
		
		# export tf sum
		write.csv(v, file=paste(dir, "tf_", program.id, ".csv", sep = ""), row.names = TRUE)
		
		PlotTermFreq(program.id, v, "Frequency of Terms")
		
#		mydata.dtm3 <- removeSparseTerms(mydata.dtm, sparse = 0.8)
#		PlotDendrogram(mydata.dtm3)
	}	
}


###
# Analyzes the sentiment terms and plots the result.
# @param program.id
# @param dir
# @param filename - input file
###
AnalyzeSentimentTerms <- function(program.id, dir, filename) {

#	program.id <- "mbc_urpretty"
#	dir <- "./20110815-20110821/"
#	filename <- "liwc_mbc_urpretty.txt"

	filename <- paste(dir, filename, sep = "")
	
	mydata.table = read.table(file(filename, encoding = "UTF-8"),
			sep = "\t", header = TRUE, stringsAsFactors = TRUE)
	
	head(mydata.table)
	
	mydata.table.subset = subset(mydata.table, 
			subset = (mydata.table$feature == "SWEAR" | 
						mydata.table$feature == "POSITIVE" | 
						mydata.table$feature == "NEGATIVE" | 
						mydata.table$feature == "ANGER" | 
						mydata.table$feature == "ANXIETY" |
						mydata.table$feature == "SADNESS"))
	
	if (nrow(mydata.table.subset) > 0) {
		mydata.word <- data.frame(textCol = mydata.table.subset$word)
		
		mydata.source <- DataframeSource(mydata.word)
		
		mydata.corpus <- Corpus(mydata.source)
		
		# make each letter lowercase
		mydata.corpus <- tm_map(mydata.corpus, tolower)
		
		mydata.dtm <- DocumentTermMatrix(mydata.corpus,
				control = list(weighting = weightTf, minWordLength = 1,
						stopwords = TRUE))
		print(mydata.dtm)
		
		mydata.dtm2 <- removeSparseTerms(mydata.dtm, sparse = 0.99)
		nrow(mydata.dtm2); ncol(mydata.dtm2)
		
		# export dtm
		mydata.matrix <- as.matrix(mydata.dtm2)
		v <- as.matrix(sort(colSums(mydata.matrix), decreasing = TRUE)); v	
		w <- rownames(v); length(w); w
		colnames(v) <- c("tf")
		
		# export tf sum
		write.csv(v, file=paste(dir, "/", "stf_", program.id, ".csv", sep = ""), row.names = TRUE)
		
		PlotTermFreq(program.id, v, "Sentiment")
	}
	else {
		print("Oops! Row count is zero.")
	}
	
}





###
# Execute 
###

programs <- c(
	"kbs1_greatking",
	"kbs_homewomen",
	"kbs2_princess",
#	"kbs2_spy",
	"kbs2_ojakkyo",
	"mbc_gyebaek",
#	"mbc_fallinlove",
	"mbc_urpretty",
	"mbc_thousand",
	"sbs_besideme",
	"sbs_dangsin",
	"sbs_baekdongsoo",
	"sbs_boss",
	"sbs_scent",
	"kbs2_gagcon",
	"kbs2_happysunday_1bak2il",
	"kbs2_happysunday_men",
	"sbs_happytogether",
	"mbc_challenge",
	"mbc_three",
	"mbc_wedding",
#	"mbc_sundaynight_house",
	"mbc_sundaynight_nagasoo",
	"sbs_strongheart",
	"sbs_starking",
	"sbs_newsunday"
)

for (program in programs) {
	print(program)
	AnalyzeTermFrequency(program, "./20110905-20110911/", paste("src_", program, ".txt", sep = ""))
	AnalyzeSentimentTerms(program, "./20110905-20110911/", paste("liwc_", program, ".txt", sep = ""))
}


