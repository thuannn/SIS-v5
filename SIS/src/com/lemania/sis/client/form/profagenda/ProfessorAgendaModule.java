package com.lemania.sis.client.form.profagenda;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ProfessorAgendaModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		bindPresenter(ProfessorAgendaPresenter.class,
				ProfessorAgendaPresenter.MyView.class,
				ProfessorAgendaView.class,
				ProfessorAgendaPresenter.MyProxy.class);
	}
}
