package com.lemania.sis.server.bean.studylog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.Subject;
import com.lemania.sis.server.bean.assignment.Assignment;
import com.lemania.sis.server.bean.classe.Classe;
import com.lemania.sis.server.bean.professor.Professor;
import com.lemania.sis.server.service.MyDAOBase;

public class StudyLogDao extends MyDAOBase {

	/*
	 * */
	public void initialize(){
		return;
	}
	
	
	/*
	 * */
	public void save(StudyLog studyLog){
		ofy().save().entities( studyLog );
	}
	
	
	/*
	 * */
	public StudyLog saveAndReturn( StudyLog studyLog ){
		Key<StudyLog> key = ofy().save().entities( studyLog ).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * */
	public StudyLog saveAndReturn( String subjectId, String classeId, String logTitle, 
			String logContent, String logDate, String logFileName ) {
		//
		StudyLog studyLog = new StudyLog();
		studyLog.setKeySubject( Key.create(Subject.class, Long.parseLong(subjectId)) );
		studyLog.setKeyClasse( Key.create(Classe.class, Long.parseLong(classeId)) );
		studyLog.setLogTitle(logTitle);
		studyLog.setLogContent(logContent);
		studyLog.setLogDate(logDate);
		studyLog.setFileName(logFileName);
		//
		Key<StudyLog> key = ofy().save().entities( studyLog ).now().keySet().iterator().next();
		try {
			//
			StudyLog returnLog = ofy().load().key(key).now();
			returnLog.setClasseName( ofy().load().key( returnLog.getKeyClasse()).now().getClassName() );
			returnLog.setSubjectName( ofy().load().key( returnLog.getKeySubject()).now().getSubjectName() );
			return returnLog;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public StudyLog updateLog( String logTitle, String logContent, String logDate, String editLogId, String logFileName ) {
		//
		StudyLog studyLog = ofy().load().key( Key.create( StudyLog.class, Long.parseLong( editLogId )) ).now();
		studyLog.setLogTitle( logTitle );
		studyLog.setLogContent( logContent );
		studyLog.setLogDate( logDate );
		if ( ! logFileName.equals("") )					// since we're updating a log, only overwrite if user re-upload a file
			studyLog.setFileName( logFileName );
		//
		Key<StudyLog> key = ofy().save().entities( studyLog ).now().keySet().iterator().next();
		try {
			//
			StudyLog returnLog = ofy().load().key(key).now();
			returnLog.setClasseName( ofy().load().key( returnLog.getKeyClasse()).now().getClassName() );
			returnLog.setSubjectName( ofy().load().key( returnLog.getKeySubject()).now().getSubjectName() );
			return returnLog;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public List<StudyLog> saveAndReturnBatch( String assIdList, 
			String logTitle, String logContent, String logDate, String logFileName ) {
		//
		StudyLog studyLog;
		List<StudyLog> studyLogs = new ArrayList<StudyLog>();
		Key<StudyLog> key;
		String[] assIds = assIdList.split(Pattern.quote("|"));
		Assignment assignment;
		//
		for (String assId : assIds ) {
			if (assId.equals(""))
				continue;
			studyLog = new StudyLog();
			assignment = ofy().load().key( Key.create(Assignment.class, Long.parseLong(assId)) ).now();
			studyLog.setKeySubject( assignment.getSubject() );
			studyLog.setKeyClasse( assignment.getClasse() );
			studyLog.setLogTitle(logTitle);
			studyLog.setLogContent(logContent);
			studyLog.setLogDate(logDate);
			studyLog.setFileName( logFileName );
			//
			key = ofy().save().entities( studyLog ).now().keySet().iterator().next();
			try {
				studyLogs.add( ofy().load().key(key).now() );
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		//
		populateIgnoredData( studyLogs );
		return studyLogs;
	}
	
	
	/*
	 * */
	public void removeStudyLog( StudyLog studyLog ){
		//
		// Delete file
		if ( !studyLog.getFileName().equals("") ) {
			GcsService gcsService = GcsServiceFactory
					.createGcsService(new RetryParams.Builder()
							.initialRetryDelayMillis(10).retryMaxAttempts(10)
							.totalRetryPeriodMillis(15000).build());
			try {
				gcsService.delete( new GcsFilename( "eprofil-uploads", studyLog.getFileName() ) );
			} catch (IOException e) {
				//
				e.printStackTrace();
			}
		}
		//
		ofy().delete().entities( studyLog ).now();
	}
	
	
	
	/* 
	 * */
	public List<StudyLog> listAll(){
		Query<StudyLog> q = ofy().load().type( StudyLog.class );
		List<StudyLog> returnList = new ArrayList<StudyLog>();
		for ( StudyLog studyLog : q.list() ){
			returnList.add( studyLog );
		}
		//
		populateIgnoredData( returnList );
		Collections.sort( returnList, new StudyLogDateComparator() );
		return returnList;
	}
	
	
	/* 
	 * */
	public List<StudyLog> listAllBySubject( String subjectId, String dateFrom, String dateTo ) {
		//
		Key<Subject> keySubject = Key.create( Subject.class, Long.parseLong(subjectId) );
		Query<StudyLog> q = ofy().load().type( StudyLog.class )
				.filter("keySubject", keySubject)
				.filter("logDate >=", dateFrom )
				.filter("logDate <=", dateTo )
				.order("logDate");
		List<StudyLog> returnList = new ArrayList<StudyLog>();
		for ( StudyLog studyLog : q.list() ){
			returnList.add( studyLog );
		}
		//
		populateIgnoredData( returnList );
		Collections.sort( returnList, new StudyLogDateComparator() );
		return returnList;
	}
	
	
	/* 
	 * */
	public List<StudyLog> listAllByClass( String classId, String dateFrom, String dateTo  ) {
		//
		Key<Classe> keyClasse = Key.create( Classe.class, Long.parseLong(classId) );
		Query<StudyLog> q = ofy().load().type( StudyLog.class )
				.filter("keyClasse", keyClasse)
				.filter("logDate >=", dateFrom )
				.filter("logDate <=", dateTo )
				.order("logDate");
		List<StudyLog> returnList = new ArrayList<StudyLog>();
		for ( StudyLog studyLog : q.list() ){
			returnList.add( studyLog );
		}
		//
		populateIgnoredData( returnList );
		Collections.sort( returnList, new StudyLogDateComparator() );
		return returnList;
	}
	
	
	/* 
	 * */
	public List<StudyLog> listAllBySubjectClass( String subjectId, String classeId, String dateFrom, String dateTo ) {
		//
		Key<Subject> keySubject = Key.create( Subject.class, Long.parseLong(subjectId) );
		Key<Classe> keyClasse = Key.create( Classe.class, Long.parseLong(classeId) );
		//
		Query<StudyLog> q = ofy().load().type( StudyLog.class )
				.filter("keySubject", keySubject)
				.filter("keyClasse", keyClasse )
				.filter("logDate >=", dateFrom )
				.filter("logDate <=", dateTo )
				.order("logDate");
		List<StudyLog> returnList = new ArrayList<StudyLog>();
		for ( StudyLog studyLog : q.list() ){
			returnList.add( studyLog );
		}
		//
		populateIgnoredData( returnList );
		Collections.sort( returnList, new StudyLogDateComparator() );
		return returnList;
	}
	
	
	/* 
	 * 20150826 - List all study logs from classes for which this professor is responsible
	 * */
	public List<StudyLog> listAllBySubjectProf( String profId, String subjectId, String dateFrom, String dateTo ) {
		//
		// List all classes by prof and subject
		Query<Assignment> qClass = ofy().load().type(Assignment.class)
				.filter("prof", Key.create( Professor.class, Long.parseLong(profId) ))
				.filter("subject", Key.create(Subject.class, Long.parseLong(subjectId)))
				.order("classe");
		List<Classe> listClasses = new ArrayList<Classe>();
		// Goes through the list of assignments and populate the subject list
		Classe cl = new Classe();
		for ( Assignment a : qClass ){
			if (a.getActive()) {				
				cl = ofy().load().key( a.getClasse() ).now();
				listClasses.add( cl );
			}
		}
		
		//
		// Loop through the classes and get the study logs
		Key<Subject> keySubject = Key.create( Subject.class, Long.parseLong(subjectId) );
		Key<Classe> keyClasse;
		Query<StudyLog> q;
		List<StudyLog> returnList = new ArrayList<StudyLog>();
		//
		for ( Classe classe : listClasses ) {
			//
			keyClasse = Key.create( Classe.class, classe.getId() );
			//
			q = ofy().load().type( StudyLog.class )
					.filter("keySubject", keySubject)
					.filter("keyClasse", keyClasse )
					.filter("logDate >=", dateFrom )
					.filter("logDate <=", dateTo )
					.order("logDate");
			for ( StudyLog studyLog : q.list() ){
				returnList.add( studyLog );
			}
			q.list().clear();
		}
		
		//
		populateIgnoredData( returnList );
		Collections.sort( returnList, new StudyLogDateComparator() );
		return returnList;
	}
	
	
	/*
	 * */
	public List<StudyLog> loadBatch( String dateFrom, String dateTo, String assIDs ) {
		//
		Assignment ass;
		Query<StudyLog> q;
		
		List<StudyLog> returnList = new ArrayList<StudyLog>();
		
		//
		if( assIDs == "")
			return returnList;
		String[] assIds = assIDs.split(Pattern.quote("|"));
		
		//
		for( String assignmentId : assIds ) {
			//
			if( assignmentId.equals("") )
				continue;
			
			ass = ofy().load().key( Key.create(Assignment.class, Long.parseLong(assignmentId))).now();
			
			q = ofy().load().type( StudyLog.class )
					.filter("keySubject", ass.getSubject() )
					.filter("keyClasse", ass.getClasse() )
					.filter("logDate >=", dateFrom )
					.filter("logDate <=", dateTo )
					.order("logDate");
			
			for ( StudyLog studyLog : q.list() ){
				returnList.add( studyLog );
			}
		}
		
		//
		populateIgnoredData( returnList );
		Collections.sort( returnList, new StudyLogDateComparator() );
		return returnList;
	}
	
	
	/*************************/
	
	/*
	 * */
	public void populateIgnoredData( List<StudyLog> logs ) {
		//
		for ( StudyLog log : logs ) {
			log.setClasseName( ofy().load().key( log.getKeyClasse()).now().getClassName() );
			log.setSubjectName( ofy().load().key( log.getKeySubject()).now().getSubjectName() );
		}
	}
	
}
