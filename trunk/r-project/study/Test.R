# TODO: Add comment
# 
# Author: Younggue Bae
###############################################################################



##########################
# Data Structures
##########################

# vector: same data types
v <- c(10, 20, 30)
names(v) <- c("A", "B", "C")
print(v)
v["A"]

# list: diffent data types
t <- list("A", "B", 1, 2)
print(t)
t[2]
t[[2]]
class(t[2])
class(t[[2]])

years <- list(Kennedy=1960, Johnson=1964, Carter=1976, Clinton=1994)
years[["Kennedy"]]

# factor, level
f <- factor(c("Mon", "Tue", "Wed", "Mon", "Fri"))
f
f <- factor(c("Mon", "Tue", "Wed", "Mon", "Fri"), c("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"))
f

# matrix
e <- c(1, 2, 3,
	   4, 5, 6)
mat <- matrix(e, 2, 3)
mat <- matrix(e, 2, 3, byrow=TRUE)
mat[1,]

# data frame
audiences <- read.csv("twitter_audiences.csv")
head(audiences)
summary(audiences)

populars <- read.csv("twitter_populars.csv")
head(populars)

audiences[[1]]
audiences[,1]
audiences$target_user
audiences[["target_user"]]

audiences[1]

class(audiences[[1]])
class(audiences[1])

audiences[, c(1,2,3)]
audiences[, c("target_user","create_date")]

edit(audiences)

# data frame -> subset
subset(audiences, select=c("target_user","create_date"))
subset(audiences, 
	   select=c(target_user, create_date, total_tweet_count), 
	   subset=(total_tweet_count > 20000))

# col names
colnames(audiences)
colnames(populars)
colnames(audiences) <- c("user", "a.create_date", "a.total_tweet_count", "a.total_user_count", "a.positive_tweet_count", "a.positive_user_count", "a.negative_tweet_count", "a.negative_user_count")

# merge, rbind, cbind
m <- merge(audiences, populars, by="user")
head(m)
edit(head(m))

# lapply -> return list, sapply -> return vector, tapply -> grouping by factor
lapply(audiences, mean)
sapply(audiences$total_tweet_count, mean)
tapply(populars$total_tweet_count, populars$user, mean)

# by -> applying a function to group of rows
by(populars, populars$user, summary)

# mapply -> applying a function to parallel vectors or lists for element-wise arguments
gcd <- function(a, b) {
	if (b == 0) return (a)
	else return(gcd(b, a %% b))
}

gcd(c(1,2,3), c(9,6,3))
mapply(gcd, c(1,2,3), c(9,6,3))

######################
# Strings and Dates
######################
nchar("Moe")
length("Moe")

s <- c("Moe", "Larry", "Curly")
nchar(s)
length(s)

paste("A", "B", "C")
paste("A", "B", "C", sep="")

substr("Statistics", 1, 4)

ss <- c("Moe", "Larry", "Curly")
substr(ss, 1, 3)

path <- "/home/louie/data/trials.csv"
strsplit(path, "/")

ss <- "Old will be replaced into Old one."
sub("Old", "New", ss)
gsub("Old", "New", ss)

# generating all pairwise combination strings
locations <- c("NY", "LA", "CHI", "HOU")
treatments <- c("T1", "T2", "T3")
outer(locations, treatments, paste, sep="-")

# date
d <- Sys.Date()
d
class(d)

as.Date("2011-08-08")
as.Date("12/31/2010")
as.Date("12/31/2010", format="%m/%d/%Y")

format(Sys.Date(), format="%m/%d/%Y")

ISOdate(2012,2,29)
as.Date(ISOdate(2012,2,29))

d <- as.Date("2010-03-15")
p <- as.POSIXlt(d)
p$mday	# 1~31
p$mon	# 0 = January
p$year + 1900	# since 1900

########################
# sum
########################
audiences <- read.csv("twitter_audiences.csv")
agg = aggregate(audiences$total_tweet_count,by=list(audiences$target_user),sum)
barplot(agg$x)

