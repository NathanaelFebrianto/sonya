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

############################


data <- read_data(system.file("data/NYTimes.csv.gz",package="RTextTools"),type="csv")
data <- data[sample(1:3100,size=100,replace=FALSE),]
matrix <- create_matrix(cbind(data$Title,data$Subject), language="english", 
removeNumbers=TRUE, stemWords=FALSE, weighting=weightTf)

k <- length(unique(data$Topic.Code))
lda <- LDA(matrix, k)
terms(lda)
topics(lda)


############################


sentence <- "철수가 음악에 재능이 없으면서도 노래를 아주 열심히 부르는 것을 영희가 안다."
morphemeList(sentence)
parseTree(sentence)
showParseTree(sentence)

##############################

data = read.table(file("navertalk.txt", encoding = "UTF-8"),
      header = TRUE, comment.char = "#", sep = "\t",
			stringsAsFactors = FALSE, na.strings = "")
nrow(data)
colnames(data)
head(data)

mydata.terms <- data.frame(textCol = data$filtered_text1) 
nrow(mydata.terms)
head(mydata.terms)
mydata.terms = subset(mydata.terms, 
  		subset = (textCol != ""))
nrow(mydata.terms)
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

k <- 6
lda <- LDA(mydata.dtm, k)
terms(lda, 10)
topics(lda, 2)


