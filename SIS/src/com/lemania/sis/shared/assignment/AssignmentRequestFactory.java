package com.lemania.sis.shared.assignment;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.lemania.sis.server.bean.assignment.AssignmentDao;
import com.lemania.sis.server.service.DaoServiceLocator;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.SubjectProxy;

public interface AssignmentRequestFactory extends RequestFactory {
	
	@Service(value=AssignmentDao.class, locator=DaoServiceLocator.class)
	interface AssignmentRequestContext extends RequestContext {
		//
		Request<List<AssignmentProxy>> listAll();		
		Request<List<AssignmentProxy>> listAll(String profId);
		Request<List<AssignmentProxy>> listAllActive();
		Request<List<AssignmentProxy>> listAllActive(String profId);
		
		Request<List<ProfessorProxy>> listAllProfessorBySubject(String subjectId);
		Request<List<ProfessorProxy>> listAllProfessorBySubject(String subjectId, String classId);
		
		Request<List<SubjectProxy>> listAllSubjectByProfessor(String profId);
		
		Request<List<ClasseProxy>> listAllClassByProfAndSubject(String profId, String subjectId);
		
		Request<Void> save(AssignmentProxy a);
		Request<AssignmentProxy> saveAndReturn(AssignmentProxy a);
		Request<AssignmentProxy> saveAndReturn(String profId, String classId, String subjectId, Boolean isActive);
		
		Request<AssignmentProxy> updateAssignmentStatus(Long userId, AssignmentProxy assignment, Boolean status);
		
		Request<Void> removeAssignment(AssignmentProxy a);
		Request<Void> initialize();
	}
	
	AssignmentRequestContext assignmentRequest();
}
