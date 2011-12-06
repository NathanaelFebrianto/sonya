package com.nhn.socialanalytics.nlp.sentiment;

import java.io.File;
import java.util.Map;

import com.nhn.socialanalytics.nlp.semantic.SemanticClause;
import com.nhn.socialanalytics.nlp.semantic.SemanticSentence;
import com.nhn.socialanalytics.nlp.sentiment.LIWCDictionary.WordCount;

public class SentimentAnalyzer {
	
	private static SentimentAnalyzer instance = null;
	private static File liwcCat = new File("liwc.txt");
	private PersonalityRecognizer personalityRecognizer;
	
	public SentimentAnalyzer(File liwcCatFile) {
		personalityRecognizer = new PersonalityRecognizer(liwcCatFile);
	}
	
	public static SentimentAnalyzer getInstance(File liwcCatFile) {
		if (instance == null || !liwcCat.getPath().equals(liwcCatFile.getPath())) {
			instance = new SentimentAnalyzer(liwcCatFile);			
			liwcCat = liwcCatFile;
		}
			
		return instance;
	}
	
	public SemanticClause analyzePolarity(SemanticClause clause) {

		double polarity = 0.0;
		int priority = clause.getPriority();
		if (priority < 1) priority = 1;
		
		//String label = clause.makeLabel(false);
		String label = clause.makeStandardLabel(false);
		label = label + " " + clause.makeStandardAttributesLabel();
		
		if (label.trim().equals(""))
			return clause;
		
		Map<String, WordCount> liwcMaps = this.analyzeLIWCFeatures(label);
		
		double posCount = 0.0;
		double negCount = 0.0;
		
		for (Object key : liwcMaps.keySet()) {
			WordCount wordCount = (WordCount) liwcMaps.get(key);
			
			if (wordCount.getCount() > 0) {
				if (key.equals("POSITIVE")) {
					posCount += wordCount.getCount();
				}
				else if (key.equals("NEGATIVE") || key.equals("ANGER") || key.equals("ANXIETY") || key.equals("SADNESS")) {
					negCount += wordCount.getCount();
				}					
			}
		}
		
		if (posCount > negCount)
			polarity = 1.0;		//positive
		else if (posCount <= negCount && negCount > 0.0)
			polarity = -1.0;	//negative
		else 
			polarity = 0.0;		//neutral
		
		
		double strength = 1 / new Double(priority).doubleValue();
		
		System.out.println("priority == " + priority);
		System.out.println("polarity == " + polarity);
		System.out.println("strength == " + strength);
		
		clause.setPolarity(polarity);
		clause.setStrength(strength);
		clause.setPositiveWordCount(posCount);
		clause.setNegativeWordCount(negCount);
		
		return clause;
	}
	
	public SemanticSentence analyzePolarity(SemanticSentence sentence) {
		sentence.sort(true);		
		
		double weightedPolarity = 0.0;
		boolean isSubjective = false;
		boolean isAllSamePriority = true;
		
		int prevPriority = -1;
		for (SemanticClause clause : sentence) {
			clause = this.analyzePolarity(clause);
			
			if (prevPriority == clause.getPriority()) {
				weightedPolarity = weightedPolarity + (clause.getPolarity() * clause.getStrength()) * clause.getStrength();
			}
			else {			
				weightedPolarity = weightedPolarity + (clause.getPolarity() * clause.getStrength());
			}
			
			if (weightedPolarity > 1.0)
				weightedPolarity = 1.0;
			else if (weightedPolarity < -1.0)
				weightedPolarity = -1.0;
			
			if (clause.getPolarity() != 0.0)
				isSubjective = true;
			
			if (prevPriority < clause.getPriority())
				isAllSamePriority = false;
			
			prevPriority = clause.getPriority();
		}
		
		// polarity
		if (!isAllSamePriority && weightedPolarity > 0) {
			sentence.setPolarity(1.0);
			sentence.setPolarityStrength(Math.abs(weightedPolarity));
		}
		else if (!isAllSamePriority && weightedPolarity < 0) {
			sentence.setPolarity(-1.0);
			sentence.setPolarityStrength(Math.abs(weightedPolarity));
		}
		else if (isSubjective && (isAllSamePriority || weightedPolarity == 0)) {
			double posWordCount = sentence.sumPostiveWordCount();
			double negWordCount = sentence.sumNegativeWordCount();
			
			System.out.println("positive word count == " + posWordCount);
			System.out.println("negative word count == " + negWordCount);			
			
			if (posWordCount > negWordCount) {
				sentence.setPolarity(1.0);
				sentence.setPolarityStrength( posWordCount / (posWordCount + negWordCount) );
			}
			else if (posWordCount <= negWordCount) {
				sentence.setPolarity(-1.0);
				sentence.setPolarityStrength( negWordCount / (posWordCount + negWordCount) );
			}						
		}
		else {
			sentence.setPolarity(0.0);
			sentence.setPolarityStrength(Math.abs(weightedPolarity));
		}
		
		System.out.println("sentence polarity == " + sentence.getPolarity());
		System.out.println("sentence polarity strength == " + sentence.getPolarityStrength());
		
		return sentence;
	}
	
