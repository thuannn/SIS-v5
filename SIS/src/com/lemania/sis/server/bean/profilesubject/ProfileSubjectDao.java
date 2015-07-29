package com.lemania.sis.server.bean.profilesubject;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.Profile;
import com.lemania.sis.server.ProfileBranche;
import com.lemania.sis.server.Subject;
import com.lemania.sis.server.bean.classe.Classe;
import com.lemania.sis.server.bean.professor.Professor;
import com.lemania.sis.server.service.MyDAOBase;

public class ProfileSubjectDao extends MyDAOBase {
	//
	public void initialize(){
		return;
	}
	
	
	/**/
	public List<ProfileSubject> listAll(){
		Query<ProfileSubject> q = ofy().load().type(ProfileSubject.class).order("subjectName");
		List<ProfileSubject> returnList = new ArrayList<ProfileSubject>();
		for (ProfileSubject profileSubject : q){
			//
			if (profileSubject.getProfessor() != null)
				profileSubject.setProfName( ofy().load().key(profileSubject.getProfessor()).now().getProfName() );
			if (profileSubject.getProfessor1() != null)
				profileSubject.setProf1Name( ofy().load().key(profileSubject.getProfessor1()).now().getProfName() );
			if (profileSubject.getProfessor2() != null)
				profileSubject.setProf2Name( ofy().load().key(profileSubject.getProfessor2()).now().getProfName() );
			//
			profileSubject.setSubjectName( ofy().load().key( profileSubject.getSubject()).now().getSubjectName() );
			//
			returnList.add(profileSubject);
		}
		return returnList;
	}
	
	
	/**/
	public List<ProfileSubject> listAllActive(){
		Query<ProfileSubject> q = ofy().load().type(ProfileSubject.class)
				.filter("isActive", true)
				.order("subjectName");
		List<ProfileSubject> returnList = new ArrayList<ProfileSubject>();
		for ( ProfileSubject profileSubject : q ){
			//
			if (profileSubject.getProfessor() != null)
				profileSubject.setProfName( ofy().load().key(profileSubject.getProfessor()).now().getProfName() );
			if (profileSubject.getProfessor1() != null)
				profileSubject.setProf1Name( ofy().load().key(profileSubject.getProfessor1()).now().getProfName() );
			if (profileSubject.getProfessor2() != null)
				profileSubject.setProf2Name( ofy().load().key(profileSubject.getProfessor2()).now().getProfName() );
			//
			profileSubject.setSubjectName( ofy().load().key( profileSubject.getSubject()).now().getSubjectName() );
			returnList.add( profileSubject );
		}
		return returnList;
	}
	
	
	/**/
	public List<ProfileSubject> listAll( String profileId ){
		Query<ProfileSubject> q = ofy().load().type(ProfileSubject.class)
				.filter("profile", Key.create(Profile.class, Long.parseLong( profileId )))
				.order("subjectName");
		List<ProfileSubject> returnList = new ArrayList<ProfileSubject>();
		for ( ProfileSubject profileSubject : q ){
			//
			if (profileSubject.getProfessor() != null)
				profileSubject.setProfName( ofy().load().key(profileSubject.getProfessor()).now().getProfName() );
			if (profileSubject.getProfessor1() != null)
				profileSubject.setProf1Name( ofy().load().key(profileSubject.getProfessor1()).now().getProfName() );
			if (profileSubject.getProfessor2() != null)
				profileSubject.setProf2Name( ofy().load().key(profileSubject.getProfessor2()).now().getProfName() );
			//
			profileSubject.setSubjectName( ofy().load().key( profileSubject.getSubject()).now().getSubjectName() );
			returnList.add( profileSubject );
		}
		return returnList;
	}
	
	
	/*
	 * */
	public void save(ProfileSubject profileSubject){
		ofy().save().entities( profileSubject );
	}
	
	
	/*
	 * */
	public ProfileSubject saveAndReturn(ProfileSubject profile){
		Key<ProfileSubject> key = ofy().save().entities(profile).now().keySet().iterator().next();
		try {
			ProfileSubject ps = ofy().load().key(key).now();
			//
			if (ps.getProfessor() != null)
				ps.setProfName( ofy().load().key(ps.getProfessor()).now().getProfName() );
			if (ps.getProfessor1() != null)
				ps.setProf1Name( ofy().load().key(ps.getProfessor1()).now().getProfName() );
			if (ps.getProfessor2() != null)
				ps.setProf2Name( ofy().load().key(ps.getProfessor2()).now().getProfName() );
			//
			ps.setSubjectName( ofy().load().key( ps.getSubject()).now().getSubjectName() );
			return ps;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public ProfileSubject updateSubjectProf( ProfileSubject profile, String profId, String profId1, String profId2 ){
		//
		profile.setProfessor( Key.create( Professor.class, Long.parseLong(profId)) );
		//
		if (!profId1.equals(""))
			profile.setProfessor1( Key.create( Professor.class, Long.parseLong(profId1)) );
		else
			profile.setProfessor1(null);
		//
		if (!profId2.equals(""))
			profile.setProfessor2( Key.create( Professor.class, Long.parseLong(profId2)) );
		else
			profile.setProfessor2(null);
		//
		Key<ProfileSubject> key = ofy().save().entities(profile).now().keySet().iterator().next();
		try {
			ProfileSubject ps = ofy().load().key(key).now();
			//
			if (ps.getProfessor() != null)
				ps.setProfName( ofy().load().key(ps.getProfessor()).now().getProfName() );
			if (ps.getProfessor1() != null)
				ps.setProf1Name( ofy().load().key(ps.getProfessor1()).now().getProfName() );
			if (ps.getProfessor2() != null)
				ps.setProf2Name( ofy().load().key(ps.getProfessor2()).now().getProfName() );
			//
			ps.setSubjectName( ofy().load().key( ps.getSubject()).now().getSubjectName() );
			return ps;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	/**/
	public ProfileSubject saveAndReturn(String profileId, String subjectId, 
			String professorId, String professorId1, String professorId2, String subjectCoef ){
		//
		ProfileSubject ps = new ProfileSubject();
		ps.setProfile( Key.create( Profile.class, Long.parseLong(profileId)));
		ps.setSubject( Key.create( Subject.class, Long.parseLong(subjectId)));
		//
		ps.setProfessor( Key.create(Professor.class, Long.parseLong(professorId)));
		ps.setProfName( ofy().load().key(ps.getProfessor()).now().getProfName() );
		// 
		// if there aren't any other prof, set null
		if (professorId1.equals(""))
			ps.setProfessor1(null);
		else {
			ps.setProfessor1( Key.create(Professor.class, Long.parseLong(professorId1)) );
			ps.setProf1Name( ofy().load().key(ps.getProfessor1()).now().getProfName() );
		}
		//
		if (professorId2.equals(""))
			ps.setProfessor2(null);
		else {
			ps.setProfessor2( Key.create(Professor.class, Long.parseLong(professorId2)));
			ps.setProf2Name( ofy().load().key(ps.getProfessor2()).now().getProfName() );
		}
		//
		ps.setSubjectName( ofy().load().key( ps.getSubject()).now().getSubjectName() );
		ps.setSubjectCoef( Double.parseDouble(subjectCoef));
		//
		Key<ProfileSubject> key = ofy().save().entities( ps ).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**/
	public Boolean removeProfileSubject(ProfileSubject profileSubject) {
		//
		Query<ProfileBranche> q = ofy().load().type(ProfileBranche.class)
				.filter("profileSubject", Key.create(ProfileSubject.class, profileSubject.getId()));
		if (q.count() > 0)
			return false;
		else {
			ofy().delete().entities(profileSubject);
			return true;
		}
	}
	
	
	/*
	 * */
	public ProfileSubject calculateTotalBrancheCoef(String profileSubjectId) {
		//
		ProfileSubject ps = ofy().load().key( Key.create(ProfileSubject.class, Long.parseLong(profileSubjectId)) ).now();
		Query<ProfileBranche> q = ofy().load().type(ProfileBranche.class)
				.filter("profileSubject", Key.create( ProfileSubject.class, Long.parseLong(profileSubjectId)) )
				.order("profileBranche");
		ps.setTotalBrancheCoef(0.0);
		for ( ProfileBranche profileBranche : q ){
			ps.setTotalBrancheCoef( ps.getTotalBrancheCoef() + profileBranche.getBrancheCoef() );
		}
		ofy().save().entities( ps );
		return ps;
	}
	
	
	/*
	 * */
	public List<Professor> listProfessorsByProfileSubject( String subjectId, String classId ) {
		//
		Query<Profile> qp = ofy().load().type(Profile.class)
				.filter("classe", Key.create(Classe.class, Long.parseLong(classId)));
		Key<Profile> keyP = qp.keys().iterator().next();
		
		Query<ProfileSubject> q = ofy().load().type(ProfileSubject.class)
				.filter("subject", Key.create(Subject.class, Long.parseLong(subjectId)))
				.filter("profile", keyP )
				.order("profName");
		//
		List<Professor> returnList = new ArrayList<Professor>();
		for ( ProfileSubject profileSubject : q ){
			//
			if (profileSubject.getProfessor() != null)
				returnList.add( ofy().load().key( profileSubject.getProfessor() ).now() );
			if (profileSubject.getProfessor1() != null)
				returnList.add( ofy().load().key( profileSubject.getProfessor1() ).now() );
			if (profileSubject.getProfessor2() != null)
				returnList.add( ofy().load().key( profileSubject.getProfessor2() ).now() );
		}
		return returnList;
	}
}
