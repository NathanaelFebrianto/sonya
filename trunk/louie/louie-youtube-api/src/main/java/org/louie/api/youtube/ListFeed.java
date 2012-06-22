package org.louie.api.youtube;

import java.util.List;

/**
 * This class is feed of item list.
 * 
 * @author Younggue Bae
 */
public abstract class ListFeed<T extends Item> extends Feed {
	
	abstract public List<T> getItems();
	abstract public int getTotalItemsSize();

}