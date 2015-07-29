package com.lemania.sis.shared.service;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.lemania.sis.server.service.DaoServiceLocator;
import com.lemania.sis.server.service.SubjectDao;
import com.lemania.sis.server.service.SubjectDao.SubjectPagingLoadResultBean;
import com.lemania.sis.shared.SubjectProxy;
import com.lemania.sis.shared.bulletin.BulletinProxy;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

public interface SubjectRequestFactory extends RequestFactory {
	@Service(value = SubjectDao.class, locator = DaoServiceLocator.class)
	interface SubjectRequestContext extends RequestContext {
		//
		Request<List<SubjectProxy>> listAll();

		Request<List<SubjectProxy>> listAllActive();

		Request<List<SubjectProxy>> listAllActiveByProfile(
				BulletinProxy bulletin);

		Request<List<SubjectProxy>> listAllActiveByProfile(String profileId);

		Request<Void> save(SubjectProxy subject);

		Request<SubjectProxy> saveAndReturn(SubjectProxy subject);

		Request<Void> removeSubject(SubjectProxy subject);

		Request<Void> initialize();

		/*
		 * GXT Grid
		 * */
		@ProxyFor(SubjectPagingLoadResultBean.class)
		public interface SubjectPagingLoadResultProxy extends ValueProxy, PagingLoadResult<SubjectProxy> {
			@Override
			public List<SubjectProxy> getData();
		}

		Request<SubjectPagingLoadResultProxy> listAll(int offset, int limit,
				List<? extends SortInfo> sortInfo,
				List<? extends FilterConfig> filterConfig);
	}

	//
	SubjectRequestContext subjectRequest();
}
