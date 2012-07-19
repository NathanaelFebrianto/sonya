package org.louie.hadoop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * This class is Hadoop utilities.
 * 
 * @author Younggue Bae
 */
public final class HadoopUtil {

	public static void delete(Configuration conf, Iterable<Path> paths) throws IOException {
		if (conf == null) {
			conf = new Configuration();
		}
		for (Path path : paths) {
			FileSystem fs = path.getFileSystem(conf);
			if (fs.exists(path)) {
				fs.delete(path, true);
			}
		}
	}

	public static void delete(Configuration conf, Path... paths) throws IOException {
		delete(conf, Arrays.asList(paths));
	}
	
	public static int readInt(Path path, Configuration conf) throws IOException {
		FileSystem fs = FileSystem.get(path.toUri(), conf);
		FSDataInputStream fis = fs.open(path);
		int result = 0;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        
        while(reader.ready()){
        	result += Double.parseDouble(reader.readLine());
        }
        reader.close();
        fis.close();
        
		return result;
	}

}
