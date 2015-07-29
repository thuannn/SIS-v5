package com.lemania.sis.client.form.studylogstudent;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class StudyLogStudentModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(StudyLogStudentPresenter.class, StudyLogStudentPresenter.MyView.class, StudyLogStudentView.class, StudyLogStudentPresenter.MyProxy.class);
    }
}