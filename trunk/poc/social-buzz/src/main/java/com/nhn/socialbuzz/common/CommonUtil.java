package com.nhn.socialbuzz.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {

	
    /**
     * Converts data object into string with the specified format.
     *
     * @param mask the date pattern the string is in
     * @param date a date object
     * @return a formatted string representation of the date
     */
    public static final String convertDateToString(String mask, Date date) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (date == null) {
        } else {
            df = new SimpleDateFormat(mask);
            returnValue = df.format(date);
        }

        return returnValue;
    }
    
    /**
     * Converts string into date object.
     * 
     * @param mask
     * @param date
     * @return
     */
	public static final Date convertStringToDate(String mask, String strDate)
			throws ParseException {
		SimpleDateFormat sdfmt = new SimpleDateFormat(mask);

		Date date = sdfmt.parse(strDate);
		return date;
	}
	
}
