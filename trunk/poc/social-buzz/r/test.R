# TODO: Add comment
# 
# Author: Younggue Bae
###############################################################################

library(tm)
library(Rstem)
library(Snowball)

Sys.setlocale('LC_ALL','C') 

texts <- c("나는 정말 행복합니다. 너는 어때?")

data("acq")
acq[[10]]
stemDoc(acq[[10]])

library("openNLP")
tagPOS(acq[[10]])
tagPOS(texts[[1]])

data("crude")
crudeTDM <- DocumentTermMatrix(crude, control = list(stopwords = TRUE))
findFreqTerms(crudeTDM, 10)

