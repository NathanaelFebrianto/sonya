/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * UI handler for swing.
 * 
 * @author Young-Gue Bae
 */
public class UIHandler {
	private static UIHandler instance;
	static ResourceBundle texts = null;
	Locale locale = null;

	/**
	 * Constructor.
	 * 
	 * @param resource
	 *            the resource name
	 */
	public UIHandler(String resource) {
		texts = ResourceBundle.getBundle(resource);
		locale = texts.getLocale();
	}

	/**
	 * Sets the resource bundle name.
	 * 
	 * @param resource
	 *            the resource name
	 */
	public static void setResourceBundle(String resource) {
		instance = new UIHandler(resource);
	}

	/**
	 * Sets the default look and feel.
	 * 
	 */
	public static void setDefaultLookAndFeel() {
		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			//UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.Plastic3DLookAndFeel");
			//UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Changes the all swing component default font.
	 * 
	 */
	public static void changeAllSwingComponentDefaultFont() {
		try {
			UIDefaults swingComponentDefaultTable = UIManager.getDefaults();

			Enumeration<Object> allDefaultKey = swingComponentDefaultTable
					.keys();
			while (allDefaultKey.hasMoreElements()) {
				String defaultKey = allDefaultKey.nextElement().toString();
				if (defaultKey.indexOf("font") != -1) {
					Font newDefaultFont = new Font("³ª´®°íµñ", Font.PLAIN, 12);
					UIManager.put(defaultKey, newDefaultFont);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the image icon.
	 * 
	 * @param imageUrl
	 *            the image url
	 * @return ImageIcon the ImageIcon
	 */
	public static ImageIcon getImageIcon(String imageUrl) {
		try {
			URL url = UIHandler.class.getResource(imageUrl);
			return new ImageIcon(url);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the text by the specified key from the resource bundle.
	 * 
	 * @param key
	 *            the key
	 * @return String the text
	 */
	public static String getText(String key) {
		try {
			if (instance == null) {
				instance = new UIHandler("graph");
			}
			return texts.getString(key);
			// return encodingKorean(texts.getString(key));
		} catch (Exception e) {
			e.printStackTrace();
			return key;
		}
	}

	/**
	 * Locates the component to the center of the parent container.
	 * 
	 * @param parent
	 *            the parent container
	 * @param comp
	 *            the componet to locate to the center
	 */
	public static void center(Container parent, Component comp) {
		int x, y;
		Rectangle parentBounds;
		Dimension compSize = comp.getSize();

		// If Container is null or smaller than the component
		// then our bounding rectangle is the
		// whole screen
		if ((parent == null) || (parent.getBounds().width < compSize.width)
				|| (parent.getBounds().height < compSize.height)) {
			parentBounds = new Rectangle(comp.getToolkit().getScreenSize());
			parentBounds.setLocation(0, 0);
		}
		// Else our bounding rectangle is the Container
		else {
			parentBounds = parent.getBounds();
		}

		// Place the component so its center is the same
		// as the center of the bounding rectangle
		x = parentBounds.x + ((parentBounds.width / 2) - (compSize.width / 2));
		y = parentBounds.y
				+ ((parentBounds.height / 2) - (compSize.height / 2));
		comp.setLocation(x, y);
	}

	/**
	 * Gets the point to locate the component to the center of the container.
	 * 
	 * @param parent
	 *            the parent container
	 * @param comp
	 *            the componet to locate to the center
	 * @return Point the point
	 */
	public static Point getCenterPoint(Container parent, Component comp) {
		int x, y;
		Rectangle parentBounds;
		Dimension compSize = comp.getSize();

		// If Container is null or smaller than the component
		// then our bounding rectangle is the
		// whole screen
		if ((parent == null) || (parent.getBounds().width < compSize.width)
				|| (parent.getBounds().height < compSize.height)) {
			parentBounds = new Rectangle(comp.getToolkit().getScreenSize());
			parentBounds.setLocation(0, 0);
		}
		// Else our bounding rectangle is the Container
		else {
			parentBounds = parent.getBounds();
		}

		// Place the component so its center is the same
		// as the center of the bounding rectangle
		x = parentBounds.x + ((parentBounds.width / 2) - (compSize.width / 2));
		y = parentBounds.y
				+ ((parentBounds.height / 2) - (compSize.height / 2));

		return new Point(x, y);
	}

	/*
	 * private static String encodingKorean(String text) throws
	 * UnsupportedEncodingException { String result = null; try { if (text !=
	 * null) result = new String(text.getBytes("8859_1"), "KSC5601"); }
	 * catch(java.io.UnsupportedEncodingException e) { throw e; } return result;
	 * }
	 */
}
