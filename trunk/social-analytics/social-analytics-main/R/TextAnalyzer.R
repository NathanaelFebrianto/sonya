# Analyzes documents.
# 
# Author: Younggue Bae
###############################################################################

setwd("D:/dev/workspace/social-analytics-main/R")

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

