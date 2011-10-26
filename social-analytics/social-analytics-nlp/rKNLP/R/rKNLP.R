
morphemeList <- function(text) {
	jo <- .jnew("com.nhn.socialanalytics.nlp.kr.r.Rknlp")
	out <- .jcall(jo, "[S", "morphemeList", text)
	return(out)
}

morphemes <- function(text) {
	jo <- .jnew("com.nhn.socialanalytics.nlp.kr.r.Rknlp")
	out <- .jcall(jo, "[S", "morphemes", text)
	return(out)
}

parseTree <- function(text) {
	jo <- .jnew("com.nhn.socialanalytics.nlp.kr.r.Rknlp")
	out <- .jcall(jo, "[S", "parseTree", text)
	return(out)
}

showParseTree <- function(text) {
	jo <- .jnew("com.nhn.socialanalytics.nlp.kr.r.Rknlp")
	out <- .jcall(jo, "V", "showParseTree", text)
}

extractTerms <- function(text) {
	library(RJSONIO)
	
	json1 <- morphemes(text)
	json2 <- fromJSON(json1[[1]])
	json3 <- json2[[1]]
	
	for (i in 1 : length(json3)) {
		t <- json3[[i]]$term
		
		if (i == 1)
			a <- c(t)
		else
			a <- c(a, t)
	}	
	return(a)	
}