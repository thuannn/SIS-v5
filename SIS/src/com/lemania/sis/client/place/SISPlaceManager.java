package com.lemania.sis.client.place;

import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.TokenFormatter;
import com.lemania.sis.client.place.DefaultPlace;
import com.google.web.bindery.event.shared.EventBus;
import com.google.inject.Inject;

public class SISPlaceManager extends PlaceManagerImpl{
	private final PlaceRequest defaultPlaceRequest;

	@Inject
	public SISPlaceManager(final EventBus eventBus,
			final TokenFormatter tokenFormatter, @DefaultPlace String defaultNameToken) {
		super(eventBus, tokenFormatter);
		// TODO Auto-generated constructor stub
		this.defaultPlaceRequest = new PlaceRequest(defaultNameToken);
	}

	@Override
	public void revealDefaultPlace() {
		// TODO Auto-generated method stub
		revealPlace(defaultPlaceRequest);
	}

}
