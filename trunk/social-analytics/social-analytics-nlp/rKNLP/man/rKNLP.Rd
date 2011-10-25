\name{rKNLP}
\alias{rKNLP}
%- Also NEED an '\alias' for EACH other topic documented here.
\title{ rKNLP - Korean Natural Language Processing for R }
\description{
rKNLP package is for analyzing Korean Natural Language Processing.
}
\usage{
morphemeList(text)
morphemes(text)
parseTree(text)
showParseTree(text)
}
%- maybe also 'usage' for other objects documented here.
\arguments{
\item{morphemes}{ ~~Describe \code{analyzeMorpheme} here~~ }
}
\details{
11
~~ If necessary, more details than the __description__ above ~~
}
\value{
~Describe the value returned
If it is a LIST, use
\item{comp1 }{Description of 'comp1'}
\item{comp2 }{Description of 'comp2'}
...
}
\references{ ~put references to the literature/web site here ~ }
\author{ ~~who you are~~ }
\note{ ~~further notes~~ }
~Make other sections like Warning with \section{Warning }{....} ~
\seealso{ ~~objects to See Also as \code{\link{~~fun~~}}, ~~~ }
\examples{
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
}
\keyword{ ~kwd1 }% at least one, from doc/KEYWORDS
\keyword{ ~kwd2 }% __ONLY ONE__ keyword per line