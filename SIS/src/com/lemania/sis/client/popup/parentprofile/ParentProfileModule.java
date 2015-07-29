package com.lemania.sis.client.popup.parentprofile;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ParentProfileModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		bindPresenterWidget(ParentProfilePresenter.class,
				ParentProfilePresenter.MyView.class, ParentProfileView.class);
	}
}
