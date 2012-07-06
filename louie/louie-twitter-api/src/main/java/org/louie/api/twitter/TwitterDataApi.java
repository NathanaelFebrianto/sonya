package org.louie.api.twitter;

import java.util.ArrayList;
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
	 * Looks up the user list with json format string by the screen names.
	 * 
	 * @param screenNames
	 * @throws TwitterApiException
	 */
	public List<String> lookupUsers(String[] screenNames) throws TwitterApiException {
		try {
			List<String> result = new ArrayList<String>();

			List<String[]> screenNamesList = this.getScreenNamesByMaxUnit(screenNames);
			
			for (String[] screenNameArray : screenNamesList) {				
				ResponseList<User> users = twitter.lookupUsers(screenNameArray);
				for (User user : users) {
					String json = DataObjectFactory.getRawJSON(user);
					System.out.println(json);
					
					result.add(json);
				}			
			}

			return result;
		} catch (TwitterException e) {
			String msg = "Can't collect users from twitter4j.";
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
