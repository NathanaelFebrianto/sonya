package com.beeblz.liwc;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.beeblz.common.Config;

public class PersonalityRecognizer {

	/**
	 * LIWC.CAT dictionary file from the Linguistic Inquiry and Word Count
	 * (LIWC) tool (in configuration file).
	 */
	private File liwcCatFile;
	
	/** LIWC dictionary. */
	private LIWCDictionary liwcDic;
	
	/** Mapping between long feature names and short ones in the Weka models. */
	private Map<String,String> featureShortcuts;
	
	/** Arrays of features that aren't included in the models. **/
	/*
	private static final String[] domainDependentFeatures = {"FRIENDS", "FAMILY", 
		"OCCUP", "SCHOOL", "JOB", "LEISURE", "HOME","SPORTS","TV", "MUSIC", "MONEY", 
		"METAPH", "DEATH", "PHYSCAL", "BODY", "EATING", "SLEEP", "GROOM"};
	*/	
	private static final String[] domainDependentFeatures = {""};
	
	/** Set of features that aren't included in the models. **/
	private Set<String> domainDependentFeatureSet;
	
	/** Arrays of features that aren't included in one instance analysis (corpus analysis only). **/
	private static final String[] absoluteCountFeatures = {"WC"};
	
	/** Set of features that aren't included in one instance analysis (corpus analysis only). **/
	private Set<String> absoluteCountFeatureSet;
	

