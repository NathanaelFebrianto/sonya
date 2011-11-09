package com.nhn.socialanalytics.nlp.kr.termvector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.common.iterator.FileLineIterator;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.Vector.Element;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.utils.vectors.VectorHelper;
import org.apache.mahout.utils.vectors.csv.CSVVectorIterator;

public class DocTermVectorReader {
	
	private static final Pattern TAB_PATTERN = Pattern.compile("\t");
	
	public DocTermVectorReader() {}
	
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
	
	public Map<String, Integer> loadTermDictionary(String dictFile) throws IOException {
		Map<String, Integer> dicMap = new HashMap<String, Integer>();
		FileLineIterator it = new FileLineIterator(new FileInputStream(new File(dictFile)));

		while (it.hasNext()) {
			String line = it.next();
			if (line.startsWith("#")) {
				continue;
			}
			String[] tokens = TAB_PATTERN.split(line);
			if (tokens.length < 3) {
				continue;
			}
			dicMap.put(tokens[0], new Integer(tokens[1]));
		}
		return dicMap;
	}
	
	public void readVectors(String vectorsFileFormat, String vectorsPath, String dicFile) throws IOException {
		Configuration conf = new Configuration();
		
		List<String> dictionaryMap = loadTermDictionary("text", dicFile);
			
		FileSystem fs = FileSystem.get(conf);
		Path path = new Path(vectorsPath);		 
		
		if (vectorsFileFormat != null) {
			if (vectorsFileFormat.equals("file")) {
				BufferedReader reader = new BufferedReader(new FileReader(vectorsPath));
				
				CSVVectorIterator it = new CSVVectorIterator(reader);
				
				while (it.hasNext()) {
					Vector vect = (Vector) it.next();
					System.out.println("vector == " + vect);
					for (Element e : vect){
						System.out.println("Token: " + dictionaryMap.get(e.index()) + ", TF-IDF weight: " + e.get()) ;
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
					System.out.println("Token: " + dictionaryMap.get(e.index()) + ", TF-IDF weight: " + e.get()) ;
				}
			}
			reader.close();			
		}
	}
	
	public static void main(String[] args) {
		DocTermVectorReader reader = new DocTermVectorReader();
		
		try {
			reader.readVectors(
					"file", // vectors file format
					"./bin/vectors", // vetors path
					"./bin/dic_predicate.txt" // dictionary file
			);

			Map<String, Integer> dicMap = reader.loadTermDictionary("./bin/dic_predicate.txt");

			for (Map.Entry<String, Integer> entry : dicMap.entrySet()) {
				String term = entry.getKey();
				int tf = (Integer) entry.getValue();

				System.out.println(term + " tf = " + tf);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
