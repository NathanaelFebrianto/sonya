
analyzeMorpheme <- function(text) {
	jo <- .jnew("com.nhn.socialanalytics.nlp.kr.r.Rknlp")
	out <- .jcall(jo, "[S", "analyzeMorpheme", text)
	return(out)
}

showParseTree <- function(text) {
	jo <- .jnew("com.nhn.socialanalytics.nlp.kr.r.Rknlp")
	out <- .jcall(jo, "V", "showParseTree", text)
}