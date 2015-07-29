package com.lemania.sis.client.popup.periodlistpopup;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class PeriodListPopupModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		bindPresenterWidget(PeriodListPopupPresenter.class,
				PeriodListPopupPresenter.MyView.class,
				PeriodListPopupView.class);
	}
}
