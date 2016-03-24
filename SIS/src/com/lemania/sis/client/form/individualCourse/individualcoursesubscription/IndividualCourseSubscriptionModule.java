package com.lemania.sis.client.form.individualCourse.individualcoursesubscription;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class IndividualCourseSubscriptionModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(IndividualCourseSubscriptionPresenter.class, IndividualCourseSubscriptionPresenter.MyView.class, IndividualCourseSubscriptionView.class, IndividualCourseSubscriptionPresenter.MyProxy.class);
    }
}