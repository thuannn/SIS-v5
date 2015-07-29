package com.lemania.sis.client.form.absencelist;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class AbsenceListModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(AbsenceListPresenter.class, AbsenceListPresenter.MyView.class, AbsenceListView.class, AbsenceListPresenter.MyProxy.class);
    }
}