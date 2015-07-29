package com.lemania.sis.client.form.studentagenda;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class StudentAgendaModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		bindPresenter(StudentAgendaPresenter.class,
				StudentAgendaPresenter.MyView.class, StudentAgendaView.class,
				StudentAgendaPresenter.MyProxy.class);
	}
}
