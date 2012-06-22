package org.louie.api.youtube;

/**
 * This class is view.
 * 
 * @author Younggue Bae
 */
class View {

	static void header(String name) {
		System.out.println();
		System.out.println("============== " + name + " ==============");
		System.out.println();
	}

	static void displayItems(ListFeed<? extends Item> feed) {

			System.out.println("Showing first " + feed.getItems().size() + " of "
					+ feed.getTotalItemsSize() + " total results: ");
			
			for (Item item : feed.getItems()) {
				System.out.println();
				System.out.println("-----------------------------------------------");
				displayItem(item);
			}
	}

	static void displayItem(Item item) {
		
		System.out.println(item.toString());
		
		/*
		System.out.println("title: " + item.title);
		System.out.println("updated: " + item.updated);
		
		if (item instanceof Video) {
			Video video = (Video) item;
			if (video.description != null) {
				System.out.println("description: " + video.description);
			}
			if (!video.tags.isEmpty()) {
				System.out.println("tags: " + video.tags);
			}
			if (video.player != null) {
				System.out.println("play url: " + video.player.defaultUrl);
			}
			
			System.out.println("view count: " + video.viewCount);
			System.out.println("category: " + video.category);
		}
		else if (item instanceof Comment) {
			Comment comment = (Comment) item;
			
			System.out.println("content: " + comment.content);
			System.out.println("id: " + comment.id);
			System.out.println("author: " + comment.author.name);
		}
		*/
	}
	
	static void displayEntry(EntryFeed feed) {
		System.out.println(feed.toString());
		
		/*
		if (feed instanceof UserFeed) {
			UserFeed user = (UserFeed) feed;
			
			System.out.println("id: " + user.id);
			System.out.println("username: " + user.author.name);
			System.out.println("gender: " + user.gender);
			System.out.println("gender: " + user.age);
			System.out.println("location: " + user.location);
		*/
	}
}
