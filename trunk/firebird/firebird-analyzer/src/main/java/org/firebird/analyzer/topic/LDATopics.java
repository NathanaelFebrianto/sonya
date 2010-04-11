/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.topic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import org.firebird.analyzer.util.JobLogger;
import org.firebird.io.model.TopicTerm;

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
	/** logger */
	private static JobLogger logger = JobLogger.getLogger(LDATopics.class);

	/**
	 * StringDoublePair
	 *
	 */
	private class StringDoublePair implements Comparable<StringDoublePair> {
		private final double score;
		private final String term;

		StringDoublePair(double score, String term) {
			this.score = score;
			this.term = term;
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
			return score == other.score && term.equals(other.term);
		}

		@Override
		public int hashCode() {
			return (int) Double.doubleToLongBits(score) ^ term.hashCode();
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
	public void writeEachTopics(String input,
							String dictFile,
							String output,
							int numWords,
							String dictType) throws Exception {
	      
		Map<Integer, List<TopicTerm>> topTerms = this.getTopics(input, dictFile, numWords, dictType);	

		if (output != null && !output.equals("")) {
			File outputDir = new File(output);
			if (!outputDir.exists()) {
				if (!outputDir.mkdirs()) {
					throw new IOException("Could not create directory: " + output);
				}
			}
			writeTopTerms(topTerms, outputDir);
			// print also by louie
			printTopTerms(topTerms);
		} else {
			printTopTerms(topTerms);
		}
	}

	/**
	 * Prints out the top K words for each topic in a single file.
	 * 
	 * @param input
	 * 			The path to an LDA output (a state)
	 * @param dictFile
	 * 			The dictionary to read in, in the same format as one created by 
     *     		org.apache.mahout.utils.vectors.lucene.Driver
	 * @param outputFile
	 * 			The output file to write top words
	 * @param numWords
	 * 			The number of words to print
	 * @param dictType
	 * 			The dictionary file type (text|sequencefile)
	 * @exception
	 */
	public void writeTopics(String input,
							String dictFile,
							String outputFile,
							int numWords,
							String dictType) throws Exception {
	      
		Map<Integer, List<TopicTerm>> topics = this.getTopics(input, dictFile, numWords, dictType);		

		File out = new File(outputFile);
		PrintWriter writer = new PrintWriter(new FileWriter(out));

		writer.println("#topic_id	term	score");

		Iterator<Integer> it = topics.keySet().iterator();
		while (it.hasNext()) {
			Object topicId = it.next();
			List<TopicTerm> topicTerms = (List<TopicTerm>) topics.get(topicId);
			for (TopicTerm topicTerm : topicTerms) {
				writer.println(topicId + "\t" + topicTerm.getTerm() + "\t"
						+ topicTerm.getScore());
			}
		}
		writer.close();
	}

	/**
	 * Gets the unique words in topics.
	 * 
	 * @param input
	 * 			The path to an LDA output (a state)
	 * @param dictFile
	 * 			The dictionary to read in, in the same format as one created by 
     *     		org.apache.mahout.utils.vectors.lucene.Driver
	 * @param numWords
	 * 			The number of words to print
	 * @param dictType
	 * 			The dictionary file type (text|sequencefile)
	 * @return List<String> the list of unique words
	 * @exception
	 */
	public List<String> getUniqueTerms(String input,
							String dictFile,
							int numWords,
							String dictType) throws Exception {
		
		Map<Integer, List<TopicTerm>> topics = this.getTopics(input, dictFile, numWords, dictType);		
		List<String> result = new ArrayList<String>();

		Iterator<Integer> it = topics.keySet().iterator();
		while (it.hasNext()) {
			Object topicId = it.next();
			List<TopicTerm> topicTerms = (List<TopicTerm>) topics.get(topicId);
			for (TopicTerm topicTerm : topicTerms) {
				if (!result.contains(topicTerm.getTerm()))
					result.add(topicTerm.getTerm());
			}
		}
		
		return result;
	}

	/**
	 * Gets the top K words for each topic.
	 * 
	 * @param input
	 * 			The path to an LDA output (a state)
	 * @param dictFile
	 * 			The dictionary to read in, in the same format as one created by 
     *     		org.apache.mahout.utils.vectors.lucene.Driver
	 * @param numWords
	 * 			The number of words to print
	 * @param dictType
	 * 			The dictionary file type (text|sequencefile)
	 * @return Map<Integer, List<TopicTerm>> the map of topic word
	 * @exception
	 */
	public Map<Integer, List<TopicTerm>> getTopics(String input,
							String dictFile,
							int numWords,
							String dictType) throws Exception {
	      
		if (numWords < 0) {
			numWords = 20;
		}
		
		if (dictType == null || dictType.equals("")) {
			dictType = "text";
		}
		
		Configuration config = new Configuration();

		List<String> termList;
		if (dictType.equals("text")) {
			termList = Arrays.asList(VectorHelper.loadTermDictionary(new File(dictFile)));
		} else if (dictType.equals("sequencefile")) {
			FileSystem fs = FileSystem.get(new Path(dictFile).toUri(), config);
			termList = Arrays.asList(VectorHelper.loadTermDictionary(config, fs, dictFile));
		} else {
			throw new IllegalArgumentException("Invalid dictionary format");
		}

		List<List<StringDoublePair>> topTerms = topTermsForTopics(input, config, termList, numWords);

		Map<Integer, List<TopicTerm>> result = new HashMap<Integer, List<TopicTerm>>();
		for (int i = 0; i < topTerms.size(); ++i) {
			List<StringDoublePair> topK = topTerms.get(i);
			List<TopicTerm> topicTerms = new ArrayList<TopicTerm>();
			
			for (StringDoublePair sdp : topK) {
				TopicTerm topicTerm = new TopicTerm();
				topicTerm.setTopicId(i);
				topicTerm.setTerm(sdp.term);
				topicTerm.setScore(sdp.score);				
				topicTerms.add(topicTerm);
			}			
			// sort by score
			Collections.sort(topicTerms);
			result.put(Integer.valueOf(i), topicTerms);
		}
		
		return result;
	}
	
	// Adds the term if the queue is below capacity, or the score is high enough
	private void maybeEnqueue(Queue<StringDoublePair> q, String term,
			double score, int numTermsToPrint) {
		if (q.size() >= numTermsToPrint && score > q.peek().score) {
			q.poll();
		}
		if (q.size() < numTermsToPrint) {
			q.add(new StringDoublePair(score, term));
		}
	}

	private void printTopTerms(Map<Integer, List<TopicTerm>> topTerms) {
		Iterator<Integer> it = topTerms.keySet().iterator();
		while (it.hasNext()) {
			Object topicId = it.next();
			List<TopicTerm> topK = (List<TopicTerm>) topTerms.get(topicId);
			logger.info("Topic " + topicId);
			logger.info("===========");
			for (TopicTerm topicTerm : topK) {
				logger.info(topicTerm.getTerm());
			}
		}
	}

	private void writeTopTerms(Map<Integer, List<TopicTerm>> topTerms, File output) throws IOException {
		Iterator<Integer> it = topTerms.keySet().iterator();
		while (it.hasNext()) {
			Object topicId = it.next();
			List<TopicTerm> topK = (List<TopicTerm>) topTerms.get(topicId);
			File out = new File(output, "topic-" + topicId);
			PrintWriter writer = new PrintWriter(new FileWriter(out));
			writer.println("Topic " + topicId);
			writer.println("===========");
			for (TopicTerm topicTerm : topK) {
				writer.println(topicTerm.getTerm());
			}
			writer.close();
		}
	}
	
	private List<List<StringDoublePair>> topTermsForTopics(String dir,
			Configuration job, List<String> termList, int numTermsToPrint) throws IOException {
		FileSystem fs = new Path(dir).getFileSystem(job);

		List<PriorityQueue<StringDoublePair>> queues = new ArrayList<PriorityQueue<StringDoublePair>>();

		IntPairWritable key = new IntPairWritable();
		DoubleWritable value = new DoubleWritable();
		for (FileStatus status : fs.globStatus(new Path(dir, "*"))) {
			Path path = status.getPath();
			SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, job);
			while (reader.next(key, value)) {
				int topic = key.getFirst();
				int term = key.getSecond();

				ensureQueueSize(queues, topic);
				if (term >= 0 && topic >= 0) {
					double score = value.get();
					String realTerm = termList.get(term);
					maybeEnqueue(queues.get(topic), realTerm, score, numTermsToPrint);
				}
			}
			reader.close();
		}

		List<List<StringDoublePair>> result = new ArrayList<List<StringDoublePair>>();
		for (int i = 0; i < queues.size(); ++i) {
			result.add(i, new LinkedList<StringDoublePair>());
			for (StringDoublePair sdp : queues.get(i)) {
				//result.get(i).add(0, sdp.term); // prepend
				//result.get(i).add(0, sdp.term + "\t" + sdp.score); // by louie
				result.get(i).add(0, sdp);
			}
		}

		return result;
	}
	
}
