package com.lemania.sis.client.form.supervisedstudytracking;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionProxy;

interface SupervisedStudyTrackingUiHandlers extends UiHandlers {
	//
	void loadAppliedStudentsByDate( String date );
	//
	void loadStudentSubscriptions( CourseSubscriptionProxy subscription );
	//
	void saveSubscriptionNote( CourseSubscriptionProxy subscription, String note );
}