	public double analyzePolarity(String text) {
		if (text == null)
			return 0.0;
		
		Map<String, WordCount> liwcMaps = this.analyzeLIWCFeatures(text);
		
		double posCount = 0.0;
		double negCount = 0.0;
		
		for (Object key : liwcMaps.keySet()) {
			WordCount wordCount = (WordCount) liwcMaps.get(key);
			
			if (wordCount.getCount() > 0) {
				if (key.equals("POSITIVE")) {
					posCount += wordCount.getCount();
				}
				else if (key.equals("NEGATIVE") || key.equals("ANGER") || key.equals("ANXIETY") || key.equals("SADNESS")) {
					negCount += wordCount.getCount();
				}					
			}
		}
		
		if (posCount > negCount)
			return 1.0;	//positive
		else if (posCount <= negCount && negCount > 0.0)
			return -1.0;	//negative			
		
		return 0.0;	//neutral
	}
	
	public Map<String, WordCount> analyzeLIWCFeatures(String text) {
		try {
			System.out.println("source text to analyze LIWC == " + text);
			
			// get feature counts from the input text
			Map<String, WordCount> counts = personalityRecognizer.getWordFeatureCounts(text, true);
			
			System.out.println("Total features computed: " + counts.size());			
			//System.out.println("Feature counts:");
			//SentimentUtil.printMap(counts, System.out);
			System.out.println("\n");
			
			return counts;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}	
	
	
	public static void main(String[] args) {
		SentimentAnalyzer analyzer = SentimentAnalyzer.getInstance(new File("./liwc/LIWC_ko.txt"));
		
		//String text = "Hello you wonderful people who are reading my paper.  I hope you really really  enjoy this one after all the other ones you have read.  Anyway right now I am  really excited because I'm just about to go to the Rage Against the Machine concert.  I'm hoping that I have a blast and I know I will.  My brother and a  lot of my friends came up for this concert.  So, I will be able to see them.   AIN'T THAT THE GREATEST THING.  Earlier today, I was wishing that I could fly.  The reason for that is that we  were stuck in traffic.  Just think about it, just flying through the air, your hair flying back, brushing across your face--but I guess if we could fly, we  wouldn't think that much about it.  Yesterday, I got an e-mail from my sister.  It was really COOL and all, and she should come visit me soon.  My sister is the one person in my family that I  really enjoy--but enough about that.  I'm going to a concert, and to party!    Oh, just to throw something in here.  I was noticing that many people here get  really excited about going out and staying out till whatever time in the  morning.  I think this is really hilarious because I come from Laredo, Texas. That is a border-town to Mexico (not that it could be Canada in Texas).  Well,  we have clubs and discos in Mexico that are just a few minutes away so I'm  use to going out, staying out late, and drinking.  Therefore, I came to the conclusion that most of the people here have not been able to go out and have  fun during their high school years--whether this is attributed to their parents or themselves.  That is why there is such a high drop-out rate here in UT, and why a lot of students struggle just to pass.    I hope pass all my classes with A's if possible.  I'm planning to be a plastic  and reconstructive surgeon, and I kind of need a great GPA to get into Med  School.  Oh by the way, I want to clear up the reason why I want to be that  kind of a surgeon.  Many people believe that plastic surgeons are doctors who are just in the business for money.  Some people don't even consider doctors as real doctors.  They are seen as individuals who help superficial people stay  young and beautiful.  Although plastic surgeons may do this, they help build up the self-esteem of the patient.  There are some people who were born with  defects or were in a really bad accident, plastic/reconstructive surgeons help these people enter the world again.  Most people with some type of defect  usually have a low self-esteem.  Therefore, they do not really enjoy life nor do they participate in daily activities with other people.  Plastic/reconstructive surgeons allow these people to enter into the world.  They feel better about  themselves and as a whole their spiritual self is improved.  This is vital to  the survival of the individual--for without it there is nothing to live for.  Well, I'm going now hope I didn't bore you too much!  :>";
		//String text = "@BillGates Hey Bill. I just want you to know that I want to be like you someday. while I climb my way up there, care to share some tips? :)";
		//String text = "긍정돼지 저게요 밑에 은근히 많이 깔려서 배부르답니다ㅋㅋ누군가를 좋아하게 되니 ㅎㅎ저절로 소식하고 싶다는ㅜㅜᆢ뭐래?;;탕수육 ♥맛나게 드셔요~규일님^^";
		String text = "이 물건은 배송이 빨라서 정말 좋지만, 품질이 별로 안좋네요.";
		
		double polarity = analyzer.analyzePolarity(text);
		System.out.println("polarity == " + polarity);
	}

}
