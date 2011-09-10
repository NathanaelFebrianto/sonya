# TODO: Add comment
# 
# Author: Louie
###############################################################################


library(XML)

url <- "http://www.warwick.ac.uk/statsdept/useR-2011/participant-list.html"

participants <- readHTMLTable(readLines(url), which=1, stringsAsFactors=F)

names(participants) <- c("Name", "Country", "Organisation")

## Correct typo and shortcut

participants$Country <- gsub("Kngdom","Kingdom",participants$Country)

participants$Country <- gsub("USA","United States",participants$Country)

participants$Country <- factor(participants$Country)

partCountry <- as.data.frame(xtabs( ~ Country, data=participants))

library(googleVis)

## Please note the option gvis.editor requires googleVis version >= 0.2.9

G <- gvisGeoChart(partCountry,"Country", "Freq", options=list(gvis.editor="Edit me") )

plot(G)

##########################
library(googleVis)
Fruits ## Example data
M <- gvisMotionChart(Fruits, idvar="Fruit", timevar="Year")
plot(M)
