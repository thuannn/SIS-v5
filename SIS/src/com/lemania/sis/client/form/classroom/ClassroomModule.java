package com.lemania.sis.client.form.classroom;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ClassroomModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		bindPresenter(ClassroomPresenter.class,
				ClassroomPresenter.MyView.class, ClassroomView.class,
				ClassroomPresenter.MyProxy.class);
	}
}
