/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.topic;

import java.io.IOException;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.mahout.clustering.lda.LDADriver;
import org.apache.mahout.clustering.lda.LDAMapper;
import org.apache.mahout.clustering.lda.LDAReducer;
import org.apache.mahout.clustering.lda.LDAState;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.IntPairWritable;
import org.apache.mahout.common.RandomUtils;
import org.apache.mahout.math.DenseMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class for running a LDA algorithm in Apache Mahout.
 * Estimates an LDA model from a corpus of documents, which are SparseVectors of word counts. At each phase,
 * it outputs a matrix of log probabilities of each topic.
 * This class is doing the same job as below:
 * MAVEN_OPTS="-Xmx1G -ea" mvn -e exec:java -Dexec.mainClass=org.apache.mahout.clustering.lda.LDADriver 
 * -Dexec.args="-i D:/firebird/vectors -o D:/firebird/lda/ -k 10 -v 10000 --maxIter 40"
 * 
 * @author Young-Gue Bae
 */
public class LDAAnalyzer {

	  static final String STATE_IN_KEY = "org.apache.mahout.clustering.lda.stateIn";
	  static final String NUM_TOPICS_KEY = "org.apache.mahout.clustering.lda.numTopics";
	  static final String NUM_WORDS_KEY = "org.apache.mahout.clustering.lda.numWords";
	  static final String TOPIC_SMOOTHING_KEY = "org.apache.mahout.clustering.lda.topicSmoothing";
	  
	  static final int LOG_LIKELIHOOD_KEY = -2;
	  static final int TOPIC_SUM_KEY = -1;
	  static final double OVERALL_CONVERGENCE = 1.0E-5;
	  
	  private static final Logger log = LoggerFactory.getLogger(LDAAnalyzer.class);
	
	/**
	 * Constructor.
	 * 
	 */
	public LDAAnalyzer() {}

	/**
	 * Estimates an LDA model from a corpus of documents.
	 * 
	 * @param input
	 * 			The Path for input Vectors. Must be a SequenceFile of Writable, Vector
	 * @param output
	 * 			The Output Working Directory
	 * @param overwrite
	 * 			If set, overwrite the output directory
	 * @param numTopics
	 * 			The number of topics
	 * @param numWords
	 * 			The total number of words in the corpus
	 * @param topicSmoothing
	 * 			Topic smoothing parameter. Default is 50/numTopics
	 * @param maxIter
	 * 			Max iterations to run (or until convergence). -1 (default) waits until convergence.
	 * @param numReducers
	 * 			Max iterations to run (or until convergence). Default 10
	 * @exception 
	 */
	public void analyze(String input,
						String output,
						boolean overwrite,
						int numTopics,
						int numWords,
						double topicSmoothing,
						int maxIter,
						int numReducers) throws ClassNotFoundException, IOException, InterruptedException {
	      if (maxIter < 0) {
	    	  maxIter = -1;
	      }
	      
	      if (numReducers < 0) {
	    	  numReducers = 2;
	      }	

	      if (numTopics < 0) {
	    	  numTopics = 20;
	      }
	      
	      if (numWords < 0) {
	    	  numWords = 20;
	      }
	      
	      if (overwrite) {
	        HadoopUtil.overwriteOutput(output);
	      }
	      
	      topicSmoothing = -1.0;
	      if (topicSmoothing < 1) {
	        topicSmoothing = 50.0 / numTopics;
	      }
	      
	      runJob(input, output, numTopics, numWords, topicSmoothing, maxIter, numReducers);
	}
	
	/**
	 * Run the job using supplied arguments.
	 * 
	 * @param input
	 *            the directory pathname for input points
	 * @param output
	 *            the directory pathname for output points
	 * @param numTopics
	 *            the number of topics
	 * @param numWords
	 *            the number of words
	 * @param topicSmoothing
	 *            pseudocounts for each topic, typically small &lt; .5
	 * @param maxIterations
	 *            the maximum number of iterations
	 * @param numReducers
	 *            the number of Reducers desired
	 * @throws IOException
	 */
	public void runJob(String input, 
					   String output, 
					   int numTopics,
					   int numWords, 
					   double topicSmoothing, 
					   int maxIterations,
					   int numReducers) throws IOException, InterruptedException, ClassNotFoundException {

		String stateIn = output + "/state-0";
		writeInitialState(stateIn, numTopics, numWords);
		double oldLL = Double.NEGATIVE_INFINITY;
		boolean converged = false;

		for (int iteration = 0; ((maxIterations < 1) || (iteration < maxIterations))
				&& !converged; iteration++) {
			log.info("Iteration {}", iteration);
			//System.out.println("Iteration: " + iteration);
			
			// point the output to a new directory per iteration
			String stateOut = output + "/state-" + (iteration + 1);
			double ll = runIteration(input, stateIn, stateOut, numTopics,
					numWords, topicSmoothing, numReducers);
			double relChange = (oldLL - ll) / oldLL;

			// now point the input to the old output directory
			log.info("Iteration {} finished. Log Likelihood: {}", iteration, ll);
			log.info("(Old LL: {})", oldLL);
			log.info("(Rel Change: {})", relChange);			
			//System.out.println("Iteration " + iteration + " finished. Log Likelihood: " + ll);
			//System.out.println("(Old LL: " + oldLL + ")");
			//System.out.println("(Rel Change: " + relChange + ")");

			converged = (iteration > 2) && (relChange < OVERALL_CONVERGENCE);
			stateIn = stateOut;
			oldLL = ll;
		}
	}

