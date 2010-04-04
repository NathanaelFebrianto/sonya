/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is a convert util.
 * 
 * @author Young-Gue Bae
 */
public class ConvertUtil {

	
    /**
     * This method generates a string representation of a date's date/time
     * in the format you specify on input.
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
}
