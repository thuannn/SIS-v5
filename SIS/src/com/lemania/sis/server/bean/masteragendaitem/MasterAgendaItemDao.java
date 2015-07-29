package com.lemania.sis.server.bean.masteragendaitem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.Profile;
import com.lemania.sis.server.Subject;
import com.lemania.sis.server.bean.bulletin.Bulletin;
import com.lemania.sis.server.bean.bulletinsubject.BulletinSubject;
import com.lemania.sis.server.bean.classroom.Classroom;
import com.lemania.sis.server.bean.period.Period;
import com.lemania.sis.server.bean.professor.Professor;
import com.lemania.sis.server.bean.profilesubject.ProfileSubject;
import com.lemania.sis.server.service.MyDAOBase;

public class MasterAgendaItemDao extends MyDAOBase {
	//
	public void initialize(){
		return;
	}
	
	/*
	 * */
	public List<MasterAgendaItem> listAll(){
		//
		Query<MasterAgendaItem> q = ofy().load().type(MasterAgendaItem.class);
		List<MasterAgendaItem> returnList = q.list();
		
		return returnList;
	}
	
	
	
	/*
	 * */
	public List<MasterAgendaItem> listAllByProfile(String profileId){
		//
		Query<MasterAgendaItem> q = ofy().load().type(MasterAgendaItem.class).filter("profile", Key.create(Profile.class, Long.parseLong(profileId)));
		List<MasterAgendaItem> returnList = q.list();
		for ( MasterAgendaItem mai : returnList ) {
			mai.setPeriodDescription( (ofy().load().key( mai.getPeriod() ).now()).getDescription() );
			mai.setSubjectName( (ofy().load().key( mai.getSubject() ).now()).getSubjectName() );
			mai.setProfName( (ofy().load().key( mai.getProf() ).now()).getProfName() );
			mai.setClassroomName( (ofy().load().key( mai.getClassroom() ).now()).getRoomName() );
			mai.setPeriodId( Long.toString( mai.getPeriod().getId() ));
		}
		Collections.sort( returnList );
		return returnList;
	}
	
	
	
	/*
	 * */
	public List<MasterAgendaItem> listAllByProf(String profId){
		//
		Query<MasterAgendaItem> q = ofy().load().type(MasterAgendaItem.class).filter("prof", Key.create(Professor.class, Long.parseLong(profId)));
		List<MasterAgendaItem> returnList = q.list();
		for ( MasterAgendaItem mai : returnList ) {
			mai.setSubjectName( (ofy().load().key( mai.getSubject() ).now()).getSubjectName() );
			mai.setProfName( (ofy().load().key( mai.getProf() ).now()).getProfName() );
			mai.setClassroomName( (ofy().load().key( mai.getClassroom() ).now()).getRoomName() );
			mai.setPeriodId( Long.toString( mai.getPeriod().getId() ));
			mai.setPeriodDescription( (ofy().load().key( mai.getPeriod() ).now()).getDescription() );
		}
		Collections.sort( returnList );
		return returnList;
	}
	
	
	
	/*
	 * */
	public List<MasterAgendaItem> listAllByClassProfileStudent(String classId, String profileId, String bulletinId){
		// Get the current bulletin of this student
		//
		Bulletin qb = ofy().load().type(Bulletin.class).id( Long.parseLong(bulletinId) ).now();
		if (qb == null)
			return null;
		
		// Get the list of subjects this student study
		//
		Query<BulletinSubject> qbs = ofy().load().type(BulletinSubject.class).filter("bulletin", Key.create(Bulletin.class, qb.getId() ));
		// Go through and make the list of subject keys
		List<Key<Subject>> subjects = new ArrayList<Key<Subject>>();
		List<BulletinSubject> subjectGroup = new ArrayList<BulletinSubject>();
		Key<Subject> prevKey = null;
		for (BulletinSubject bs : qbs ) {
			if ( prevKey != bs.getSubject() ) {
				subjects.add( bs.getSubject() );
				subjectGroup.add( bs );
				prevKey = bs.getSubject();
			} else continue;
		}
		
		// Get the list of master agenda item base on the subjects
		//
		Query<MasterAgendaItem> q = ofy().load().type(MasterAgendaItem.class).filter("subject in", subjects );
		List<MasterAgendaItem> roughList = q.list();
		List<MasterAgendaItem> returnList = new ArrayList<MasterAgendaItem>();
		// Go through the list and select only those with the same professor
		//
		for ( MasterAgendaItem mai : roughList ) {
			for (int i=0; i<subjectGroup.size(); i++) {
				if ( mai.getSubject().equals(subjectGroup.get(i).getSubject())
						&& mai.getProf().equals(subjectGroup.get(i).getProfessor()) ) {
					mai.setPeriodDescription( (ofy().load().key( mai.getPeriod() ).now()).getDescription() );
					mai.setSubjectName( (ofy().load().key( mai.getSubject() ).now()).getSubjectName() );
					mai.setProfName( (ofy().load().key( mai.getProf() ).now()).getProfName() );
					mai.setClassroomName( (ofy().load().key( mai.getClassroom() ).now()).getRoomName() );
					mai.setPeriodId( Long.toString( mai.getPeriod().getId() ));
					returnList.add(mai);
				}
			}
		}
		Collections.sort( returnList );
		return returnList;
	}
	
	

	/*
	 * */
	public void save(MasterAgendaItem mai){
		ofy().save().entities( mai );
	}
	
	/*
	 * */
	public MasterAgendaItem saveAndReturn(MasterAgendaItem mai){
		Key<MasterAgendaItem> key = ofy().save().entities( mai ).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * */
	public MasterAgendaItem addMasterAgendaItem( String jourCode, String periodId, String profileId, 
			String profileSubjectId, String classroomId, int duration ) {
		//
		MasterAgendaItem mai = new MasterAgendaItem();
		mai.setJourCode(jourCode);
		mai.setPeriod( Key.create(Period.class, Long.parseLong(periodId)));
		mai.setProfile( Key.create(Profile.class, Long.parseLong(profileId)));
		
		ProfileSubject ps = ofy().load().key( Key.create(ProfileSubject.class, Long.parseLong(profileSubjectId))).now();
		mai.setSubject( ps.getSubject() );
		mai.setProf( ps.getProfessor() );
		
		mai.setClassroom( Key.create(Classroom.class, Long.parseLong(classroomId)));
		mai.setDuration(duration);
		
		Key<MasterAgendaItem> key = ofy().save().entities( mai ).now().keySet().iterator().next();
		try {
			MasterAgendaItem returnMai = ofy().load().key(key).now();
			returnMai.setPeriodDescription( (ofy().load().key( Key.create(Period.class, Long.parseLong(periodId))).now()).getDescription() );
			returnMai.setSubjectName( (ofy().load().key( ps.getSubject() ).now()).getSubjectName() );
			returnMai.setProfName( (ofy().load().key( ps.getProfessor() ).now()).getProfName() );
			returnMai.setClassroomName( (ofy().load().key( Key.create(Classroom.class, Long.parseLong(classroomId))).now()).getRoomName() );
			returnMai.setPeriodId(periodId);
			return returnMai;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * */
	public void removeMasterAgendaItem(MasterAgendaItem mai){
		ofy().delete().entities(mai);
	}

}
