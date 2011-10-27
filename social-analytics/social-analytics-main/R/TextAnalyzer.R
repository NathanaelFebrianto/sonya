# Analyzes documents.
# 
# Author: Younggue Bae
###############################################################################

setwd("D:/dev/workspace/social-analytics-main/R")

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

library(RTextTools)
library(topicmodels)

data <- read_data(system.file("data/NYTimes.csv.gz",package="RTextTools"),type="csv")
data <- data[sample(1:3100,size=100,replace=FALSE),]
matrix <- create_matrix(cbind(data$Title,data$Subject), language="english", 
removeNumbers=TRUE, stemWords=FALSE, weighting=weightTf)

k <- length(unique(data$Topic.Code))
lda <- LDA(matrix, k)
terms(lda)
topics(lda)


############################
library(rKNLP)

sentence <- "철수가 음악에 재능이 없으면서도 노래를 아주 열심히 부르는 것을 영희가 안다."
morphemeList(sentence)
parseTree(sentence)
showParseTree(sentence)

##############################
#library(rKNLP)
library(tm)

data <- read.csv("navertalk.csv", header=TRUE, stringsAsFactors=FALSE)
colnames(data)


mydata.terms <- data.frame(textCol = data$filtered_text)  	
mydata.source <- DataframeSource(mydata.terms)

mydata.corpus <- Corpus(mydata.source)

mydata.corpus <- tm_map(mydata.corpus, tolower)
  	
# remove punctuation
mydata.corpus <- tm_map(mydata.corpus, removePunctuation)	

# remove generic and custom stopwords
all.stopwords <- c(stopwords('english'), GetStopwords())
mydata.corpus <- tm_map(mydata.corpus, removeWords, all.stopwords)

mydata.dtm <- DocumentTermMatrix(mydata.corpus,
  			control = list(weighting = weightTf, minWordLength = 2,
						stopwords = TRUE))
print(mydata.dtm)

k <- 10
lda <- LDA(mydata.dtm, k)
terms(lda, 10)
topics(lda, 2)


