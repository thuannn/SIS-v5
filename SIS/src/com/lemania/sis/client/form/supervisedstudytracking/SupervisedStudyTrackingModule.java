package com.lemania.sis.client.form.supervisedstudytracking;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class SupervisedStudyTrackingModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(SupervisedStudyTrackingPresenter.class, SupervisedStudyTrackingPresenter.MyView.class, SupervisedStudyTrackingView.class, SupervisedStudyTrackingPresenter.MyProxy.class);
    }
}