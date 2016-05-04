package com.lemania.sis.client.form.supervisedstudysubcription;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.client.values.Repetition;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionProxy;

interface SupervisedStudySubscriptionUiHandlers extends UiHandlers {
	//
	void loadAppliedStudentsByDate( String date );
	//
	void onProfessorSelected(String profId);
	//
	void onAssignmentSelected(String assignmentId);
	//
	void addCourseSubscription( 
			String studentID, String profID, String date, boolean R, boolean ES, String note, String courseID,
			Repetition repetition, String dateRepetitionEnd );
	//
	void removeCourseSubscription( CourseSubscriptionProxy subscription, String date );
	void removeAllRepetitions( CourseSubscriptionProxy ps, String curDate );
	void removeFutureRepetitions( CourseSubscriptionProxy ps, String curDate );
	//
	void saveSubscriptionNote1( CourseSubscriptionProxy subscription, String note, String date );
	//
	void saveSubscriptionDetails( CourseSubscriptionProxy subscription, String note, String date, 
			String subjectId, boolean isR, boolean isES );
}