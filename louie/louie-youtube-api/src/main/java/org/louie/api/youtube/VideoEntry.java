package org.louie.api.youtube;

import java.util.List;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

/**
 * This class is a video feed. 
 * 
 * @author Younggue Bae
 */
public class VideoEntry extends Entry {

	@Key
	String id;
	
	@Key
	DateTime published;
	
	@Key
	DateTime updated;
	
	@Key
	String title;
	
	@Key
	String content;
	
	@Key
	List<String> link;

	@Key("author")
	Author author;

	@Key("media:group")
	MediaGroup mediaGroup;
	
	@Key("gd:comments")
	CommentStatistics commentStatistics;
	
	@Key("gd:rating")
	RatingStatistics ratingStatistics;
	
	@Key("yt:statistics")
	Statistics statistics;
	
	@Key("yt:rating")
	Rating rating;
	
	public static class CommentStatistics {
		@Key("gd:feedLink")
		FeedLink feedLink;

		@Override
		public String toString() {
			return "CommentStatistics [feedLink=" + feedLink + "]";
		}
	}
	
	public static class FeedLink {
		@Key("@countHint")
		int countHint;

		@Override
		public String toString() {
			return "FeedLink [countHint=" + countHint + "]";
		}
	}
	
	public static class RatingStatistics {
		@Key("@average")
		double average;	
		
		@Key("@numRaters")
		int numRaters;

		@Override
		public String toString() {
			return "RatingStatistics [average=" + average + ", numRaters=" + numRaters + "]";
		}
	}
	
	public static class Statistics {
		@Key("@favoriteCount")
		int favoriteCount;	
		
		@Key("@viewCount")
		int viewCount;

		@Override
		public String toString() {
			return "Statistics [favoriteCount=" + favoriteCount
					+ ", viewCount=" + viewCount + "]";
		}
	}
	
	public static class Rating {
		@Key("@numDislikes")
		double numDislikes;	
		
		@Key("@numLikes")
		int numLikes;

		@Override
		public String toString() {
			return "Rating [numDislikes=" + numDislikes + ", numLikes="
					+ numLikes + "]";
		}
	}

	@Override
	public String toString() {
		return "VideoEntry [id=" + id + ", published=" + published
				+ ", updated=" + updated + ", title=" + title + ", content="
				+ content + ", link=" + link + ", author=" + author
				+ ", mediaGroup=" + mediaGroup + ", commentStatistics="
				+ commentStatistics + ", ratingStatistics=" + ratingStatistics
				+ ", statistics=" + statistics + ", rating=" + rating + "]";
	}

}