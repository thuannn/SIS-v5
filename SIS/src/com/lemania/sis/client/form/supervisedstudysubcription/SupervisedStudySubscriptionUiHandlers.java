package com.lemania.sis.client.form.supervisedstudysubcription;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionProxy;

interface SupervisedStudySubscriptionUiHandlers extends UiHandlers {
	//
	void loadAppliedStudentsByDate( String date );
	//
	void addCourseSubscription( String studentID, String profID, String date );
	//
	void removeCourseSubscription( CourseSubscriptionProxy subscription, String date );
}