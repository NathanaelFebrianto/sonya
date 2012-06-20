package org.louie.api.twitter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.louie.common.util.DateUtils;
import org.louie.common.util.FileUtils;

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
	public void collectUsers(String file, String[] screenNames) throws TwitterApiException {

		try {
			String baseDate = DateUtils.convertDateToString("yyyyMMddHHmmss", new Date());
			FileUtils.mkdirsFromFullpath(file);
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true), "UTF-8"));
			
			List<String[]> screenNamesList = this.getScreenNamesByMaxUnit(screenNames);
			
			for (String[] screenNameArray : screenNamesList) {				
				ResponseList<User> users = twitter.lookupUsers(screenNameArray);
				for (User user : users) {
					String json = DataObjectFactory.getRawJSON(user);
					System.out.println(json);
					writer.write(baseDate + "\t" + json);
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
	
	private List<String[]> getScreenNamesByMaxUnit(String[] screenNames) {
		List<String[]> screenNameList = new ArrayList<String[]>();
		List<String> itemArray = new ArrayList<String>();
		for (String screenName : screenNames) {
			itemArray.add(screenName);

			if (itemArray.size() == 100) {
				screenNameList.add(itemArray.toArray(new String[itemArray.size()]));
				itemArray.clear();
			}
		}
		
		if (itemArray.size() > 0) {
			screenNameList.add(itemArray.toArray(new String[itemArray.size()]));
		}
		
		return screenNameList;
	}

}
