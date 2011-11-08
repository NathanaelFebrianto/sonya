package com.nhn.socialanalytics.nlp.kr.termvector;

public class DocTermVector {
	
	public DocTermVector() {
		
	}
	
	public static void main(String[] args) {
		DocTermVectorWriter writer = new DocTermVectorWriter();
		
		try {	
			writer.write(
		   		"./bin/index/",		// inputDir
		   		"./bin/vectors", 	// outputFile
				 "predicate",			// field
				 null,				// idField
				 "./bin/dic.txt",	// dictOut
				 "tf",				// weightOpt
				 null,				// delimiter
				 null,				// power
				 Long.MAX_VALUE,	// max
				 "file",			// outputWriter
				 1,					// minDF
				 99					// maxDFPercent		
				);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
