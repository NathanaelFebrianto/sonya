# TODO: Add comment
# 
# Author: Younggue Bae
###############################################################################

setwd("D:/workspace/social-buzz/output")
Sys.setlocale("LC_ALL", "C") 


library(tm)

df = read.table("kbs2_princess.txt", sep = "\t", header = TRUE, stringsAsFactors = FALSE, na.strings = "")
head(df)
d <- data.frame(textCol = df$terms)
ds <- DataframeSource(d, encoding = "UTF-8")
dsc <- Corpus(ds, readerControl = list(reader = readPlain, language = "en"))

dtm = DocumentTermMatrix(dsc, control = list(stopwords = TRUE))

findFreqTerms(dtm, 50)

dict <- Dictionary(dtm)


