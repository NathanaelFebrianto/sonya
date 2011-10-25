
morphemeList <- function(text) {
	jo <- .jnew("com.nhn.socialanalytics.nlp.kr.r.Rknlp")
	out <- .jcall(jo, "[S", "morphemeList", text)
	return(out)
}

morphemes <- function(text) {
	jo <- .jnew("com.nhn.socialanalytics.nlp.kr.r.Rknlp")
	out <- .jcall(jo, "S", "morphemes", text)
	return(out)
}

parseTree <- function(text) {
	jo <- .jnew("com.nhn.socialanalytics.nlp.kr.r.Rknlp")
	out <- .jcall(jo, "S", "parseTree", text)
	return(out)
}

showParseTree <- function(text) {
	jo <- .jnew("com.nhn.socialanalytics.nlp.kr.r.Rknlp")
	out <- .jcall(jo, "V", "showParseTree", text)
}