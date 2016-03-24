package com.lemania.sis.client.form.individualCourse.individualcoursesubscription;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionProxy;

interface IndividualCourseSubscriptionUiHandlers extends UiHandlers {
	//
	void loadAppliedStudentsByDate( String date );
	//
	void addCourseSubscription( String studentID, String profID, String date );
	//
	void removeCourseSubscription( CourseSubscriptionProxy subscription, String date );
}