package com.lemania.sis.client.form.masteragenda;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class MasterAgendaModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		bindPresenter(MasterAgendaPresenter.class,
				MasterAgendaPresenter.MyView.class, MasterAgendaView.class,
				MasterAgendaPresenter.MyProxy.class);
	}
}
