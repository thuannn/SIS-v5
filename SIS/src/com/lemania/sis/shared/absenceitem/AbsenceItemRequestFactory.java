package com.lemania.sis.shared.absenceitem;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.lemania.sis.server.bean.absenceitem.AbsenceItemDao;
import com.lemania.sis.server.service.DaoServiceLocator;

public interface AbsenceItemRequestFactory extends RequestFactory {
	
	@Service ( value= AbsenceItemDao.class, locator= DaoServiceLocator.class )
	interface AbsenceItemRequestContext extends RequestContext {
		//
		Request<List<AbsenceItemProxy>> listAll();
		Request<List<AbsenceItemProxy>> listAllByAssignment( String assignmentId, String strAbsenceDate );
		Request<List<AbsenceItemProxy>> listAllByStudent( String studentId );
		Request<List<AbsenceItemProxy>> listAllByStudentAndDate( String studentId, String dateFrom, String dateTo );
		//
		Request<List<AbsenceItemProxy>> loadAbsentStudents( String dateFrom, String dateTo );
		//
		Request<Void> save(AbsenceItemProxy ai); 
		Request<AbsenceItemProxy> saveAndReturn(AbsenceItemProxy ai);
		//
		Request<AbsenceItemProxy> updateAbsenceLateItem(String aiID, int minutes); 
		Request<AbsenceItemProxy> updateRemarque(String aiID, String strRemarque); 
		Request<AbsenceItemProxy> updateMotif(AbsenceItemProxy ai, String motifID);
		//
		Request<Void> removeAbsenceItem(AbsenceItemProxy ai);
		Request<Void> removeAbsenceItem(String aiID);
		//
		Request<AbsenceItemProxy> saveNotificationDateEmail( String absenceItemID ); 
		Request<AbsenceItemProxy> saveNotificationDateSMS( String absenceItemID ); 
		//
		Request<AbsenceItemProxy> saveAbsenceItem(
				String strAbsenceDate,
				String studentId,
				String periodId,
				String profId,
				String classId,
				String subjectId, 
				String motifId,
				String codeAbsence,
				String profComment,
				int lateMinute,
				boolean justified,
				boolean parentNotified );
	}
	
	AbsenceItemRequestContext absenceItemRequestContext();

}
