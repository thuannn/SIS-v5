package com.lemania.sis.client.form.periodmanagement;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class PeriodManagementModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		bindPresenter(PeriodManagementPresenter.class,
				PeriodManagementPresenter.MyView.class,
				PeriodManagementView.class,
				PeriodManagementPresenter.MyProxy.class);
	}
}
