package com.nhn.socialbuzz.textmining.regex;

import java.util.regex.Pattern;

public class Regex {
	  private static final String[] RESERVED_ACTION_WORDS = {""};
	  
	  /* URL related hash regex collection */
	  private static final String URL_VALID_PRECEEDING_CHARS = "(?:[^\\-/\"':!=A-Z0-9_@��]+|^|\\:)";
	  private static final String URL_VALID_DOMAIN = "(?:[^\\p{Punct}\\s][\\.-](?=[^\\p{Punct}\\s])|[^\\p{Punct}\\s]){1,}\\.[a-z]{2,}(?::[0-9]+)?";

	  private static final String URL_VALID_GENERAL_PATH_CHARS = "[a-z0-9!\\*';:=\\+\\$/%#\\[\\]\\-_,~]";
	  /** Allow URL paths to contain balanced parens
	   *  1. Used in Wikipedia URLs like /Primer_(film)
	   *  2. Used in IIS sessions like /S(dfd346)/
	  **/
	  private static final String URL_BALANCE_PARENS = "(?:\\(" + URL_VALID_GENERAL_PATH_CHARS + "+\\))";
	  private static final String URL_VALID_URL_PATH_CHARS = "(?:" +
	    URL_BALANCE_PARENS +
	    "|@" + URL_VALID_GENERAL_PATH_CHARS + "+/" +
	    "|[\\.,]?" + URL_VALID_GENERAL_PATH_CHARS + "+" +
	  ")";

	  /** Valid end-of-path chracters (so /foo. does not gobble the period).
	   *   2. Allow =&# for empty URL parameters and other URL-join artifacts
	  **/
	  private static final String URL_VALID_URL_PATH_ENDING_CHARS = "(?:[a-z0-9=_#/\\-\\+]+|"+URL_BALANCE_PARENS+")";
	  private static final String URL_VALID_URL_QUERY_CHARS = "[a-z0-9!\\*'\\(\\);:&=\\+\\$/%#\\[\\]\\-_\\.,~]";
	  private static final String URL_VALID_URL_QUERY_ENDING_CHARS = "[a-z0-9_&=#/]";
	  private static final String VALID_URL_PATTERN_STRING =
	  "(" +                                                            //  $1 total match
	    "(" + URL_VALID_PRECEEDING_CHARS + ")" +                       //  $2 Preceeding chracter
	    "(" +                                                          //  $3 URL
	      "(https?://)" +                                              //  $4 Protocol
	      "(" + URL_VALID_DOMAIN + ")" +                               //  $5 Domain(s) and optional port number
	      "(/" +
	        "(?:" +
	          URL_VALID_URL_PATH_CHARS + "+" +
	            URL_VALID_URL_PATH_ENDING_CHARS + "|" +                //     1+ path chars and a valid last char
	          URL_VALID_URL_PATH_CHARS + "+" +
	            URL_VALID_URL_PATH_ENDING_CHARS + "?|" +               //     Optional last char to handle /@foo/ case
	          URL_VALID_URL_PATH_ENDING_CHARS +                        //     Just a # case
	        ")?" +
	      ")?" +                                                       //  $6 URL Path and anchor
	      "(\\?" + URL_VALID_URL_QUERY_CHARS + "*" +                   //  $7 Query String
	              URL_VALID_URL_QUERY_ENDING_CHARS + ")?" +
	    ")" +
	  ")";

	  public static final Pattern VALID_URL = Pattern.compile(VALID_URL_PATTERN_STRING, Pattern.CASE_INSENSITIVE);
	  public static final int VALID_URL_GROUP_ALL          = 1;
	  public static final int VALID_URL_GROUP_BEFORE       = 2;
	  public static final int VALID_URL_GROUP_URL          = 3;
	  public static final int VALID_URL_GROUP_PROTOCOL     = 4;
	  public static final int VALID_URL_GROUP_DOMAIN       = 5;
	  public static final int VALID_URL_GROUP_PATH         = 6;
	  public static final int VALID_URL_GROUP_QUERY_STRING = 7;

}
