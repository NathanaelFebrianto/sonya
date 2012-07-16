package org.louie.hadoop;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;

/**
 * Superclass of many Hadoop "jobs". A job drives configuration and launch of
 * one or more maps and reduces in order to accomplish some task.
 * 
 * @author Younggue Bae
 */
public abstract class AbstractDriver extends Configured implements Tool {
	
	private Map<String, String> argMap;

	/** internal list of options that have been added */
	private final List<Option> options;
	private Group group;

	protected AbstractDriver() {
		options = new LinkedList<Option>();
	}

	/**
	 * Add an option with no argument whose presence can be checked for using
	 * {@code containsKey} method on the map returned by
	 * {@link #parseArguments(String[])};
	 */
	protected void addFlag(String name, String shortName, String description) {
		options.add(buildOption(name, shortName, description, false, false, null));
	}

	/**
	 * Add an option to the the set of options this job will parse when
	 * {@link #parseArguments(String[])} is called. This options has an argument
	 * with null as its default value.
	 */
	protected void addOption(String name, String shortName, String description) {
		options.add(buildOption(name, shortName, description, true, false, null));
	}

	/**
	 * Add an option to the the set of options this job will parse when
	 * {@link #parseArguments(String[])} is called.
	 * 
	 * @param required
	 *            if true the {@link #parseArguments(String[])} will throw fail
	 *            with an error and usage message if this option is not
	 *            specified on the command line.
	 */
	protected void addOption(String name, String shortName, String description, boolean required) {
		options.add(buildOption(name, shortName, description, true, required, null));
	}

	/**
	 * Add an option to the the set of options this job will parse when
	 * {@link #parseArguments(String[])} is called. If this option is not
	 * specified on the command line the default value will be used.
	 * 
	 * @param defaultValue
	 *            the default argument value if this argument is not found on
	 *            the command-line. null is allowed.
	 */
	protected void addOption(String name, String shortName, String description, String defaultValue) {
		options.add(buildOption(name, shortName, description, true, false, defaultValue));
	}

	/**
	 * Add an arbitrary option to the set of options this job will parse when
	 * {@link #parseArguments(String[])} is called. If this option has no
	 * argument, use {@code containsKey} on the map returned by
	 * {@code parseArguments} to check for its presence. Otherwise, the string
	 * value of the option will be placed in the map using a key equal to this
	 * options long name preceded by '--'.
	 * 
	 * @return the option added.
	 */
	protected Option addOption(Option option) {
		options.add(option);
		return option;
	}

	/**
	 * Build an option with the given parameters. Name and description are
	 * required.
	 * 
	 * @param name
	 *            the long name of the option prefixed with '--' on the
	 *            command-line
	 * @param shortName
	 *            the short name of the option, prefixed with '-' on the
	 *            command-line
	 * @param description
	 *            description of the option displayed in help method
	 * @param hasArg
	 *            true if the option has an argument.
	 * @param required
	 *            true if the option is required.
	 * @param defaultValue
	 *            default argument value, can be null.
	 * @return the option.
	 */
	protected static Option buildOption(String name, String shortName,
			String description, boolean hasArg, boolean required, String defaultValue) {

		DefaultOptionBuilder optBuilder = new DefaultOptionBuilder()
				.withLongName(name).withDescription(description)
				.withRequired(required);

		if (shortName != null) {
			optBuilder.withShortName(shortName);
		}

		if (hasArg) {
			ArgumentBuilder argBuilder = new ArgumentBuilder().withName(name)
					.withMinimum(1).withMaximum(1);

			if (defaultValue != null) {
				argBuilder = argBuilder.withDefault(defaultValue);
			}

			optBuilder.withArgument(argBuilder.create());
		}

		return optBuilder.create();
	}

	/**
	 * 
	 * @param name
	 *            The name of the option
	 * @return the {@link org.apache.commons.cli2.Option} with the name, else
	 *         null
	 */
	protected Option getCLIOption(String name) {
		for (Option option : options) {
			if (option.getPreferredName().equals(name)) {
				return option;
			}
		}
		return null;
	}
	
	/**
	 * Parse the arguments specified based on the options defined using the
	 * various {@code addOption} methods.
	 * 
	 * @return a {@code Map<String,String>} containing options and their
	 *         argument values. The presence of a flag can be tested using
	 *         {@code containsKey}, while argument values can be retrieved using
	 *         {@code get(optionName)}. The names used for keys are the option
	 *         name parameter prefixed by '--'.
	 * 
	 * 
	 */
	public Map<String, String> parseArguments(String[] args) throws IOException {

		Option helpOpt = addOption(helpOption());
		
		GroupBuilder gBuilder = new GroupBuilder().withName("Job-Specific Options:");

		for (Option opt : options) {
			gBuilder = gBuilder.withOption(opt);
		}

		group = gBuilder.create();

		CommandLine cmdLine;
		try {
			Parser parser = new Parser();
			parser.setGroup(group);
			parser.setHelpOption(helpOpt);
			cmdLine = parser.parse(args);

		} catch (OptionException e) {
			CommandLineUtils.printHelpWithGenericOptions(group, e);
			return null;
		}

		if (cmdLine.hasOption(helpOpt)) {
			CommandLineUtils.printHelpWithGenericOptions(group);
			return null;
		}

		argMap = new TreeMap<String, String>();
		maybePut(argMap, cmdLine, this.options.toArray(new Option[this.options.size()]));

		return argMap;
	}
	
	/**
	 * Returns a default command line option for help. Used by all clustering
	 * jobs and many others
	 * */
	public static Option helpOption() {
		return new DefaultOptionBuilder().withLongName("help")
				.withDescription("Print out help").withShortName("h").create();
	}

	/**
	 * Build the option key (--name) from the option name
	 */
	public static String keyFor(String optionName) {
		return "--" + optionName;
	}

	/**
	 * @return the requested option, or null if it has not been specified
	 */
	public String getOption(String optionName) {
		return argMap.get(keyFor(optionName));
	}

	/**
	 * Get the option, else the default
	 * 
	 * @param optionName
	 *            The name of the option to look up, without the --
	 * @param defaultVal
	 *            The default value.
	 * @return The requested option, else the default value if it doesn't exist
	 */
	public String getOption(String optionName, String defaultVal) {
		String res = getOption(optionName);
		if (res == null) {
			res = defaultVal;
		}
		return res;
	}

	/**
	 * @return if the requested option has been specified
	 */
	public boolean hasOption(String optionName) {
		return argMap.containsKey(keyFor(optionName));
	}
	
	protected static void maybePut(Map<String, String> args,
			CommandLine cmdLine, Option... opt) {
		for (Option o : opt) {

			// the option appeared on the command-line, or it has a value
			// (which is likely a default value).
			if (cmdLine.hasOption(o) || cmdLine.getValue(o) != null) {

				// nulls are ok, for cases where options are simple flags.
				Object vo = cmdLine.getValue(o);
				String value = vo == null ? null : vo.toString();
				args.put(o.getPreferredName(), value);
			}
		}
	}

}
