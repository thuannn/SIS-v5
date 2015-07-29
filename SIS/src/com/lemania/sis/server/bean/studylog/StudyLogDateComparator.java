package com.lemania.sis.server.bean.studylog;

import java.util.Comparator;

public class StudyLogDateComparator implements Comparator<StudyLog> {

	@Override
	public int compare(StudyLog o1, StudyLog o2) {
		//
		return o1.getLogDate().compareTo( o2.getLogDate() );
	}

}
