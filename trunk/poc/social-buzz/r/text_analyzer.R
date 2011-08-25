# TODO: Add comment
# 
# Author: Younggue Bae
###############################################################################

setwd("D:/dev/workspace/social-buzz/output")
Sys.setlocale("LC_ALL", "C") 


library(tm)

df = read.table("kbs1_greatking.txt", sep = ",", header = TRUE, stringsAsFactors = FALSE, na.strings = "")
head(df)
d <- data.frame(textCol = df$terms)
ds <- DataframeSource(d)
dsc <- Corpus(ds, readerControl = list(reader = readPlain, language = "en"))
dtm = DocumentTermMatrix(dsc, control = list(stopwords = TRUE))

findFreqTerms(dtm, 3)


dict <- Dictionary(dtm)


