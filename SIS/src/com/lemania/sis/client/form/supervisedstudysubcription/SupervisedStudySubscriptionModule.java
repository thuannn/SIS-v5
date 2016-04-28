package com.lemania.sis.client.form.supervisedstudysubcription;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class SupervisedStudySubscriptionModule extends AbstractPresenterModule {
	//
    @Override
    protected void configure() {
        bindPresenter(SupervisedStudySubscriptionPresenter.class, SupervisedStudySubscriptionPresenter.MyView.class, SupervisedStudySubscriptionView.class, SupervisedStudySubscriptionPresenter.MyProxy.class);
    }
}