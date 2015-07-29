package com.lemania.sis.client.form.studylogmgt;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class StudyLogManagementModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(StudyLogManagementPresenter.class, StudyLogManagementPresenter.MyView.class, StudyLogManagementView.class, StudyLogManagementPresenter.MyProxy.class);
    }
}