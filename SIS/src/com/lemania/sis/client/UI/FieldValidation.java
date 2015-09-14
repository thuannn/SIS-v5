package com.lemania.sis.client.UI;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DateBox;

public class FieldValidation {
	
	// User name validation
	public static boolean isValidUserName(String userName) {
		if (userName == null)
			return false;
		return (userName.length() >= 6);
	}
	
	// Password validation
	private final static String PASSWORD_VALIDATION_REGEX = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,32})";
	
	public static String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(?:[a-zA-Z]{2,6})$";
	
	
	/*
	 * */
	public static boolean isValidPassword(String password) {
		if (password == null)
			return false;
		return password.matches(PASSWORD_VALIDATION_REGEX);
	}
	
	// Email validation
	public static boolean isValidEmailAddress( String value ) {
		//
		 if(value == null) return false;
         
         boolean valid = false;
         
         if( value.getClass().toString().equals(String.class.toString()) ) {
                 valid = ((String)value).matches(emailPattern);
         } else {
                 valid = ((Object)value).toString().matches(emailPattern);
         }

         return valid;
	}
	
	// Number validation
	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	/*
	 * */
	public static void selectItemByValue( ListBox list, String value) {
		//
		for (int i=0; i<list.getItemCount(); i++)
			if (list.getValue(i).equals(value)) {
				list.setSelectedIndex(i);
				break;
			}
	}
	
	/*
	 * */
	public static void selectItemByText( ListBox list, String text) {
		//
		for (int i=0; i<list.getItemCount(); i++)
			if (list.getItemText(i).equals(text)) {
				list.setSelectedIndex(i);
				break;
			}
	}
	
	
	//
	public static String swissDateFormat( String date ) {
		return date.substring(6) + "."
				+ date.substring(4, 6) + "."
				+ date.substring(0, 4);
	}
	
	
	/*
	 * */
	public static String getFileName ( String fullPath ) {
		//
		if (fullPath.equals(""))
			return fullPath;
		//
		int startIndex = (fullPath.indexOf('\\') >= 0 ? fullPath.lastIndexOf('\\') : fullPath.lastIndexOf('/'));
		String filename = fullPath.substring(startIndex);
		if (filename.indexOf('\\') == 0 || filename.indexOf('/') == 0) {
			filename = filename.substring(1);
		}
		return filename;
	}
	
	
	/*
	 * */
	public static void setDateRangeCurrentMonth( DateBox dateFrom, DateBox dateTo ) {
		//
		Date date = new Date();
		
		CalendarUtil.setToFirstDayOfMonth(date);
		dateFrom.setValue(date);
		
		CalendarUtil.addMonthsToDate(date, 1);
		CalendarUtil.addDaysToDate(date, -1);
		dateTo.setValue(date);
	}
	
	/*
	 * */
	public static void setDateRangeCurrentWeek( DateBox dateFrom, DateBox dateTo ) {
		//
		DateTimeFormat fmt = DateTimeFormat.getFormat("yyyyMMdd");
		Date firstMonday2015 = fmt.parse("20150105");
		//
		Date date = new Date();
		//
		int weekDayCount = 7;
		int dayCount = CalendarUtil.getDaysBetween(firstMonday2015, date);
		int firstDayOfWeekMargin = 0;
		for (int i=0; i<60; i++) {
			if ( (i * weekDayCount) > dayCount ) {
				firstDayOfWeekMargin = dayCount - ((i-1) * weekDayCount);
				break;
			}
		}
		
		CalendarUtil.addDaysToDate(date, firstDayOfWeekMargin * (-1) );
		dateFrom.setValue(date);
		
		CalendarUtil.addDaysToDate(date, 6);
		dateTo.setValue(date);
	}
	
	
	/*
	 * */
	public static String getFileNameFormat(String date, String fileName) {
		//
		if (fileName.equals(""))
			return "";

		return date + "_" + getFileName(fileName);
	}

}
