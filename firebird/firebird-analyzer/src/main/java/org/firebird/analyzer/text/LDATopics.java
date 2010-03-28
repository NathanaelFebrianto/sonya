/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.common.IntPairWritable;
import org.apache.mahout.utils.vectors.VectorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is to print out the top K words for each topic. 
 * This class is doing the same job as below: mvn -q exec:java
 * -Dexec.mainClass="org.apache.mahout.clustering.lda.LDAPrintTopics"
 * -Dexec.args="-i `ls -1dtr D:/firebird/lda/state-* | tail -1` -d
 * D:/firebird/dict.txt -o D:/firebird/topics/ -w 20"
 * 
 * @author Young-Gue Bae
 */
public class LDATopics {

	private static final Logger log = LoggerFactory.getLogger(LDATopics.class);

	/**
	 * StringDoublePair
	 *
	 */
	private class StringDoublePair implements Comparable<StringDoublePair> {
		private final double score;
		private final String word;

		StringDoublePair(double score, String word) {
			this.score = score;
			this.word = word;
		}

		@Override
		public int compareTo(StringDoublePair other) {
			return Double.compare(score, other.score);
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof StringDoublePair)) {
				return false;
			}
			StringDoublePair other = (StringDoublePair) o;
			return score == other.score && word.equals(other.word);
		}

		@Override
		public int hashCode() {
			return (int) Double.doubleToLongBits(score) ^ word.hashCode();
		}
	}

	// Expands the queue list to have a Queue for topic K
	private void ensureQueueSize(
			List<PriorityQueue<StringDoublePair>> queues, int k) {
		for (int i = queues.size(); i <= k; ++i) {
			queues.add(new PriorityQueue<StringDoublePair>());
		}
	}

	/**
	 * Constructor.
	 * 
	 */
	public LDATopics() {
	}

	/**
	 * Prints out the top K words for each topic.
	 * 
	 * @param input
	 * 			The path to an LDA output (a state)
	 * @param dictFile
	 * 			The dictionary to read in, in the same format as one created by 
     *     		org.apache.mahout.utils.vectors.lucene.Driver
	 * @param output
	 * 			The output directory to write top words
	 * @param numWords
	 * 			The number of words to print
	 * @param dictType
	 * 			The dictionary file type (text|sequencefile)
	 * @exception
	 */
	public void printTopics(String input,
							String dictFile,
							String output,
							int numWords,
							String dictType) throws Exception {
	      
		if (numWords < 0) {
			numWords = 20;
		}
		
		if (dictType == null || dictType.equals("")) {
			dictType = "text";
		}
		
		Configuration config = new Configuration();

		List<String> wordList;
		if (dictType.equals("text")) {
			wordList = Arrays.asList(VectorHelper.loadTermDictionary(new File(dictFile)));
		} else if (dictType.equals("sequencefile")) {
			FileSystem fs = FileSystem.get(new Path(dictFile).toUri(), config);
			wordList = Arrays.asList(VectorHelper.loadTermDictionary(config, fs, dictFile));
		} else {
			throw new IllegalArgumentException("Invalid dictionary format");
		}

		List<List<String>> topWords = topWordsForTopics(input, config, wordList, numWords);

		if (output != null && !output.equals("")) {
			File outputDir = new File(output);
			if (!outputDir.exists()) {
				if (!outputDir.mkdirs()) {
					throw new IOException("Could not create directory: " + output);
				}
			}
			writeTopWords(topWords, outputDir);
			// print also by louie
			printTopWords(topWords);
		} else {
			printTopWords(topWords);
		}
	}

	// Adds the word if the queue is below capacity, or the score is high enough
	private void maybeEnqueue(Queue<StringDoublePair> q, String word,
			double score, int numWordsToPrint) {
		if (q.size() >= numWordsToPrint && score > q.peek().score) {
			q.poll();
		}
		if (q.size() < numWordsToPrint) {
			q.add(new StringDoublePair(score, word));
		}
	}

	private void printTopWords(List<List<String>> topWords) {
		for (int i = 0; i < topWords.size(); ++i) {
			List<String> topK = topWords.get(i);
			System.out.println("Topic " + i);
			System.out.println("===========");
			for (String word : topK) {
				System.out.println(word);
			}
		}
	}

	public List<List<String>> topWordsForTopics(String dir,
			Configuration job, List<String> wordList, int numWordsToPrint)
			throws IOException {
		FileSystem fs = new Path(dir).getFileSystem(job);

		List<PriorityQueue<StringDoublePair>> queues = new ArrayList<PriorityQueue<StringDoublePair>>();

		IntPairWritable key = new IntPairWritable();
		DoubleWritable value = new DoubleWritable();
		for (FileStatus status : fs.globStatus(new Path(dir, "*"))) {
			Path path = status.getPath();
			SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, job);
			while (reader.next(key, value)) {
				int topic = key.getFirst();
				int word = key.getSecond();

				ensureQueueSize(queues, topic);
				if (word >= 0 && topic >= 0) {
					double score = value.get();
					String realWord = wordList.get(word);
					maybeEnqueue(queues.get(topic), realWord, score, numWordsToPrint);
				}
			}
			reader.close();
		}

		List<List<String>> result = new ArrayList<List<String>>();
		for (int i = 0; i < queues.size(); ++i) {
			result.add(i, new LinkedList<String>());
			for (StringDoublePair sdp : queues.get(i)) {
				//result.get(i).add(0, sdp.word); // prepend
				result.get(i).add(0, sdp.word + "\t" + sdp.score); // by louie
			}
		}

		return result;
	}

	private void writeTopWords(List<List<String>> topWords, File output) throws IOException {
		for (int i = 0; i < topWords.size(); ++i) {
			List<String> topK = topWords.get(i);
			File out = new File(output, "topic-" + i);
			PrintWriter writer = new PrintWriter(new FileWriter(out));
			writer.println("Topic " + i);
			writer.println("===========");
			for (String word : topK) {
				writer.println(word);
			}
			writer.close();
		}
	}
	
}
