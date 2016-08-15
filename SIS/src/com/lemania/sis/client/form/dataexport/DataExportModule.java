package com.lemania.sis.client.form.dataexport;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class DataExportModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(DataExportPresenter.class, DataExportPresenter.MyView.class, DataExportView.class, DataExportPresenter.MyProxy.class);
    }
}