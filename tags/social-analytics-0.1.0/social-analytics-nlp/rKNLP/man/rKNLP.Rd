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
extratTerms(text)
}
%- maybe also 'usage' for other objects documented here.
\arguments{
\item{text}{ the string text to analyze }
}
\details{
....
}
\value{
Functions such as morphemes, parseTree return JSON format strings.
%\item{comp1 }{Description of 'comp1'}
%\item{comp2 }{Description of 'comp2'}
...
}
\references{ .... }
\author{ Younggue Bae(louiezzang@hotmail.com) }
\note{ .... }
~Make other sections like Warning with \section{Warning }{....} ~
\seealso{ See Also as \code{\link{...}} }
\examples{
## Sample codes here!

x1 <- morphemes("write your sentence to analyze...") # return json formt string
x2 <- parseTree("write your sentence to analyze...") # return json formt string
x3 <- extractTerms("write your sentence to analyze...") # return vector of terms
showParseTree("write your sentence to analyze...")

}
\keyword{ NLP }% at least one, from doc/KEYWORDS
\keyword{ textmining }% __ONLY ONE__ keyword per line