	private void writeInitialState(String statePath, int numTopics,
			int numWords) throws IOException {
		Path dir = new Path(statePath);
		Configuration job = new Configuration();
		FileSystem fs = dir.getFileSystem(job);

		DoubleWritable v = new DoubleWritable();

		Random random = RandomUtils.getRandom();

		for (int k = 0; k < numTopics; ++k) {
			Path path = new Path(dir, "part-" + k);
			SequenceFile.Writer writer = new SequenceFile.Writer(fs, job, path,
					IntPairWritable.class, DoubleWritable.class);

			double total = 0.0; // total number of pseudo counts we made
			for (int w = 0; w < numWords; ++w) {
				IntPairWritable kw = new IntPairWritable(k, w);
				// A small amount of random noise, minimized by having a floor.
				double pseudocount = random.nextDouble() + 1.0E-8;
				total += pseudocount;
				v.set(Math.log(pseudocount));
				writer.append(kw, v);
			}
			IntPairWritable kTsk = new IntPairWritable(k, TOPIC_SUM_KEY);
			v.set(Math.log(total));
			writer.append(kTsk, v);

			writer.close();
		}
	}

	private double findLL(String statePath, Configuration job) throws IOException {
		Path dir = new Path(statePath);
		FileSystem fs = dir.getFileSystem(job);

		double ll = 0.0;

		IntPairWritable key = new IntPairWritable();
		DoubleWritable value = new DoubleWritable();
		for (FileStatus status : fs.globStatus(new Path(dir, "part-*"))) {
			Path path = status.getPath();
			SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, job);
			while (reader.next(key, value)) {
				if (key.getFirst() == LOG_LIKELIHOOD_KEY) {
					ll = value.get();
					break;
				}
			}
			reader.close();
		}

		return ll;
	}

	/**
	 * Run the job using supplied arguments
	 * 
	 * @param input
	 *            the directory pathname for input points
	 * @param stateIn
	 *            the directory pathname for input state
	 * @param stateOut
	 *            the directory pathname for output state
	 * @param numTopics
	 *            the number of clusters
	 * @param numReducers
	 *            the number of Reducers desired
	 */
	public double runIteration(String input, 
									  String stateIn,
									  String stateOut, 
									  int numTopics, 
									  int numWords,
									  double topicSmoothing, 
									  int numReducers) throws IOException, InterruptedException, ClassNotFoundException {
		Configuration conf = new Configuration();
		conf.set(STATE_IN_KEY, stateIn);
		conf.set(NUM_TOPICS_KEY, Integer.toString(numTopics));
		conf.set(NUM_WORDS_KEY, Integer.toString(numWords));
		conf.set(TOPIC_SMOOTHING_KEY, Double.toString(topicSmoothing));

		Job job = new Job(conf);

		job.setOutputKeyClass(IntPairWritable.class);
		job.setOutputValueClass(DoubleWritable.class);
		FileInputFormat.addInputPaths(job, input);
		Path outPath = new Path(stateOut);
		FileOutputFormat.setOutputPath(job, outPath);

		job.setMapperClass(LDAMapper.class);
		job.setReducerClass(LDAReducer.class);
		job.setCombinerClass(LDAReducer.class);
		job.setNumReduceTasks(numReducers);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setJarByClass(LDADriver.class);

		job.waitForCompletion(true);
		return findLL(stateOut, conf);
	}

	static LDAState createState(Configuration job) throws IOException {
		String statePath = job.get(STATE_IN_KEY);
		int numTopics = Integer.parseInt(job.get(NUM_TOPICS_KEY));
		int numWords = Integer.parseInt(job.get(NUM_WORDS_KEY));
		double topicSmoothing = Double
				.parseDouble(job.get(TOPIC_SMOOTHING_KEY));

		Path dir = new Path(statePath);
		FileSystem fs = dir.getFileSystem(job);

		DenseMatrix pWgT = new DenseMatrix(numTopics, numWords);
		double[] logTotals = new double[numTopics];
		double ll = 0.0;

		IntPairWritable key = new IntPairWritable();
		DoubleWritable value = new DoubleWritable();
		for (FileStatus status : fs.globStatus(new Path(dir, "part-*"))) {
			Path path = status.getPath();
			SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, job);
			while (reader.next(key, value)) {
				int topic = key.getFirst();
				int word = key.getSecond();
				if (word == TOPIC_SUM_KEY) {
					logTotals[topic] = value.get();
					if (Double.isInfinite(value.get())) {
						throw new IllegalArgumentException();
					}
				} else if (topic == LOG_LIKELIHOOD_KEY) {
					ll = value.get();
				} else {
					if (!((topic >= 0) && (word >= 0))) {
						throw new IllegalArgumentException(topic + " " + word);
					}
					if (pWgT.getQuick(topic, word) != 0.0) {
						throw new IllegalArgumentException();
					}
					pWgT.setQuick(topic, word, value.get());
					if (Double.isInfinite(pWgT.getQuick(topic, word))) {
						throw new IllegalArgumentException();
					}
				}
			}
			reader.close();
		}

		return new LDAState(numTopics, numWords, topicSmoothing, pWgT, logTotals, ll);
	}
}
