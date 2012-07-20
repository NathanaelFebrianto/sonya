package org.louie.hadoop.mapreduce;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 * This class is a mapper to join.
 * 
 * @author Younggue Bae
 */
public class JoinMapper extends Mapper<LongWritable, Text, Text, Text> {
	
	private String delimiter = "\t";
	private String inputTag;
	private String[] baseKeyColumns;
	private String[] joinKeyColumns;
	private String[] joinValueColumns;
	
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String record = value.toString();
		
		String strKey = makeKey(record);
		String strValue = makeValue(record);
		context.write(new Text(strKey), new Text(strValue));
	}

	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		super.setup(context);
		
		Configuration conf = context.getConfiguration();

		FileSplit fileSplit = (FileSplit) context.getInputSplit(); 
        String currentInputFile = fileSplit.getPath().toString(); 
        
        delimiter = conf.get("delimiter", "\t");

        String inputBaseFile = conf.get("input.base.file");
		String inputJoinFile = conf.get("input.join.file");
		baseKeyColumns = conf.getStrings("base.key.columns");
		joinKeyColumns = conf.getStrings("join.key.columns");
		joinValueColumns = conf.getStrings("join.value.columns");
		
		System.out.println("@ mapreduce.map.input.file == " + currentInputFile);
		System.out.println("@ input.base.file == " + inputBaseFile);
		System.out.println("@ input.join.file == " + inputJoinFile);
		System.out.println("@ base.key.columns == " + Arrays.asList(baseKeyColumns));
		System.out.println("@ join.key.columns == " + Arrays.asList(joinKeyColumns));
		System.out.println("@ join.value.columns == " + Arrays.asList(joinValueColumns));
		
		if (currentInputFile.equalsIgnoreCase(inputBaseFile)) {
			inputTag = "BASE~"; 
		}
		else if (currentInputFile.equalsIgnoreCase(inputJoinFile)) {
			inputTag = "JOIN~"; 
		}
		else {
			throw new IOException("Failed in identifying join input tag from input files!");
		}		
	}
	
	private String makeKey(String record) {
		StringBuffer key = new StringBuffer();
		
		String[] field = record.split(delimiter);
		if (inputTag.equals("BASE~")) {
			for (String column : baseKeyColumns) {
				int index = Integer.parseInt(column);
				key.append(field[index]).append(delimiter);
			}
		}
		else if (inputTag.equals("JOIN~")) {
			for (String column : joinKeyColumns) {
				int index = Integer.parseInt(column);
				key.append(field[index]).append(delimiter);
			}
		}
		
		return key.toString().trim();
	}
	
	private String makeValue(String record) {
		StringBuffer value = new StringBuffer();
		
		String[] field = record.split(delimiter);

		if (inputTag.equals("BASE~")) {
			value.append(inputTag).append(record);
		}
		else if (inputTag.equals("JOIN~")) {
			value.append(inputTag);
			for (String column : joinValueColumns) {
				int index = Integer.parseInt(column);
				value.append(field[index]).append(delimiter);
			}
		}

		return value.toString().trim();
	}
	
}

