package com.nhn.me2day;


/**
 * A interface for generic manager.
 * 
 * @author Young-Gue Bae
 */
public interface GenericManager {

	/**
	 * Sets the data source.
	 * 
	 * @param mapper the dao mapper
	 */
	public void setDataSource(GenericMapper mapper);
}
