package org.louie.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * This class is date utilities.
 * 
 * @author Younggue Bae
 */
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
	public static final Date convertStringToDate(String format, String strDate, Locale locale) {
		try {
			SimpleDateFormat sdfmt = new SimpleDateFormat(format, locale);
			Date date = sdfmt.parse(strDate);
			
			return date;
		} catch (ParseException e) {
			System.out.println(strDate);
			e.printStackTrace();
			return null;
		}
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
	
	public static String getWeekOfYear(Date date) {
		if (date == null)
			return "";

		Calendar cal = new GregorianCalendar();
		cal.setTime(date);

		int year = cal.get(Calendar.YEAR);
		int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
		
		String result = String.valueOf(year) + String.valueOf(weekOfYear);
		
		if (result.length() < 6) {
			result = result.substring(0, 4) + "0" + result.substring(4);
		}
		
		return result;
	}
	
	public static Date getStartDateOfWeek(Date date) {
		if (date == null)
			return null;

		Calendar cal = new GregorianCalendar();
		cal.setTime(date);

		int year = cal.get(Calendar.YEAR);
		int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
		cal.set(Calendar.DAY_OF_WEEK, 1);
		
		return cal.getTime();
	}
	
	public static Date getEndDateOfWeek(Date date) {
		if (date == null)
			return null;

		Calendar cal = new GregorianCalendar();
		cal.setTime(date);

		int year = cal.get(Calendar.YEAR);
		int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
		cal.set(Calendar.DAY_OF_WEEK, 7);
		
		return cal.getTime();
	}
	
	public static Date getStartDateOfWeek(String weekofyear) {
		if (weekofyear == null || weekofyear.length() != 6)
			return null;

		
		Calendar cal = new GregorianCalendar();

		int year = Integer.valueOf(weekofyear.substring(0, 4));
		int week = Integer.valueOf(weekofyear.substring(4, 6));
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.WEEK_OF_YEAR, week);
		cal.set(Calendar.DAY_OF_WEEK, 1);

		//DecimalFormat df = new DecimalFormat("00");
		//String month = df.format(cal.get(Calendar.MONTH) + 1);
		//String date = df.format(cal.get(Calendar.DATE));
		//return year + month + date;
		return cal.getTime();
	}
	
	public static Date getEndDateOfWeek(String weekofyear) {
		if (weekofyear == null || weekofyear.length() != 6)
			return null;

		Calendar cal = new GregorianCalendar();

		int year = Integer.valueOf(weekofyear.substring(0, 4));
		int week = Integer.valueOf(weekofyear.substring(4, 6));
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.WEEK_OF_YEAR, week);
		cal.set(Calendar.DAY_OF_WEEK, 7);

		//DecimalFormat df = new DecimalFormat("00");
		//String month = df.format(cal.get(Calendar.MONTH) + 1);
		//String date = df.format(cal.get(Calendar.DATE));
		//return year + month + date;
		
		return cal.getTime();
	}
	
	public static void main(String[] args) {
		String weekOfYear = DateUtil.getWeekOfYear(DateUtil.convertStringToDate("yyyyMMdd", "20120201", Locale.KOREA));
		System.out.println("week of year == " + weekOfYear);
		
		System.out.println("start date of week == " + DateUtil.getStartDateOfWeek(new Date()));
		System.out.println("end date of week == " + DateUtil.getEndDateOfWeek(new Date()));
	}
}
