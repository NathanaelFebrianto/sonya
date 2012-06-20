package org.louie.api.twitter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.json.DataObjectFactory;

/**
 * This class is a Twitter api.
 * 
 * @author Younggue Bae
 */
public class TwitterDataApi {
	
	private Twitter twitter;
	
	/**
	 * Constructor.
	 */
	public TwitterDataApi() {
		twitter = new TwitterFactory().getInstance();
	}
	
	/**
	 * Collects and writes users with json format into file by screen names.
	 * 
	 * @param file
	 * @param screenNames
	 * @throws TwitterApiException
	 */
	public void collectUsers(String file, List<String[]> screenNames) throws TwitterApiException {

		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true), "UTF-8"));
			
			System.out.println("list size == " + screenNames.size());
			
			for (String[] screenNameArray : screenNames) {
				System.out.println("array size of screenNames == " + screenNameArray.length);
				
				ResponseList<User> users = twitter.lookupUsers(screenNameArray);
				for (User user : users) {
					String json = DataObjectFactory.getRawJSON(user);
					System.out.println(json);
					writer.write(json);
					writer.newLine();
				}			
			}
			
			writer.close();
		} catch (TwitterException e) {
			String msg = "Can't collect users from twitter4j.";
			throw new TwitterApiException(msg, e);
		} catch (IOException e) {
			String msg = "Can't write users into file.";
			throw new TwitterApiException(msg, e);
		}
	}

}
