/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.topic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.firebird.analyzer.util.JobLogger;
import org.firebird.common.util.ConvertUtil;

/**
 * This class creates an index with Lucene from the text files.
 * 
 * @author Young-Gue Bae
 */
public class DocIndexWriter {

	/** 
	 * An english sotp words set from 
	 * http://www.ranks.nl/tools/stopwords.html
	 * http://www.onjava.com/onjava/2003/01/15/examples/EnglishStopWords.txt
	 */
	public static final Set<?> ENGLISH_STOP_WORDS_SET;

	static {
		final List<String> stopWords = Arrays.asList(
				 "a"
				, "able"
				, "about"
				, "above"
				, "absolutely"
				, "abst"
				, "accordance"
				, "according"
				, "accordingly"
				, "across"
				, "act"
				, "actually"
				, "added"
				, "adj"
				, "adopted"
				, "affected"
				, "affecting"
				, "affects"
				, "after"
				, "afterwards"
				, "again"
				, "against"
				, "ago"
				, "agree"
				, "agreed"
				, "ah"
				, "al"
				, "all"
				, "almost"
				, "alone"
				, "along"
				, "already"
				, "also"
				, "although"
				, "always"
				, "am"
				, "among"
				, "amongst"
				, "an"
				, "and"
				, "announce"
				, "another"
				, "any"
				, "anybody"
				, "anyhow"
				, "anymore"
				, "anyone"
				, "anything"
				, "anyway"
				, "anyways"
				, "anywhere"
				, "apparently"
				, "approximately"
				, "are"
				, "aren"
				, "arent"
				, "aren't"
				, "arise"
				, "around"
				, "as"
				, "aside"
				, "ask"
				, "asking"
				, "at"
				, "auth"
				, "available"
				, "away"
				, "awesome"
				, "awfully"
				, "b"
				, "back"
				, "be"
				, "became"
				, "because"
				, "become"
				, "becomes"
				, "becoming"
				, "been"
				, "before"
				, "beforehand"
				, "begin"
				, "beginning"
				, "beginnings"
				, "begins"
				, "behind"
				, "being"
				, "believe"
				, "below"
				, "beside"
				, "besides"
				, "bet"
				, "between"
				, "beyond"
				, "biol"
				, "bit"
				, "both"
				, "bp"
				, "brief"
				, "briefly"
				, "btw"
				, "but"
				, "by"
				, "bye"
				, "c"
				, "ca"
				, "called"
				, "came"
				, "can"
				, "cannot"
				, "cant"
				, "can't"
				, "cause"
				, "causes"
				, "cc"
				, "certain"
				, "certainly"
				, "co"
				, "com"
				, "come"
				, "comes"
				, "completely"
				, "con"
				, "contain"
				, "containing"
				, "contains"
				, "could"
				, "couldn"
				, "couldnt"
				, "d"
				, "da"
				, "dan"
				, "date"
				, "dc"
				, "de"
				, "dear"
				, "definitely"
				, "detail"
				, "details"
				, "did"
				, "didn"
				, "didnt"
				, "didn't"
				, "different"
				, "dm"
				, "do"
				, "does"
				, "doesn"
				, "doesnt"
				, "doesn't"
				, "doing"
				, "don"
				, "done"
				, "dont"
				, "don't"
				, "down"
				, "downwards"
				, "dude"
				, "due"
				, "during"
				, "e"
				, "each"
				, "ed"
				, "edu"
				, "effect"
				, "eg"
				, "eight"
				, "eighty"
				, "either"
				, "else"
				, "elsewhere"
				, "em"
				, "en"
				, "end"
				, "ending"
				, "enough"
				, "era"
				, "especially"
				, "est"
				, "et"
				, "et-al"
				, "etc"
				, "eu"
				, "even"
				, "event"
				, "ever"
				, "every"
				, "everybody"
				, "everyone"
				, "everything"
				, "everywhere"
				, "ex"
				, "exactly"
				, "except"
				, "f"
				, "fact"
				, "fact"
				, "far"
				, "feedback"
				, "feel"
				, "feels"
				, "felt"
				, "few"
				, "ff"
				, "fi"
				, "fifth"
				, "finally"
				, "finished"
				, "first"
				, "five"
				, "fix"
				, "followed"
				, "following"
				, "follows"
				, "for"
				, "former"
				, "formerly"
				, "forth"
				, "forward"
				, "found"
				, "four"
				, "francisco"
				, "from"
				, "ft"
				, "further"
				, "furthermore"
				, "g"
				, "gave"
				, "get"
				, "gets"
				, "getting"
				, "give"
				, "given"
				, "gives"
				, "giving"
				, "glad"
				, "go"
				, "goes"
				, "going"
				, "gone"
				, "gonna"
				, "good"
				, "got"
				, "gotta"
				, "gotten"
				, "guess"
				, "h"
				, "ha"
				, "ha"
				, "had"
				, "haha"
				, "hanging"
				, "happens"
				, "hardly"
				, "has"
				, "hasn"
				, "hasnt"
				, "have"
				, "havent"
				, "having"
				, "he"
				, "heading"
				, "hed"
				, "hello"
				, "hence"
				, "her"
				, "here"
				, "hereafter"
				, "hereby"
				, "herein"
				, "heres"
				, "hereupon"
				, "hers"
				, "herself"
				, "hes"
				, "hey"
				, "hi"
				, "hid"
				, "him"
				, "himself"
				, "his"
				, "hither"
				, "hmm"
				, "home"
				, "hope"
				, "how"
				, "howbeit"
				, "however"
				, "hr"
				, "http"
				, "hundred"
				, "i"
				, "id"
				, "ie"
				, "if"
				, "ii"
				, "i'll"
				, "im"
				, "i'm"
				, "immediate"
				, "immediately"
				, "importance"
				, "important"
				, "in"
				, "inc"
				, "indeed"
				, "index"
				, "information"
				, "instead"
				, "into"
				, "invention"
				, "inward"
				, "is"
				, "isn"
				, "isnt"
				, "isn't"
				, "it"
				, "itd"
				, "it'll"
				, "its"
				, "itself"
				, "i've"
				, "j"
				, "just"
				, "justin"
				, "k"
				, "keep"
				, "keeps"
				, "kept"
				, "keys"
				, "kg"
				, "km"
				, "knew"
				, "know"
				, "known"
				, "knows"
				, "l"
				, "la"
				, "largely"
				, "last"
				, "lately"
				, "later"
				, "latter"
				, "latterly"
				, "least"
				, "led"
				, "less"
				, "lest"
				, "let"
				, "lets"
				, "like"
				, "liked"
				, "likely"
				, "line"
				, "little"
				, "'ll"
				, "lol"
				, "look"
				, "looking"
				, "looks"
				, "los"
				, "lot"
				, "lots"
				, "ltd"
				, "ly"
				, "m"
				, "ma"
				, "made"
				, "mainly"
				, "make"
				, "makes"
				, "many"
				, "may"
				, "maybe"
				, "me"
				, "mean"
				, "means"
				, "meantime"
				, "meanwhile"
				, "merely"
				, "mg"
				, "michael"
				, "might"
				, "million"
				, "miss"
				, "ml"
				, "more"
				, "moreover"
				, "most"
				, "mostly"
				, "mp"
				, "mr"
				, "mrs"
				, "much"
				, "mug"
				, "must"
				, "my"
				, "myself"
				, "n"
				, "na"
				, "name"
				, "namely"
				, "nay"
				, "nd"
				, "near"
				, "nearly"
				, "necessarily"
				, "necessary"
				, "need"
				, "needs"
				, "neither"
				, "never"
				, "nevertheless"
				, "new"
				, "next"
				, "nice"
				, "nice"
				, "nine"
				, "ninety"
				, "no"
				, "nobody"
				, "non"
				, "none"
				, "nonetheless"
				, "noone"
				, "nor"
				, "normally"
				, "nos"
				, "not"
				, "noted"
				, "nothing"
				, "now"
				, "nowhere"
				, "o"
				, "obtain"
				, "obtained"
				, "obviously"
				, "of"
				, "off"
				, "often"
				, "oh"
				, "ok"
				, "okay"
				, "old"
				, "omg"
				, "omitted"
				, "on"
				, "once"
				, "one"
				, "ones"
				, "only"
				, "onto"
				, "oops"
				, "or"
				, "ord"
				, "org"
				, "os"
				, "other"
				, "others"
				, "otherwise"
				, "ought"
				, "our"
				, "ours"
				, "ourselves"
				, "out"
				, "outside"
				, "over"
				, "overall"
				, "owing"
				, "own"
				, "p"
				, "pa"
				, "page"
				, "pages"
				, "part"
				, "particular"
				, "particularly"
				, "past"
				, "per"
				, "perhaps"
				, "placed"
				, "please"
				, "plus"
				, "pm"
				, "point"
				, "poorly"
				, "possible"
				, "possibly"
				, "potentially"
				, "pp"
				, "pr"
				, "predominantly"
				, "present"
				, "previously"
				, "primarily"
				, "pro"
				, "probably"
				, "promptly"
				, "proud"
				, "provides"
				, "put"
				, "q"
				, "que"
				, "quickly"
				, "quite"
				, "qv"
				, "r"
				, "ran"
				, "rather"
				, "rd"
				, "re"
				, "readily"
				, "ready"
				, "really"
				, "recent"
				, "recently"
				, "ref"
				, "refs"
				, "regarding"
				, "regardless"
				, "regards"
				, "related"
				, "relatively"
				, "research"
				, "respectively"
				, "resulted"
				, "resulting"
				, "results"
				, "right"
				, "rt"
				, "run"
				, "s"
				, "said"
				, "same"
				, "san"
				, "san"
				, "saw"
				, "say"
				, "saying"
				, "says"
				, "se"
				, "sec"
				, "section"
				, "see"
				, "seeing"
				, "seem"
				, "seemed"
				, "seeming"
				, "seems"
				, "seen"
				, "sees"
				, "self"
				, "selves"
				, "send"
				, "sense"
				, "sent"
				, "seriously"
				, "set"
				, "sets"
				, "seven"
				, "several"
				, "sf"
				, "shall"
				, "she"
				, "shed"
				, "she'll"
				, "shes"
				, "should"
				, "shouldn"
				, "shouldnt"
				, "show"
				, "showed"
				, "shown"
				, "showns"
				, "shows"
				, "significant"
				, "significantly"
				, "similar"
				, "similarly"
				, "since"
				, "sir"
				, "six"
				, "slightly"
				, "so"
				, "some"
				, "somebody"
				, "somehow"
				, "someone"
				, "somethan"
				, "something"
				, "sometime"
				, "sometimes"
				, "somewhat"
				, "somewhere"
				, "soon"
				, "sorry"
				, "specifically"
				, "specified"
				, "specify"
				, "specifying"
				, "state"
				, "states"
				, "still"
				, "stop"
				, "strongly"
				, "stuff"
				, "sub"
				, "substantially"
				, "successfully"
				, "such"
				, "sucks"
				, "sufficiently"
				, "suggest"
				, "sup"
				, "sure"
				, "t"
				, "take"
				, "taken"
				, "takes"
				, "taking"
				, "tell"
				, "tends"
				, "th"
				, "than"
				, "thank"
				, "thanks"
				, "thanx"
				, "that"
				, "that'll"
				, "thats"
				, "that've"
				, "the"
				, "their"
				, "theirs"
				, "them"
				, "themselves"
				, "then"
				, "thence"
				, "there"
				, "thereafter"
				, "thereby"
				, "thered"
				, "therefore"
				, "therein"
				, "there'll"
				, "thereof"
				, "therere"
				, "theres"
				, "thereto"
				, "thereupon"
				, "there've"
				, "these"
				, "they"
				, "theyd"
				, "they'll"
				, "theyre"
				, "they've"
				, "thing"
				, "things"
				, "think"
				, "this"
				, "tho"
				, "those"
				, "thou"
				, "though"
				, "thoughh"
				, "thought"
				, "thousand"
				, "throug"
				, "through"
				, "throughout"
				, "thru"
				, "thus"
				, "thx"
				, "til"
				, "till"
				, "till"
				, "tip"
				, "to"
				, "together"
				, "tom"
				, "too"
				, "took"
				, "totally"
				, "toward"
				, "towards"
				, "tried"
				, "tries"
				, "truly"
				, "try"
				, "trying"
				, "ts"
				, "twice"
				, "two"
				, "u"
				, "uk"
				, "um"
				, "un"
				, "under"
				, "unfortunately"
				, "unless"
				, "unlike"
				, "unlikely"
				, "until"
				, "unto"
				, "up"
				, "upon"
				, "ups"
				, "ur"
				, "us"
				, "use"
				, "used"
				, "useful"
				, "usefully"
				, "usefulness"
				, "uses"
				, "using"
				, "usually"
				, "v"
				, "value"
				, "various"
				, "'ve"
				, "very"
				, "via"
				, "viz"
				, "vol"
				, "vols"
				, "vs"
				, "w"
				, "wanna"
				, "want"
				, "wanted"
				, "wants"
				, "was"
				, "wasn"
				, "wasnt"
				, "way"
				, "we"
				, "wed"
				, "weird"
				, "welcome"
				, "we'll"
				, "went"
				, "were"
				, "we're"
				, "werent"
				, "we've"
				, "what"
				, "whatever"
				, "what'll"
				, "whats"
				, "when"
				, "whence"
				, "whenever"
				, "where"
				, "whereafter"
				, "whereas"
				, "whereby"
				, "wherein"
				, "wheres"
				, "whereupon"
				, "wherever"
				, "whether"
				, "which"
				, "while"
				, "whim"
				, "whither"
				, "who"
				, "whod"
				, "whoever"
				, "whole"
				, "who'll"
				, "whom"
				, "whomever"
				, "whos"
				, "whose"
				, "why"
				, "widely"
				, "will"
				, "willing"
				, "wish"
				, "with"
				, "within"
				, "without"
				, "won"
				, "wondering"
				, "wont"
				, "words"
				, "world"
				, "worse"
				, "would"
				, "wouldn"
				, "wouldnt"
				, "wow"
				, "wrong"
				, "wtf"
				, "www"
				, "x"
				, "y"
				, "ya"
				, "yay"
				, "yeah"
				, "yep"
				, "yes"
				, "yet"
				, "yo"
				, "york"
				, "you"
				, "youd"
				, "you'll"
				, "your"
				, "youre"
				, "you're"
				, "yours"
				, "yourself"
				, "yourselves"
				, "you've"
				, "z"
				, "zero"
		);
		final CharArraySet stopSet = new CharArraySet(stopWords.size(), false);
		stopSet.addAll(stopWords);
		ENGLISH_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
	}
	
