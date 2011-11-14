package com.nhn.socialanalytics.miner.termvector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.mahout.common.iterator.FileLineIterator;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.Vector.Element;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.utils.vectors.VectorHelper;
import org.apache.mahout.utils.vectors.csv.CSVVectorIterator;

public class DocTermVectorReader {
	
	public Set<String> stopwordSet = new HashSet<String>();
	
	private static final Set<?> DEFAULT_STOPWORDS;

	static {
		final List<?> stopWords = Arrays.asList(
				 "tagquestion"
				, "tagsmile"
				, "tagcry"
				, "taglove"
				, "tagexclamation"
				, "tagempty"
				, "http"
				, "gt"
				, "lt"
				, "brgt"
				, "brgt"
		);
		final CharArraySet stopSet = new CharArraySet(stopWords.size(), false);
		stopSet.addAll(stopWords);
		DEFAULT_STOPWORDS = CharArraySet.unmodifiableSet(stopSet);
	}
		
	private static final Pattern TAB_PATTERN = Pattern.compile("\t");
	
	public DocTermVectorReader() throws IOException {
		this(null, null);
	}
	
	public DocTermVectorReader(String stopwordFile) throws IOException {
		this(stopwordFile, null);
	}
	
	public DocTermVectorReader(String stopwordFile, Set<String> customStopwords) throws IOException {
		Set<String> stopwords = loadStopwords(stopwordFile);
		stopwordSet.addAll((Set<String>)DEFAULT_STOPWORDS);
		stopwordSet.addAll(stopwords);
		
		if (customStopwords != null)
			stopwordSet.addAll(customStopwords);
	}
	
	private Set<String> loadStopwords(String stopwordFile) throws IOException {
		Set<String> stopSet = new HashSet<String>();
		
		if (stopwordFile == null)
			return stopSet;
		
		FileLineIterator it = new FileLineIterator(new FileInputStream(new File(stopwordFile)));

		while (it.hasNext()) {
			String line = it.next();
			line = line.trim();
			if (line.startsWith("#")) {
				continue;
			}
			
			if (!line.equals("")) {
				stopSet.add(line);
			}
		}
		return stopSet;
	}
	
	public Set<String> getStopwords() {
		return this.stopwordSet;
	}
	
	public Map<String, Integer> getTerms(String dicFile, int minTF) throws IOException {
		Map<String, Integer> terms = new HashMap<String, Integer>();
		Map<String, Integer> map = this.loadTermDictionary(dicFile, false);

		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			String term = entry.getKey();
			int tf = (Integer) entry.getValue();
			
			if (tf >= minTF) {
				terms.put(term, new Integer(tf));
			}			
		}
		
		return terms;
	}
	
	public List<String> loadTermDictionary(String dicFileFormat, String dicFile) throws IOException {
		Configuration conf = new Configuration();		
		List<String> dictionaryMap;
		
		if (dicFileFormat.equals("text")) {
			dictionaryMap = Arrays.asList(VectorHelper.loadTermDictionary(new File(dicFile)));
		} else if (dicFileFormat.equals("sequencefile")) {
			dictionaryMap = Arrays.asList(VectorHelper.loadTermDictionary(conf, dicFile));
		} else {
			throw new IllegalArgumentException("Invalid dictionary format");
		}
		
		return dictionaryMap;
	}
	
	public Map<String, Integer> loadTermDictionary(String dicFile, boolean includeStopwords) throws IOException {
		Map<String, Integer> dicMap = new HashMap<String, Integer>();
		FileLineIterator it = new FileLineIterator(new FileInputStream(new File(dicFile)));

		while (it.hasNext()) {
			String line = it.next();
			if (line.startsWith("#")) {
				continue;
			}
			String[] tokens = TAB_PATTERN.split(line);
			if (tokens.length < 3) {
				continue;
			}
			
			if (includeStopwords) {
				dicMap.put(tokens[0], new Integer(tokens[1]));
			}
			else {
				if (!stopwordSet.contains(tokens[0]))
					dicMap.put(tokens[0], new Integer(tokens[1]));
			}
		}
		return dicMap;
	}
	
	public void readVectors(String vectorsFileFormat, String vectorsPath, String dicFile) throws IOException {
		Configuration conf = new Configuration();
		
		List<String> map = loadTermDictionary("text", dicFile);
			
		FileSystem fs = FileSystem.get(conf);
		Path path = new Path(vectorsPath);		 
		
		if (vectorsFileFormat != null) {
			if (vectorsFileFormat.equals("file")) {
				BufferedReader reader = new BufferedReader(new FileReader(vectorsPath));
				
				CSVVectorIterator it = new CSVVectorIterator(reader);
				
				while (it.hasNext()) {
					Vector vect = (Vector) it.next();
					double tf = 0.0;
					for (Element e : vect){
						tf = tf + e.get();
						System.out.println("Token: " + map.get(e.index()) + ", TF-IDF weight: " + e.get()) ;
					}
				}
				reader.close();
			}
		} else {
			SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, conf);
			LongWritable key = new LongWritable();
			VectorWritable value = new VectorWritable();
			
			while (reader.next(key, value)) {
				NamedVector namedVector = (NamedVector) value.get();
				RandomAccessSparseVector vect = (RandomAccessSparseVector) namedVector.getDelegate();
			 
				for (Element e : vect){
					System.out.println("Token: " + map.get(e.index()) + ", TF-IDF weight: " + e.get()) ;
				}
			}
			reader.close();			
		}
	}
	
	public static void main(String[] args) {
		
		
		try {
			DocTermVectorReader reader = new DocTermVectorReader(null);
			reader.readVectors(
					"file", // vectors file format
					"./bin/twitter/vectors/predicate_kakaotalk", // vetors path
					"./bin/twitter/dic/predicate_kakaotalk.txt" // dictionary file
			);

			Map<String, Integer> terms = reader.getTerms("./bin/twitter/dic/predicate_kakaotalk.txt", 1);

			for (Map.Entry<String, Integer> entry : terms.entrySet()) {
				String term = entry.getKey();
				int tf = (Integer) entry.getValue();

				System.out.println(term + "\t" + tf);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
