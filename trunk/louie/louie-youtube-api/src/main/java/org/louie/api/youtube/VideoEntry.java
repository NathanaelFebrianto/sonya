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
	public String id;
	
	@Key
	public DateTime published;
	
	@Key
	public DateTime updated;
	
	@Key
	public String title;
	
	@Key
	public String content;
	
	@Key
	public List<String> link;

	@Key("author")
	public Author author;

	@Key("media:group")
	public MediaGroup mediaGroup;
	
	@Key("gd:comments")
	public CommentStatistics commentStatistics;
	
	@Key("gd:rating")
	public RatingStatistics ratingStatistics;
	
	@Key("yt:statistics")
	public Statistics statistics;
	
	@Key("yt:rating")
	public Rating rating;
	
	public static class CommentStatistics {
		@Key("gd:feedLink")
		public FeedLink feedLink;

		@Override
		public String toString() {
			return "CommentStatistics [feedLink=" + feedLink + "]";
		}
	}
	
	public static class FeedLink {
		@Key("@countHint")
		public int countHint;

		@Override
		public String toString() {
			return "FeedLink [countHint=" + countHint + "]";
		}
	}
	
	public static class RatingStatistics {
		@Key("@average")
		public double average;	
		
		@Key("@numRaters")
		public int numRaters;

		@Override
		public String toString() {
			return "RatingStatistics [average=" + average + ", numRaters=" + numRaters + "]";
		}
	}
	
	public static class Statistics {
		@Key("@favoriteCount")
		public int favoriteCount;	
		
		@Key("@viewCount")
		public int viewCount;

		@Override
		public String toString() {
			return "Statistics [favoriteCount=" + favoriteCount
					+ ", viewCount=" + viewCount + "]";
		}
	}
	
	public static class Rating {
		@Key("@numDislikes")
		public double numDislikes;	
		
		@Key("@numLikes")
		public int numLikes;

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