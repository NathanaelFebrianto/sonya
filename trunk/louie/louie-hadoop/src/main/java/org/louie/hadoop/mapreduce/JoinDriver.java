package org.louie.hadoop.mapreduce;

import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.louie.hadoop.AbstractDriver;
import org.louie.hadoop.HadoopUtil;

/**
 * This class is a driver to join the base input with joinable input.
 * 
 * @author Younggue Bae
 */
public class JoinDriver extends AbstractDriver {

	@Override
	public int run(String[] args) throws Exception {
		addOption("inputBase", "i", "Base input path to join.", true);
		addOption("inputJoin", "j", "Input path to join with main input.", true);
		addOption("output", "o", "The directory pathname for output.", true);
		addOption("baseKeyColumns", null, "The Key column indexes of base input.(comma delimiter)", true);
		addOption("joinKeyColumns", null, "The Key column indexes of join input.(comma delimiter)", true);
		addOption("joinValueColumns", null, "The value column indexes of join input.(comma delimiter)", true);
		addOption("delimiter", "d", "Delimiter(The default is \\t)", false);
		
		Map<String, String> parsedArgs = parseArguments(args);
		if (parsedArgs == null) {
			return -1;
		}
		
		String inputBase = parsedArgs.get("--inputBase");
		String inputJoin = parsedArgs.get("--inputJoin");
	    String output = parsedArgs.get("--output");
	    String[] baseKeyColumns = parsedArgs.get("--baseKeyColumns").split(",");
	    String[] joinKeyColumns = parsedArgs.get("--joinKeyColumns").split(",");
	    String[] joinValueColumns = parsedArgs.get("--joinValueColumns").split(",");
	    String delimiter = parsedArgs.get("--delimiter");
		
		Configuration conf = getConf();
		
		//conf.addResource(new Path("./conf/core-site.xml"));
		//conf.addResource(new Path("./conf/hdfs-site.xml"));
		//conf.addResource(new Path("./conf/mapred-site.xml"));
		//conf.set("mapred.child.java.opts", "-Xmx1024m");
		
		Path inputBasePath = new Path(inputBase);
		Path inputJoinPath = new Path(inputJoin);
		Path outputPath = new Path(output);
		
	    conf.setStrings("base.key.columns", baseKeyColumns);
	    conf.setStrings("join.key.columns", joinKeyColumns);
	    conf.setStrings("join.value.columns", joinValueColumns);
	    conf.set("input.base.file", inputBasePath.toString());
	    conf.set("input.join.file", inputJoinPath.toString()); 
	    conf.set("delimiter", delimiter);

	    Job job = new Job(conf);
	    
	    job.setJobName("join on " + "(" + inputBase + ") and (" + inputJoin + ")");
	    job.setJarByClass(JoinDriver.class);
	    job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(Text.class);
	    job.setOutputKeyClass(NullWritable.class);
	    job.setOutputValueClass(Text.class);
	    job.setMapperClass(JoinMapper.class);
	    job.setReducerClass(JoinReducer.class);
	    job.setOutputFormatClass(TextOutputFormat.class);
	    
	    FileInputFormat.addInputPath(job, inputBasePath);
	    FileInputFormat.addInputPath(job, inputJoinPath);
	    FileOutputFormat.setOutputPath(job, outputPath);
	    HadoopUtil.delete(conf, outputPath);
	    
	    job.waitForCompletion(true);
		
		return 0;
	}
	
	public static void main(String[] args) throws Exception {
		ToolRunner.run(new Configuration(), new JoinDriver(), args);
	}
}
