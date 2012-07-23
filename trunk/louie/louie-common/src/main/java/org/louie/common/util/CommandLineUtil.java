package org.louie.common.util;

import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.util.HelpFormatter;

/**
 * Command line utils.
 * 
 * @author Younggue Bae
 */
public final class CommandLineUtil {

	private CommandLineUtil() { }

	public static void printHelp(Group group) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.setGroup(group);
		formatter.print();
	}
	
	public static void printHelp(Group group, OptionException oe) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.setGroup(group);
		//formatter.setException(oe);
		formatter.print();
		
		System.err.println(oe.getMessage());
	}

}
