package com.lemania.sis.server.service;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.Profile;
import com.lemania.sis.server.Subject;
import com.lemania.sis.server.bean.bulletin.Bulletin;
import com.lemania.sis.server.bean.profilesubject.ProfileSubject;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.loader.FilterConfigBean;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

public class SubjectDao extends MyDAOBase {

	public void initialize() {
		return;
	}

	/*
	 * 
	 * */
	public List<Subject> listAll() {
		Query<Subject> q = ofy().load().type(Subject.class)
				.order("subjectName");
		List<Subject> returnList = new ArrayList<Subject>();
		for (Subject subject : q) {
			returnList.add(subject);
		}
		return returnList;
	}

	/*
	 * 
	 * */
	public List<Subject> listAllActive() {
		Query<Subject> q = ofy().load().type(Subject.class)
				.order("subjectName");
		List<Subject> returnList = new ArrayList<Subject>();
		for (Subject subject : q) {
			if (subject.getIsActive())
				returnList.add(subject);
		}
		return returnList;
	}

	/*
	 * 
	 * */
	public List<Subject> listAllActiveByProfile(String profileId) {
		//
		Query<ProfileSubject> profileSubjects = ofy()
				.load()
				.type(ProfileSubject.class)
				.filter("profile",
						Key.create(Profile.class, Long.parseLong(profileId)))
				.order("subjectName");
		//
		List<Subject> returnList = new ArrayList<Subject>();
		for (ProfileSubject ps : profileSubjects) {
			returnList.add(ofy().load().key(ps.getSubject()).now());
		}
		return returnList;
	}

	/*
	 * 
	 * */
	public List<Subject> listAllActiveByProfile(Bulletin bulletin) {
		//
		Profile profile;
		try {
			profile = ofy().load().key(bulletin.getProfile()).now();
		} catch (RuntimeException exception) {
			Query<Profile> profiles = ofy().load().type(Profile.class)
					.filter("classe", bulletin.getClasse());
			profile = profiles.list().get(0);
		}
		//
		Query<ProfileSubject> profileSubjects = ofy().load()
				.type(ProfileSubject.class).filter("profile", profile)
				.order("subjectName");
		//
		List<Subject> returnList = new ArrayList<Subject>();
		Subject subject;
		Subject prevSubject = null;
		for (ProfileSubject ps : profileSubjects) {
			//
			subject = ofy().load().key(ps.getSubject()).now();
			if (prevSubject == null)
				prevSubject = subject;
			else {
				if (subject.getSubjectName().equals(
						prevSubject.getSubjectName()))
					continue;
			}
			returnList.add(subject);
			prevSubject = subject;
		}
		return returnList;
	}

	/*
	 * 
	 * */
	public void save(Subject subject) {
		ofy().save().entities(subject);
	}

	/*
	 * 
	 * */
	public Subject saveAndReturn(Subject subject) {
		Key<Subject> key = ofy().save().entities(subject).now().keySet()
				.iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 
	 * */
	public void removeSubject(Subject subject) {
		ofy().delete().entities(subject);
	}
	

	/*
	 * GXT Grid
	 * ----------------------
	 * */
	@SuppressWarnings("serial")
	public static class SubjectPagingLoadResultBean extends PagingLoadResultBean<Subject> {
		protected SubjectPagingLoadResultBean() {

		}

		public SubjectPagingLoadResultBean(List<Subject> list, int totalLength, int offset) {
			super(list, totalLength, offset);
		}
	}

	//
	public SubjectPagingLoadResultBean listAll(int offset, int limit, List<SortInfoBean> sortInfo, 
		      List<FilterConfigBean> filterConfig) {
		//
		List<Subject> subjects = new ArrayList<Subject>();
		Query<Subject> q = ofy().load().type(Subject.class)
				.order("subjectName");
		Subject subject;
		for (int i=0; i< limit; i++ ) {
			if ( (i + offset) >= q.count() )
				break;
			subject = q.list().get( i + offset );
			subjects.add(subject);
		}
		SubjectPagingLoadResultBean resultBean = new SubjectPagingLoadResultBean( subjects, q.count(), offset);
		return resultBean;
	}
	
	/* ------------------------
	 * */
}
