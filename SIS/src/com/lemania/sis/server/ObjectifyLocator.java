package com.lemania.sis.server;

import com.google.web.bindery.requestfactory.shared.Locator;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

public class ObjectifyLocator extends Locator<DatastoreObject, Long> {

	@Override
	public DatastoreObject create(Class<? extends DatastoreObject> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public DatastoreObject find(Class<? extends DatastoreObject> clazz, Long id) {
        return ObjectifyService.ofy().load().key( Key.create(clazz, id)).now();
	}

	@Override
	public Class<DatastoreObject> getDomainType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getId(DatastoreObject domainObject) {
		return domainObject.getId();
	}

	@Override
	public Class<Long> getIdType() {
		return Long.class;
	}

	@Override
	public Object getVersion(DatastoreObject domainObject) {
		return domainObject.getVersion();
	}

}
