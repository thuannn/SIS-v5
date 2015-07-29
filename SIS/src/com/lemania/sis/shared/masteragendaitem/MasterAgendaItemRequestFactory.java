package com.lemania.sis.shared.masteragendaitem;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.lemania.sis.server.bean.masteragendaitem.MasterAgendaItem;
import com.lemania.sis.server.bean.masteragendaitem.MasterAgendaItemDao;
import com.lemania.sis.server.service.DaoServiceLocator;

public interface MasterAgendaItemRequestFactory extends RequestFactory {
	
	@Service(value=MasterAgendaItemDao.class, locator=DaoServiceLocator.class)
	interface MasterAgendaItemRequestContext extends RequestContext {
		//	
		Request<Void> initialize();
		
		Request<List<MasterAgendaItemProxy>> listAll();
		Request<List<MasterAgendaItemProxy>> listAllByProfile(String profileId);
		Request<List<MasterAgendaItemProxy>> listAllByProf(String profId);
		Request<List<MasterAgendaItemProxy>> listAllByClassProfileStudent(String classId, String profileId, String bulletinId);

		Request<Void> save(MasterAgendaItemProxy c);
		Request<MasterAgendaItemProxy> saveAndReturn(MasterAgendaItemProxy c);
		
		Request<MasterAgendaItemProxy> addMasterAgendaItem( String jourCode, String periodId, String profileId, String profileSubjectId, String classroomId, int duration );
		
		Request<Void> removeMasterAgendaItem(MasterAgendaItemProxy mai);
	}
	
	MasterAgendaItemRequestContext masterAgendaItemRequestContext();

}
