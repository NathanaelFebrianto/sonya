package com.nhn.socialanalytics.nlp.kr.termvector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.utils.vectors.TermInfo;
import org.apache.mahout.utils.vectors.io.DelimitedTermInfoWriter;
import org.apache.mahout.utils.vectors.io.SequenceFileVectorWriter;
import org.apache.mahout.utils.vectors.io.TextualVectorWriter;
import org.apache.mahout.utils.vectors.io.VectorWriter;
import org.apache.mahout.utils.vectors.lucene.CachedTermInfo;
import org.apache.mahout.utils.vectors.lucene.LuceneIterable;
import org.apache.mahout.utils.vectors.lucene.TFDFMapper;
import org.apache.mahout.utils.vectors.lucene.VectorMapper;
import org.apache.mahout.vectorizer.TF;
import org.apache.mahout.vectorizer.TFIDF;
import org.apache.mahout.vectorizer.Weight;

public class DocTermVectorWriter {

	public DocTermVectorWriter() {}

	/**
	 * This class creates vectorsfrom the index files.
	 * 
	 * @param inputDir
	 *			The Lucene index directory
	 * @param outputFile
	 *			The vector output file
	 * @param field
	 *			The field in the index
	 * @param idField
	 *			The field in the index containing the index.  If null, then the Lucene internal doc 
	 * 			id is used which is prone to error if the underlying index changes.
	 * @param dictOut 
	 *			The output of the dictionary
	 * @param weightOpt
	 * 			The kind of weight to use. Currently TF or TFIDF
	 * @param delimiter
	 * 			The delimiter for outputing the dictionary
	 * @param power
	 * 			The norm to use, expressed as either a double or \"INF\" if you want to use the Infinite norm.
	 * 			Must be greater or equal to 0. The default is not to normalize
	 * @param max
	 * 			The maximum number of vectors to output. If not specified, then it will loop over all docs
	 * @param outputWriter
	 * 			The VectorWriter to use, either seq
	 * 			(SequenceFileVectorWriter - default) or file (Writes to a File using JSON format)
	 * @param minDF 
	 * 			The minimum document frequency. default is 1
	 * @param maxDFPercent 
	 * 			The max percentage of docs for the DF. can be used to remove really high frequency terms.
	 * 			expressed as an integer between 0 and 100. Default is 99.
	 * @exception
	 */
	public void write(String inputDir,
			   		  String outputFile,
			   		  String field,
			   		  String idField,
			   		  String dicOut,
			   		  String weightOpt,
			   		  String delimiter,
			   		  String power,
			   		  long max,
			   		  String outputWriter,
			   		  int minDF,
			   		  int maxDFPercent) throws Exception {
		if (inputDir != null && !inputDir.equals("")) { // lucene case
			File file = new File(inputDir);
			if (file.exists() && file.isDirectory()) {
				long maxDocs = Long.MAX_VALUE;
				if (max > 0) {
					maxDocs = max;
				}
				if (maxDocs < 0) {
					throw new IllegalArgumentException("maxDocs must be >= 0");
				}
				Directory dir = FSDirectory.open(file);
				IndexReader reader = IndexReader.open(dir, true);
				Weight weight;
				if (weightOpt != null) {
					if (weightOpt.equalsIgnoreCase("tf")) {
						weight = new TF();
					} else if (weightOpt.equalsIgnoreCase("tfidf")) {
						weight = new TFIDF();
					} else {
						throw new Exception("Weight option is not valid!");
					}
				} else {
					weight = new TFIDF();
				}
		          
		        if (minDF < 0)	minDF = 1;
		        if (maxDFPercent < 0) maxDFPercent = 99;
				
				TermInfo termInfo = new CachedTermInfo(reader, field, minDF, maxDFPercent);
				VectorMapper mapper = new TFDFMapper(reader, weight, termInfo);
				double norm = LuceneIterable.NO_NORMALIZING;
				if (power != null) {
					if (power.equals("INF")) {
						norm = Double.POSITIVE_INFINITY;
					} else {
						norm = Double.parseDouble(power);
					}
				}

				LuceneIterable iterable;
				if (norm == LuceneIterable.NO_NORMALIZING) {
					iterable = new LuceneIterable(reader, idField, field, mapper, LuceneIterable.NO_NORMALIZING);
				} else {
					iterable = new LuceneIterable(reader, idField, field, mapper, norm);
				}
				System.out.println("Output File: " + outputFile);

				VectorWriter vectorWriter;
				if (outputWriter != null) {
					if (outputWriter.equals("file")) {
						BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
						vectorWriter = new TextualVectorWriter(writer);
					} else {
						vectorWriter = getSeqFileWriter(outputFile);
					}
				} else {
					vectorWriter = getSeqFileWriter(outputFile);
				}

				long numDocs = vectorWriter.write(iterable, maxDocs);
				vectorWriter.close();
				System.out.println("Wrote: " + numDocs + " vectors");

				if (delimiter == null) delimiter = "\t";
				
				File dictOutFile = new File(dicOut);
				System.out.println("Dictionary Output file: " + dictOutFile);
				
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(
								new FileOutputStream(dictOutFile), Charset.forName("UTF8")));
				DelimitedTermInfoWriter tiWriter = new DelimitedTermInfoWriter(writer, delimiter, field);
				tiWriter.write(termInfo);
				tiWriter.close();
				writer.close();
			}
		}
	}

	private VectorWriter getSeqFileWriter(String outFile) throws IOException {
		Path path = new Path(outFile);
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);

		SequenceFile.Writer seqWriter = SequenceFile.createWriter(fs, conf, path, LongWritable.class, VectorWritable.class);

		return new SequenceFileVectorWriter(seqWriter);
	}
	
}

