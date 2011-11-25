package com.nhn.socialanalytics.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	
    /**
     * Converts data object into string with the specified format.
     *
     * @param format the date pattern the string is in
     * @param date a date object
     * @return a formatted string representation of the date
     */
    public static final String convertDateToString(String format, Date date) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (date == null) {
        } else {
            df = new SimpleDateFormat(format);
            returnValue = df.format(date);
        }

        return returnValue;
    }
    
    /**
     * Converts string into date object.
     * 
     * @param format
     * @param date
     * @return
     */
	public static final Date convertStringToDate(String format, String strDate, Locale locale)
			throws ParseException {
		SimpleDateFormat sdfmt = new SimpleDateFormat(format, locale);

		Date date = sdfmt.parse(strDate);
		return date;
	}
	
	public static Date addDay(Date date, int day) {
		Calendar cal = Calendar.getInstance();

		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, day);
		return cal.getTime();
	}
	
	public static String convertLongToString(String format, long lDate) {
		Date date = new Date(lDate);
		return convertDateToString(format, date);
	}
}
