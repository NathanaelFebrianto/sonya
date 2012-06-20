package org.louie.api.youtube;

class View {

	static void header(String name) {
		System.out.println();
		System.out.println("============== " + name + " ==============");
		System.out.println();
	}

	static void display(Feed<? extends Item> feed) {
		System.out.println("Showing first " + feed.items.size() + " of "
				+ feed.totalItems + " videos: ");
		
		for (Item item : feed.items) {
			System.out.println();
			System.out.println("-----------------------------------------------");
			display(item);
		}
	}

	static void display(Item item) {
		System.out.println("Title: " + item.title);
		System.out.println("Updated: " + item.updated);
		
		if (item instanceof Video) {
			Video video = (Video) item;
			if (video.description != null) {
				System.out.println("Description: " + video.description);
			}
			if (!video.tags.isEmpty()) {
				System.out.println("Tags: " + video.tags);
			}
			if (video.player != null) {
				System.out.println("Play URL: " + video.player.defaultUrl);
			}
			
			System.out.println("View Count: " + video.viewCount);
			System.out.println("Category: " + video.category);
		}
	}
}
