package com.lemania.sis.client.values;

public class AbsenceValues {
	
	public static String absenceType_Absence_Code = "AB";
	
	public static String absenceType_Late_Code = "LT";
	
	public static String absenceType_Exclude_Code = "EX";
	
	public static String absenceType_Health_Code = "IM";
	
	public static String getCodeFR( String code ) {
		if (code.equals(absenceType_Absence_Code))
			return "AB";
		if (code.equals(absenceType_Late_Code))
			return "R";
		if (code.equals(absenceType_Exclude_Code))
			return "Excl";
		if (code.equals(absenceType_Health_Code))
			return "IN";
		return "";
	}
	
	public static String getDescriptionFR( String code ) {
		if (code.equals(absenceType_Absence_Code))
			return "Absence";
		if (code.equals(absenceType_Late_Code))
			return "Retard";
		if (code.equals(absenceType_Exclude_Code))
			return "Exclusion";
		if (code.equals(absenceType_Health_Code))
			return "Infirmerie";
		return "";
	}
	
}
