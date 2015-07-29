package com.lemania.sis.client.values;

import java.util.Arrays;
import java.util.List;

public class ClassPeriodValues {
	//
	public static int numberOfPeriod = 12;
	//
	public static final String P1_Code = "P1";
	public static final String P2_Code = "P2";
	public static final String P3_Code = "P3";
	public static final String P4_Code = "P4";
	public static final String P5_Code = "P5";
	public static final String P6_Code = "P6";
	public static final String P7_Code = "P7";
	public static final String P8_Code = "P8";
	public static final String P9_Code = "P9";
	public static final String P10_Code = "P10";
	public static final String P11_Code = "P11";
	public static final String P12_Code = "P12";
	
	public static final String d2_code = "D2";
	public static final String d3_code = "D3";
	public static final String d4_code = "D4";
	public static final String d5_code = "D5";
	public static final String d6_code = "D6";
	
	public static List<String> colors = Arrays.asList("#BE9F29", "#81B1F4", "#FFAE7B", "#27E875", "#FF7B7E", "#4BEAE4");
	
	public static String getPeriodText(String code) {
		//
		if (code.equals(P1_Code))
				return "08h20-09h00";
		if (code.equals(P2_Code))
				return "09h00-09h40";
		if (code.equals(P3_Code))
				return "09h55-10h35";
		if (code.equals(P4_Code))
				return "10h35-11h15";
		if (code.equals(P5_Code))
				return "11h20-12h00";
		if (code.equals(P6_Code))
				return "13h10-13h50";
		if (code.equals(P7_Code))
				return "13h50-14h30";
		if (code.equals(P8_Code))
				return "14h40-15h20";
		if (code.equals(P9_Code))
				return "15h20-16h00";
		if (code.equals(P10_Code))
				return "16h10-16h50";
		if (code.equals(P11_Code))
				return "16h50-17h30";
		if (code.equals(P12_Code))
				return "17h35-18h15";
		return "";
	}
	
	public static String getDayName(String code) {
		//
		if (code.equals(d2_code))
				return "lundi";
		if (code.equals(d3_code))
				return "mardi";
		if (code.equals(d4_code))
				return "mercredi";
		if (code.equals(d5_code))
				return "jeudi";
		if (code.equals(d6_code))
				return "vendredi";
		return "";
	}
}
