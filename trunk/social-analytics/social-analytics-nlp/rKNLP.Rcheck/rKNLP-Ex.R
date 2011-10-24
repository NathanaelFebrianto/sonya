pkgname <- "rKNLP"
source(file.path(R.home("share"), "R", "examples-header.R"))
options(warn = 1)
options(pager = "console")
library('rKNLP')

assign(".oldSearch", search(), pos = 'CheckExEnv')
cleanEx()
nameEx("rKNLP")
### * rKNLP

flush(stderr()); flush(stdout())

### Name: rKNLP
### Title: ~~function to do ... ~~
### Aliases: rKNLP
### Keywords: ~kwd1 ~kwd2

### ** Examples

##---- Should be DIRECTLY executable !! ----
##-- ==> Define data, use random,
##-- or do help(data=index) for the standard data sets.
## The function is currently defined as
function()
{
	jo <- .jnew("com.nhn.socialanalytics.nlp.kr.r.Rknlp")
	out <- .jcall(jo, "[S", "analyzeMorpheme", text)
	return(out)
}



### * <FOOTER>
###
cat("Time elapsed: ", proc.time() - get("ptime", pos = 'CheckExEnv'),"\n")
grDevices::dev.off()
###
### Local variables: ***
### mode: outline-minor ***
### outline-regexp: "\\(> \\)?### [*]+" ***
### End: ***
quit('no')
