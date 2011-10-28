# TODO: Add comment
# 
# Author: Younggue Bae
###############################################################################

library(tm)
library(Rstem)
library(Snowball)

docs <- c("aa bb ccc?", "eee ff ggg hh iii")

v <- VectorSource(docs);
c <- Corpus(v)
c <- tm_map(c, stemDocument)

# make each letter lowercase
c <- tm_map(c, tolower)

# remove punctuation
c <- tm_map(c, removePunctuation)	

mydata.dtm <- DocumentTermMatrix(c,
		control = list(weighting = weightTf, minWordLength = 1,
				stopwords = TRUE))
print(mydata.dtm)

dict <- Dictionary(mydata.dtm)


data("acq")
acq[[10]]
stemDoc(acq[[10]])

library("openNLP")
tagPOS(acq[[10]])
tagPOS(texts[[1]])

data("crude")
crudeTDM <- DocumentTermMatrix(crude, control = list(stopwords = TRUE))
findFreqTerms(crudeTDM, 10)

