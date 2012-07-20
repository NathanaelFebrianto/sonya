package org.louie.hadoop.mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


/**
 * This class is a reducer to join.
 * 
 * @author Younggue Bae 
 */
public class JoinReducer extends Reducer<Text, Text, NullWritable, Text> {
	
	private String delimiter = "\t";
	
	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		String strJoinValue = null;
		List<String> baseValueList = new ArrayList<String>();
		
		for (Text val : values) {
			String line = val.toString();
            String[] field = line.split("~");
            // identify the record source
            if (field[0].equals("JOIN")) {
            	strJoinValue = field[1].trim();
            }
            else if (field[0].equals("BASE")) {
            	String strBaseValue = field[1].trim();
            	baseValueList.add(strBaseValue);
            }
 		}
		
        // pump final output to file
		for (String baseValue : baseValueList) {
			context.write(NullWritable.get(), new Text(baseValue + delimiter + strJoinValue));
		}
	}
	
	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		super.setup(context);
		
		Configuration conf = context.getConfiguration();
		
		delimiter = conf.get("delimiter", "\t");
	}

}
