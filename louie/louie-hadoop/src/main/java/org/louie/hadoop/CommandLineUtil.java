package org.louie.hadoop;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.cli.Options;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.util.HelpFormatter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.GenericOptionsParser;

public final class CommandLineUtil {

	private CommandLineUtil() { }

	public static void printHelp(Group group) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.setGroup(group);
		formatter.print();
	}

	/**
	 * Print the options supported by {@code GenericOptionsParser}. In addition
	 * to the options supported by the job, passed in as the group parameter.
	 * 
	 * @param group
	 *            job-specific command-line options.
	 */
	public static void printHelpWithGenericOptions(Group group) throws IOException {
		Options ops = new Options();
		new GenericOptionsParser(new Configuration(), ops, new String[0]);
		org.apache.commons.cli.HelpFormatter fmt = new org.apache.commons.cli.HelpFormatter();
		fmt.printHelp("<command> [Generic Options] [Job-Specific Options]",
				"Generic Options:", ops, "");

		PrintWriter pw = new PrintWriter(System.out, true);
		HelpFormatter formatter = new HelpFormatter();
		formatter.setGroup(group);
		formatter.setPrintWriter(pw);
		formatter.printHelp();
		formatter.setFooter("Specify HDFS directories while running on hadoop; else specify local file system directories");
		formatter.printFooter();

		pw.flush();
	}

	public static void printHelpWithGenericOptions(Group group, OptionException oe) throws IOException {
		Options ops = new Options();
		new GenericOptionsParser(new Configuration(), ops, new String[0]);
		org.apache.commons.cli.HelpFormatter fmt = new org.apache.commons.cli.HelpFormatter();
		fmt.printHelp("<command> [Generic Options] [Job-Specific Options]",
				"Generic Options:", ops, "");

		PrintWriter pw = new PrintWriter(System.out, true);
		HelpFormatter formatter = new HelpFormatter();
		formatter.setGroup(group);
		formatter.setPrintWriter(pw);
		formatter.setException(oe);
		formatter.print();
		pw.flush();
	}

}
