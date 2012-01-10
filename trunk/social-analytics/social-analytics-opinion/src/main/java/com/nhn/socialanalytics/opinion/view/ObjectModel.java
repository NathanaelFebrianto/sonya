package com.nhn.socialanalytics.opinion.view;

import java.io.Serializable;

/**
 * Object class to represents user object model.
 * 
 * @author Younggue Bae
 */
@SuppressWarnings("serial")
public class ObjectModel implements Serializable {

	protected Object userObject;
	protected String display;
	protected String value;
	protected boolean isSelected;

	/**
	 * Default constructor.
	 *
	 */
	public ObjectModel() {
	}

	/**
	 * Constructor.
	 *
     * @param display the display
     * @param value the value
	 */
	public ObjectModel(String display, String value) {
		this.display = display;
		this.value = value;
		this.isSelected = false;
	}
	
	/**
	 * Constructor.
	 *
     * @param userObject the object
     * @param display the display
     * @param value the value
	 */
	public ObjectModel(Object userObject, String display, String value) {
		this.userObject = userObject;
		this.display = display;
		this.value = value;
		this.isSelected = false;
	}

	/**
	 * Constructor.
	 *
     * @param userObject the object
     * @param display the display
     * @param value the value
     * @param isSelected the boolean
	 */
	public ObjectModel(Object userObject, String display, String value, boolean isSelected) {
		this.userObject = userObject;
		this.display = display;
		this.value = value;
		this.isSelected = isSelected;
	}

	public void setUserObject(Object userObject) { this.userObject = userObject;}

	public void setDisplay(String display) { this.display = display;}
	
	public void setValue(String value) { this.value = value;}

	public void setSelected(boolean selected) { isSelected = selected;}

	public Object getUserObject() { return this.userObject;}

	public String getDisplay() { return display; }
	
	public String getValue() { return value; }

	public boolean isSelected() { return isSelected; }

	public void invertSelected() { isSelected = !isSelected; }
	
	public String toString() { return this.display; } 
}