	public PersonalityRecognizer(File liwcCatFile) {
		try {
			this.liwcCatFile = liwcCatFile;
			// load LIWC dictionary in memory
			liwcDic = new LIWCDictionary(liwcCatFile);

			// load shortcut map
			featureShortcuts = getShortFeatureNames();
			
			// load non general features
			domainDependentFeatureSet = new LinkedHashSet<String>(Arrays.asList(domainDependentFeatures));

			// load absolute features
			absoluteCountFeatureSet = new LinkedHashSet<String>(Arrays.asList(absoluteCountFeatures));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * Computes the features from the input text (70 LIWC features and 14 from
	 * the MRC database).
	 * 
	 * @param text
	 *            input text.
	 * @param relativeOnly do not return absolute count features (WC), must be set to false if
	 *        standardized features are used (corpus analysis mode).
	 * @return mapping associating feature names strings with feature counts
	 *         (Double objects).
	 * @throws Exception
	 */
	public Map<String,Double> getFeatureCounts(String text, boolean relativeOnly) throws Exception {
		Map<String,Double> counts = new LinkedHashMap<String,Double>();
		
		// compute LIWC features
		Map<String,Double> initCounts = liwcDic.getCounts(text, true);
						
		for (String longFeature : initCounts.keySet()) { 
			
			if (featureShortcuts.containsKey(longFeature)) {
				counts.put(featureShortcuts.get(longFeature), initCounts.get(longFeature)); 
			} else {
				counts.put(longFeature, initCounts.get(longFeature)); 
				// System.err.println("Warning: LIWC feature " + longFeature + " not recognized, check LIWC.CAT file");
			}
		}
		
		// remove domain dependent LIWC features
		counts.keySet().removeAll(domainDependentFeatureSet);
		if (relativeOnly) { counts.keySet().removeAll(absoluteCountFeatureSet); }
		System.err.println("LIWC features computed: " + counts.size());
		
		return counts;
	}
	
	/**
	 * Returns a mapping associating features in the LIWC.CAT file to shortcuts used in the 
	 * Weka models.
	 * 
	 * @return mapping asssociating long names in the LIWC dictionary to the short names in the
	 * Weka models. 
	 */
	private Map<String,String> getShortFeatureNames() {
		
		Map<String,String> shortcuts = new LinkedHashMap<String,String>();
		
		shortcuts.put("LINGUISTIC", "LINGUISTIC");
		shortcuts.put("PRONOUN", "PRONOUN");
		shortcuts.put("I", "I");
		shortcuts.put("WE", "WE");
		shortcuts.put("SELF", "SELF");
		shortcuts.put("YOU", "YOU");
		shortcuts.put("OTHER", "OTHER");
		shortcuts.put("NEGATIONS", "NEGATE");
		shortcuts.put("ASSENTS", "ASSENT");
		shortcuts.put("ARTICLES", "ARTICLE");
		shortcuts.put("PREPOSITIONS", "PREPS");
		shortcuts.put("NUMBERS", "NUMBER");
		shortcuts.put("PSYCHOLOGICAL PROCESS", "PSYCHOLOGICAL PROCESS");
		shortcuts.put("AFFECTIVE PROCESS", "AFFECT");
		shortcuts.put("POSITIVE EMOTION", "POSEMO");
		shortcuts.put("POSITIVE FEELING", "POSFEEL");
		shortcuts.put("OPTIMISM", "OPTIM");
		shortcuts.put("NEGATIVE EMOTION", "NEGEMO");
		shortcuts.put("ANXIETY", "ANX");
		shortcuts.put("ANGER", "ANGER");
		shortcuts.put("SADNESS", "SAD");
		shortcuts.put("COGNITIVE PROCESS", "COGMECH");
		shortcuts.put("CAUSATION", "CAUSE");
		shortcuts.put("INSIGHT", "INSIGHT");
		shortcuts.put("DISCREPANCY", "DISCREP");
		shortcuts.put("INHIBITION", "INHIB");
		shortcuts.put("TENTATIVE", "TENTAT");
		shortcuts.put("CERTAINTY", "CERTAIN");
		shortcuts.put("SENSORY PROCESS", "SENSES");
		shortcuts.put("SEEING", "SEE");
		shortcuts.put("HEARING", "HEAR");
		shortcuts.put("FEELING", "FEEL");
		shortcuts.put("SOCIAL PROCESS", "SOCIAL");
		shortcuts.put("COMMUNICATION", "COMM");
		shortcuts.put("REFERENCE PEOPLE", "OTHREF");
		shortcuts.put("FRIENDS", "FRIENDS");
		shortcuts.put("FAMILY", "FAMILY");
		shortcuts.put("HUMANS", "HUMANS");
		shortcuts.put("RELATIVITY", "RELATIVITY");
		shortcuts.put("TIME", "TIME");
		shortcuts.put("PAST", "PAST");
		shortcuts.put("PRESENT", "PRESENT");
		shortcuts.put("FUTURE", "FUTURE");
		shortcuts.put("SPACE", "SPACE");
		shortcuts.put("UP", "UP");
		shortcuts.put("DOWN", "DOWN");
		shortcuts.put("INCLUSIVE", "INCL");
		shortcuts.put("EXCLUSIVE", "EXCL");
		shortcuts.put("MOTION", "MOTION");
		shortcuts.put("PERSONAL PROCESS", "PERSONAL PROCESS");
		shortcuts.put("OCCUPATION", "OCCUP");
		shortcuts.put("SCHOOL", "SCHOOL");
		shortcuts.put("JOB OR WORK", "JOB");
		shortcuts.put("ACHIEVEMENT", "ACHIEVE");
		shortcuts.put("LEISURE ACTIVITY", "LEISURE");
		shortcuts.put("HOME", "HOME");
		shortcuts.put("SPORTS", "SPORTS");
		shortcuts.put("TV OR MOVIE", "TV");
		shortcuts.put("MUSIC", "MUSIC");
		shortcuts.put("MONEY", "MONEY");
		shortcuts.put("METAPHYSICAL", "METAPH");
		shortcuts.put("RELIGION", "RELIG");
		shortcuts.put("DEATH AND DYING", "DEATH");
		shortcuts.put("PHYSICAL STATES", "PHYSCAL");
		shortcuts.put("BODY STATES", "BODY");
		shortcuts.put("SEXUALITY", "SEXUAL");
		shortcuts.put("EATING", "EATING");
		shortcuts.put("SLEEPING", "SLEEP");
		shortcuts.put("GROOMING", "GROOM");
		shortcuts.put("EXPERIMENTAL DIMENSION", "EXPERIMENTAL DIMENSION");
		shortcuts.put("SWEAR WORDS", "SWEAR");
		shortcuts.put("NONFLUENCIES", "NONFL");
		shortcuts.put("FILLERS", "FILLERS");
		
		return shortcuts;
	}
	
	public static void main(String[] args) {
		try {
			File liwcCatFile = new File(Config.getProperty("liwcCatFile"));
			PersonalityRecognizer recognizer = new PersonalityRecognizer(liwcCatFile);
			
			String text1 = "Hello you wonderful people who are reading my paper.  I hope you really really  enjoy this one after all the other ones you have read.  Anyway right now I am  really excited because I'm just about to go to the Rage Against the Machine concert.  I'm hoping that I have a blast and I know I will.  My brother and a  lot of my friends came up for this concert.  So, I will be able to see them.   AIN'T THAT THE GREATEST THING.  Earlier today, I was wishing that I could fly.  The reason for that is that we  were stuck in traffic.  Just think about it, just flying through the air, your hair flying back, brushing across your face--but I guess if we could fly, we  wouldn't think that much about it.  Yesterday, I got an e-mail from my sister.  It was really COOL and all, and she should come visit me soon.  My sister is the one person in my family that I  really enjoy--but enough about that.  I'm going to a concert, and to party!    Oh, just to throw something in here.  I was noticing that many people here get  really excited about going out and staying out till whatever time in the  morning.  I think this is really hilarious because I come from Laredo, Texas. That is a border-town to Mexico (not that it could be Canada in Texas).  Well,  we have clubs and discos in Mexico that are just a few minutes away so I'm  use to going out, staying out late, and drinking.  Therefore, I came to the conclusion that most of the people here have not been able to go out and have  fun during their high school years--whether this is attributed to their parents or themselves.  That is why there is such a high drop-out rate here in UT, and why a lot of students struggle just to pass.    I hope pass all my classes with A's if possible.  I'm planning to be a plastic  and reconstructive surgeon, and I kind of need a great GPA to get into Med  School.  Oh by the way, I want to clear up the reason why I want to be that  kind of a surgeon.  Many people believe that plastic surgeons are doctors who are just in the business for money.  Some people don't even consider doctors as real doctors.  They are seen as individuals who help superficial people stay  young and beautiful.  Although plastic surgeons may do this, they help build up the self-esteem of the patient.  There are some people who were born with  defects or were in a really bad accident, plastic/reconstructive surgeons help these people enter the world again.  Most people with some type of defect  usually have a low self-esteem.  Therefore, they do not really enjoy life nor do they participate in daily activities with other people.  Plastic/reconstructive surgeons allow these people to enter into the world.  They feel better about  themselves and as a whole their spiritual self is improved.  This is vital to  the survival of the individual--for without it there is nothing to live for.  Well, I'm going now hope I didn't bore you too much!  :>";
			String text2 = "@BillGates Hey Bill. I just want you to know that I want to be like you someday. while I climb my way up there, care to share some tips? :)";
			// get feature counts from the input text
			Map<String,Double> counts = recognizer.getFeatureCounts(text2, true);
			System.out.println("Total features computed: " + counts.size());
			
			System.out.println("Feature counts:");
			Utils.printMap(counts, System.out);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
