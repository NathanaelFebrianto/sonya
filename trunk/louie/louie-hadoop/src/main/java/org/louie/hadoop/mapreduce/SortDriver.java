package org.louie.hadoop.mapreduce;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.louie.hadoop.HadoopUtil;

/**
 * This class is a driver to sort the column with simple integer value.
 * 
 * @author Younggue Bae
 */
public class SortDriver extends AbstractDriver {

	public static class SortMapper extends Mapper<LongWritable, Text, IntSortable, Text> {
		
		private String delimiter = "\t";
		private String sortOption = "ascending";
		private int sortColumnIndex;
		
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String record = value.toString();
			String[] field = record.split(delimiter);
			
			try {
				int intValue = Integer.parseInt(field[sortColumnIndex]);
				context.write(new IntSortable(sortOption, intValue), new Text(record));
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.getMessage());
				System.err.println("error: record == " + record + " ----> sort column == " + field[sortColumnIndex]);
				throw new InterruptedException(e.getMessage());
			}
		}

		@Override
		public void setup(Context context) throws IOException, InterruptedException {
			super.setup(context);
			
			Configuration conf = context.getConfiguration();
			
			delimiter = conf.get("delimiter", "\t");

			String option = conf.get("sort.option", "ascending").toLowerCase();
			if (option.startsWith("asc")) {
				sortOption = "ascending";
			}
			else if (option.startsWith("desc")) {
				sortOption = "descending";
			}
			
			System.out.println("sort option == " + option);
			
			sortColumnIndex = conf.getInt("sort.column.index", -1);
		}
	}
	
	public static class SortReducer extends Reducer<IntSortable, Text, Text, NullWritable> {
		
		@Override
		protected void reduce(IntSortable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			for (Text val : values) {
				context.write(val, NullWritable.get());
			}
		}

	}
	
	public static class IntSortable implements WritableComparable<IntSortable> {

		private String sortOption = "ascending";
		private int value;
		
		public IntSortable() {
			set("ascending", 0);
		}
		
		public IntSortable(String sortOption, int value) {
			set(sortOption, value);
		}
		
		public void set(String sortOption, int value) {
			this.sortOption = sortOption;
			this.value = value;	
		}
		
		public int getValue() {
			return value;
		}
		
		@Override
		public void write(DataOutput out) throws IOException {
			out.writeInt(value);
		}
		
		@Override
		public void readFields(DataInput in) throws IOException {
			value = in.readInt();
		}
		
		@Override
		public String toString() {
			return String.valueOf(value);
		}

		@Override
		public int compareTo(IntSortable o) {
			if (sortOption.equalsIgnoreCase("descending")) {
				return new Integer(value).compareTo(new Integer(o.getValue()));
			}
			else if (sortOption.equalsIgnoreCase("ascending")) {
				return new Integer(o.getValue()).compareTo(new Integer(value));
			}
			
			return 0;
		}
	}
	
	@Override
	public int run(String[] args) throws Exception {
		addOption("input", "i", "Path to job input directory.", true);
		addOption("output", "o", "The directory pathname for output.", true);
		addOption("sortColumn", "sort", "The sort column index with integer value.", true);
		addOption("sortOption", "option", "The sort option.(asc or desc, The default is asc)", false);
		addOption("delimiter", "d", "Delimiter(The default is \\t)", false);
		
		Map<String, String> parsedArgs = parseArguments(args);
		if (parsedArgs == null) {
			return -1;
		}
		
		String inputs = parsedArgs.get("--input");
		String[] input = inputs.split(",");
	    String output = parsedArgs.get("--output");
	    String sortColumnIndex = parsedArgs.get("--sortColumn");
	    String sortOption = parsedArgs.get("--sortOption");
	    String delimiter = parsedArgs.get("--delimiter");
		
		Configuration conf = getConf();
		
		//conf.addResource(new Path("./conf/core-site.xml"));
		//conf.addResource(new Path("./conf/hdfs-site.xml"));
		//conf.addResource(new Path("./conf/mapred-site.xml"));
		//conf.set("mapred.child.java.opts", "-Xmx1024m");
		
		conf.setInt("sort.column.index", Integer.parseInt(sortColumnIndex));
		conf.set("sort.option", sortOption);
		conf.set("delimiter", delimiter);

		
		logger.info("sort.column.index == " + sortColumnIndex);
		logger.info("sort.option == " + sortOption);
		logger.info("delimiter == " + delimiter);
		
		Path[] inputPath = new Path[input.length];
		for (int i = 0; i < input.length; i++) {
			inputPath[i] = new Path(input[i].trim());
		}
		Path outputPath = new Path(output);
	    
	    Job job = new Job(conf);
	    
	    String srtInputs = inputs;
	    if (inputs.length() > 30) {
	    	srtInputs = "..." + inputs.substring(inputs.length() - 30);
	    }
	    job.setJobName("sorting" + "(" + srtInputs + ")");
	    job.setJarByClass(SortDriver.class);
	    job.setMapOutputKeyClass(IntSortable.class);
	    job.setMapOutputValueClass(Text.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(NullWritable.class);
	    job.setMapperClass(SortMapper.class);
	    job.setInputFormatClass(TextInputFormat.class);
	    job.setReducerClass(SortReducer.class);
	    job.setOutputFormatClass(TextOutputFormat.class);
	    
	    FileInputFormat.setInputPaths(job, inputPath);
	    FileOutputFormat.setOutputPath(job, outputPath);
	    HadoopUtil.delete(conf, outputPath);
	    
	    job.waitForCompletion(true);
		
		return 0;
	}
	
	public static void main(String[] args) throws Exception {
		ToolRunner.run(new Configuration(), new SortDriver(), args);
	}
}
