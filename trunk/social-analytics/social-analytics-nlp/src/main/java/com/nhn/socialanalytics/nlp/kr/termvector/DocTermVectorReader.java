package com.nhn.socialanalytics.nlp.kr.termvector;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector.Element;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.utils.vectors.VectorHelper;

public class DocTermVectorReader {
	
	public DocTermVectorReader() {}
	
	public void read(String vectorsPath, String dicFile) throws IOException {
		Configuration conf = new Configuration();
		
		Configuration config = new Configuration();

		List<String> dictionaryMap = Arrays.asList(VectorHelper.loadTermDictionary(new File(dicFile)));		
		
		FileSystem fs = FileSystem.get(conf);
		Path path = new Path(vectorsPath);		 
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
