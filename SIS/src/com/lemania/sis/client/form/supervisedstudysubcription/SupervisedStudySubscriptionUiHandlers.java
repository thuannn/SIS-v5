package com.lemania.sis.client.form.supervisedstudysubcription;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionProxy;

interface SupervisedStudySubscriptionUiHandlers extends UiHandlers {
	//
	void loadAppliedStudentsByDate( String date );
	//
	void addCourseSubscription( String studentID, String profID, String date, boolean R, boolean ES, String note, String courseID );
	//
	void removeCourseSubscription( CourseSubscriptionProxy subscription, String date );
	//
	void saveSubscriptionNote1( CourseSubscriptionProxy subscription, String note, String date );
}