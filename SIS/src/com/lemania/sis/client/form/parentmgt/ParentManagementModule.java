package com.lemania.sis.client.form.parentmgt;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ParentManagementModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		bindPresenter(ParentManagementPresenter.class,
				ParentManagementPresenter.MyView.class,
				ParentManagementView.class,
				ParentManagementPresenter.MyProxy.class);
	}
}
