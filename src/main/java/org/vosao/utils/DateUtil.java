package org.vosao.utils;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	private static final Format formatter = new SimpleDateFormat("dd.MM.yyyy");
	
	public static String toString(final Date date) {
		return formatter.format(date);
	}
	
	public static Date toDate(final String str) throws ParseException {
		return (Date) formatter.parseObject(str);
	}
	
}
