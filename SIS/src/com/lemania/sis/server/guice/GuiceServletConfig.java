package com.lemania.sis.server.guice;

import com.google.inject.Injector;
import com.google.inject.Guice;
import com.google.inject.servlet.GuiceServletContextListener;
import com.lemania.sis.server.guice.ServerModule;
import com.lemania.sis.server.guice.DispatchServletModule;

public class GuiceServletConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice
				.createInjector(new ServerModule(), new DispatchServletModule());
	}
}
