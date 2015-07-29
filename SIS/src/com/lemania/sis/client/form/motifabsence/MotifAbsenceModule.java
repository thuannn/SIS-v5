package com.lemania.sis.client.form.motifabsence;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class MotifAbsenceModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		bindPresenter(MotifAbsencePresenter.class,
				MotifAbsencePresenter.MyView.class, MotifAbsenceView.class,
				MotifAbsencePresenter.MyProxy.class);
	}
}
