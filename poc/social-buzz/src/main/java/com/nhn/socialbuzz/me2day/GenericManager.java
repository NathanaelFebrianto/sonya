package com.nhn.socialbuzz.me2day;


/**
 * A interface for generic manager.
 * 
 * @author Younggue Bae
 */
public interface GenericManager {

	/**
	 * Sets the data source.
	 * 
	 * @param mapper the dao mapper
	 */
	public void setDataSource(GenericMapper mapper);
}