	/** logger */
	private static JobLogger logger = JobLogger.getLogger(DocIndexWriter.class);
	
	/**
	 * Constructor.
	 * 
	 */
	public DocIndexWriter() {}

	/**
	 * Creates an index with Lucene from the text files.
	 * 
	 * @param inputDir the file directory that contains the text files to be indexed
	 * @param ouputDir the index directory that hosts Lucene's index files
	 * @exception
	 */
	public void write(String inputDir, String ouputDir) throws Exception {
		File fileDir = new File(inputDir);
		FSDirectory indexDir = FSDirectory.open(new File(ouputDir));
		
		//Analyzer luceneAnalyzer = new StandardAnalyzer(Version.LUCENE_CURRENT, ENGLISH_STOP_WORDS_SET);
		Analyzer luceneAnalyzer = new StopAnalyzer(Version.LUCENE_CURRENT, ENGLISH_STOP_WORDS_SET);
		IndexWriter indexWriter = new IndexWriter(indexDir, luceneAnalyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
		File[] textFiles = fileDir.listFiles();
		
		// add documents to the index
		for (int i = 0; i < textFiles.length; i++) {
			if (textFiles[i].getName().endsWith(".txt")) {
				logger.info("creating index : " + textFiles[i].getCanonicalPath());
				
				Reader textReader = new FileReader(textFiles[i]);
				
		        // first line is the user id, rest from 3rd line is the body
		        BufferedReader reader = new BufferedReader(textReader);
		        String timeline = reader.readLine();
		        String userId = reader.readLine();
		        logger.info("timeline == " + timeline);
		        logger.info("userId == " + userId);
		        reader.readLine();// skip an empty line

		        StringBuffer bodyBuffer = new StringBuffer(1024);
		        String line = null;
		        while ((line = reader.readLine()) != null) {
		        	bodyBuffer.append(line).append(' ');
		        }
		        reader.close();
		        
		        logger.info("body == " + bodyBuffer.toString()+ "\n");
		        
				Document document = new Document();
				document.add(new Field("user", userId, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
				document.add(new Field("body", bodyBuffer.toString(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
				document.add(new Field("path", textFiles[i].getPath(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
				document.add(new Field("timeline", timeline, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
				document.add(new Field("indexDate", ConvertUtil.convertDateToString("yyyyMMdd", new Date()), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
				indexWriter.addDocument(document);
			}
		}

		indexWriter.optimize();
		indexWriter.close();
	}
	
}
