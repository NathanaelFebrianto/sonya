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

	static void display(Feed<? extends Item> feed) {

		System.out.println("Showing first " + feed.getItems().size() + " of "
				+ feed.getTotalItemsSize() + " total results: ");
		
		for (Item item : feed.getItems()) {
			System.out.println();
			System.out.println("-----------------------------------------------");
			display(item);
		}
	}

	static void display(Item item) {
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
		else if (item instanceof User) {
			User user = (User) item;
			
			System.out.println("id: " + user.id);
			System.out.println("username: " + user.author.name);
			System.out.println("gender: " + user.gender);
			System.out.println("gender: " + user.age);
			System.out.println("location: " + user.location);
		}
	}
}
