package com.lemania.sis.server.bean.bulletin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.lemania.sis.client.values.DataValues;
import com.lemania.sis.server.bean.parent.Parent;
import com.lemania.sis.server.bean.professor.Professor;
import com.lemania.sis.server.bean.profilesubject.ProfileSubject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.Profile;
import com.lemania.sis.server.ProfileBranche;
import com.lemania.sis.server.Subject;
import com.lemania.sis.server.bean.bulletinbranche.BulletinBranche;
import com.lemania.sis.server.bean.bulletinsubject.BulletinSubject;
import com.lemania.sis.server.bean.classe.Classe;
import com.lemania.sis.server.bean.student.Student;
import com.lemania.sis.server.bean.user.User;
import com.lemania.sis.server.service.MyDAOBase;


/*
 * 
 * */
class BulletinSortByClassStudent implements Comparator<Bulletin> {

	@Override
	public int compare(Bulletin o1, Bulletin o2) {
		//
		return ( o1.getClasseName() + o1.getStudentName() ).compareTo( o2.getClasseName() + o2.getStudentName() );
	}
	
}


/*
 * 
 * */
public class BulletinDao extends MyDAOBase {
	//
	public void initialize(){
		return;
	}
	
	public List<Bulletin> listAll(){
		Query<Bulletin> q = ofy().load().type(Bulletin.class)
				.order("classeName")
				.order("studentName");
		List<Bulletin> returnList = new ArrayList<Bulletin>();
		Student student;
		for (Bulletin bulletin : q) {
			student = ofy().load().key( bulletin.getStudent() ).now();
			bulletin.setStudentName( student.getFirstName() + " " + student.getLastName() );
			bulletin.setProgrammeName( ofy().load().key(
					ofy().load().key( Key.create(Classe.class, bulletin.getClasse().getId())).now()
					.getProgramme()).now()
					.getCoursNom());
			returnList.add(bulletin);
		}
		//
		populateStudentName( returnList );
		//
		Collections.sort(returnList);
		return returnList;
	}
	
	public List<Bulletin> listAllActive(){
		Query<Bulletin> q = ofy().load().type(Bulletin.class)
				.filter("isFinished", false)
				.order("classeName")
				.order("studentName");
		List<Bulletin> returnList = new ArrayList<Bulletin>();
		Student student;
		for (Bulletin bulletin : q){
			student = ofy().load().key( bulletin.getStudent() ).now();
			bulletin.setStudentName( student.getFirstName() + " " + student.getLastName() );
			bulletin.setProgrammeName( ofy().load().key(
					ofy().load().key( Key.create(Classe.class, bulletin.getClasse().getId())).now()
					.getProgramme()).now()
					.getCoursNom());
			returnList.add(bulletin);
		}
		//
		populateStudentName( returnList );
		//
		Collections.sort(returnList);
		return returnList;
	}
	
	
	/* List all bulletin by class */
	public List<Bulletin> listAllByClass(String classId){
		Query<Bulletin> q = ofy().load().type(Bulletin.class)
				.filter("classe", Key.create(Classe.class, Long.parseLong(classId)))
				.order("classeName")
				.order("studentName");
		List<Bulletin> returnList = new ArrayList<Bulletin>();
		Student student;
		for (Bulletin bulletin : q){
			student = ofy().load().key( bulletin.getStudent() ).now();
			bulletin.setStudentName( student.getFirstName() + " " + student.getLastName() );
			bulletin.setProgrammeName( ofy().load().key(
					ofy().load().key( Key.create(Classe.class, bulletin.getClasse().getId())).now()
					.getProgramme()).now()
					.getCoursNom());
			returnList.add(bulletin);
		}
		//
		populateStudentName( returnList );
		//
		Collections.sort(returnList);
		return returnList;
	}
	
	
	/* List all bulletin by class */
	public List<Bulletin> listAllActiveByClass(String classId){
		Query<Bulletin> q = ofy().load().type(Bulletin.class)
				.filter("classe", Key.create(Classe.class, Long.parseLong(classId)))
				.filter("isActive", true)
				.order("classeName")
				.order("studentName");
		List<Bulletin> returnList = new ArrayList<Bulletin>();
		Student student;
		for (Bulletin bulletin : q){
			if (bulletin.getIsFinished().equals(true))
				continue;
			student = ofy().load().key( bulletin.getStudent() ).now();
			bulletin.setStudentName( student.getFirstName() + " " + student.getLastName() );
			bulletin.setProgrammeName( ofy().load().key(
					ofy().load().key( Key.create(Classe.class, bulletin.getClasse().getId())).now()
					.getProgramme()).now()
					.getCoursNom());
			returnList.add( bulletin );
		}
		//
		populateStudentName( returnList );
		//
		Collections.sort(returnList);
		return returnList;
	}
	
	
	/*
	 * */
	/* List all bulletin by parent */
	public List<Bulletin> listAllByParentUserId(String userId) {
		//
		List<Bulletin> returnList = new ArrayList<Bulletin>();
		//
		// Load the Parent by user id
		User user = ofy().load().key( Key.create(User.class, Long.parseLong(userId)) ).now();
		Parent parent = null;
		Query<Parent> parents = ofy().load().type(Parent.class)
				.filter("eMail", user.getEmail());
		if (parents.list().size() <= 0)
			return returnList;
		else
			parent = parents.list().get(0);
		//
		// Load all the children
		String[] childIds = null;
		if (!parent.getChildIds().equals("")) {
			childIds = parent.getChildIds().split(" ");
		}
		// 
		// Create list of student keys
		if (childIds == null)
			return returnList;
		List<Key<Student>> studentKeys = new ArrayList<Key<Student>>();
		for (int i=0; i<childIds.length; i++) {
			studentKeys.add( Key.create(Student.class, Long.parseLong(childIds[i].trim())));
		}
		// Load the bulletins of each child
		//
		Query<Bulletin> q = ofy().load().type(Bulletin.class)
				.filter( "student IN", studentKeys )
				.filter( "isActive", true)
				.order( "classeName" )
				.order( "studentName" );
		Student student;
		for (Bulletin bulletin : q){
			//
			if (bulletin.getIsFinished().equals(true))
				continue;
			student = ofy().load().key( bulletin.getStudent() ).now();
			bulletin.setStudentName( student.getFirstName() + " " + student.getLastName() );
			bulletin.setProgrammeName( ofy().load().key(
					ofy().load().key( Key.create(Classe.class, bulletin.getClasse().getId())).now()
					.getProgramme()).now()
					.getCoursNom());
			bulletin.setRemarqueDirection( "" );
			returnList.add( bulletin );
		}
		//
		populateStudentName( returnList );
		//
		Collections.sort(returnList);
		return returnList;
	}
	
	
	/*
	 * List all student by professor, subject and class
	 * */
	public List<Bulletin> listAllStudentByProfSubjectClass( String profId, String subjectId, String classId ) {
		//
		Bulletin bulletin = null;
		List<Bulletin> returnList = new ArrayList<Bulletin>();
		List<BulletinSubject> listBS = new ArrayList<BulletinSubject>();
		//
		// Load the list of BulletinSubject using professor and subject
		Query<BulletinSubject> bss = null, bss1 = null, bss2 = null;
		
		bss = ofy().load().type( BulletinSubject.class )
				.filter("subject", Key.create( Subject.class, Long.parseLong(subjectId)) )
				.filter("professor", Key.create( Professor.class, Long.parseLong(profId)) )
				.filter("isActive", true)
				.order("bulletin");
		bss1 = ofy().load().type( BulletinSubject.class )
				.filter("subject", Key.create( Subject.class, Long.parseLong(subjectId)) )
				.filter("professor1", Key.create( Professor.class, Long.parseLong(profId)) )
				.filter("isActive", true)
				.order("bulletin");
		bss2 = ofy().load().type( BulletinSubject.class )
				.filter("subject", Key.create( Subject.class, Long.parseLong(subjectId)) )
				.filter("professor2", Key.create( Professor.class, Long.parseLong(profId)) )
				.filter("isActive", true)
				.order("bulletin");
		
		listBS.addAll( bss.list() );
		listBS.addAll( bss1.list() );
		listBS.addAll( bss2.list() );
		//
		// Remove duplication
		Key<Bulletin> keyBulletin = null;
		Student student = null;
		for ( BulletinSubject bs : listBS ) {
			if ( keyBulletin != bs.getBulletin() ) {
				bulletin = ofy().load().key( bs.getBulletin()).now();
				if ( classId.equals( DataValues.optionAll ) || Long.toString(bulletin.getClasse().getId()).equals(classId) ) {
					student = ofy().load().key(bulletin.getStudent()).now(); 
					if ( student.getIsActive()  &&  !bulletin.getIsFinished() )
						returnList.add( bulletin );
				}
			}
			keyBulletin = bs.getBulletin();
		}
		//
		populateStudentName( returnList );
		//
		Collections.sort( returnList, new BulletinSortByClassStudent() );
		//
		return returnList;
	}
	
	
	
	/* List all bulletin by class */
	public List<Bulletin> listAllActiveByClassProfile( String classId, String profileId ){
		Query<Bulletin> q = ofy().load().type(Bulletin.class)
				.filter("profile", Key.create( Profile.class, Long.parseLong(profileId)))
				.filter("classe", Key.create(Classe.class, Long.parseLong(classId)))
				.filter("isActive", true)
				.order("classeName")
				.order("studentName");
		List<Bulletin> returnList = new ArrayList<Bulletin>();
		Student student;
		for (Bulletin bulletin : q){
			if (bulletin.getIsFinished().equals(true))
				continue;
			student = ofy().load().key( bulletin.getStudent() ).now();
			bulletin.setStudentName( student.getFirstName() + " " + student.getLastName() );
			bulletin.setProgrammeName( ofy().load().key(
					ofy().load().key( Key.create(Classe.class, bulletin.getClasse().getId())).now()
					.getProgramme()).now()
					.getCoursNom());
			returnList.add( bulletin );
		}
		//
		populateStudentName( returnList );
		//
		Collections.sort(returnList);
		return returnList;
	}
	
	
	
	/* List all bulletin by student's email address */
	public List<Bulletin> listAllByEmail(String email){
		Student student = null;
		List<Bulletin> returnList = new ArrayList<Bulletin>();
		//
		Query<Student> qStudent = ofy().load().type(Student.class).filter("Email", email);
		if (qStudent.count()>0) 
			student = qStudent.list().get(0);
		else
			return returnList;
		//
		Query<Bulletin> q = ofy().load().type(Bulletin.class)
				.filter("student", Key.create(Student.class, student.getId()))
				.order("classeName")
				.order("year");
		for (Bulletin bulletin : q){
			//
			bulletin.setStudentName( student.getFirstName() + " " + student.getLastName() );
			bulletin.setProgrammeName( ofy().load().key(
					ofy().load().key( Key.create(Classe.class, bulletin.getClasse().getId())).now()
					.getProgramme()).now()
					.getCoursNom());
			returnList.add(bulletin);
		}
		//
		populateStudentName( returnList );
		//
		return returnList;
	}
	
	
	/* List all bulletin by student's email address */
	public List<Bulletin> listAllByEmailForPublic(String email){
		Student student = null;
		List<Bulletin> returnList = new ArrayList<Bulletin>();
		//
		Query<Student> qStudent = ofy().load().type(Student.class).filter("Email", email);
		if (qStudent.count()>0) 
			student = qStudent.list().get(0);
		else
			return returnList;
		//
		Query<Bulletin> q = ofy().load().type(Bulletin.class)
				.filter("student", Key.create(Student.class, student.getId()))
				.order("classeName")
				.order("year");
		for (Bulletin bulletin : q){
			//
			bulletin.setStudentName( student.getFirstName() + " " + student.getLastName() );
			bulletin.setProgrammeName( ofy().load().key(
					ofy().load().key( Key.create(Classe.class, bulletin.getClasse().getId())).now()
					.getProgramme()).now()
					.getCoursNom());
			bulletin.setRemarqueDirection( "" );
			returnList.add(bulletin);
		}
		//
		populateStudentName( returnList );
		//
		return returnList;
	}
	
	
	public Bulletin getBulletin(String bulletinId){
		Bulletin bulletin = ofy().load().key( Key.create(Bulletin.class, Long.parseLong(bulletinId))).now();
		bulletin.setProgrammeName( ofy().load().key(
						ofy().load().key( Key.create(Classe.class, bulletin.getClasse().getId())).now()
						.getProgramme()).now()
						.getCoursNom());
		return bulletin;
	}
	
	
	public void save(Bulletin bulletin) {
		//
		ofy().save().entities(bulletin).now();
	}
	
	public Bulletin saveAndReturn(Bulletin bulletin){
		Key<Bulletin> key = ofy().save().entities(bulletin).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public Bulletin createBulletin(String studentId, String classId, String year, String profileId){
		//
		Bulletin bulletin = new Bulletin();
		BulletinSubject curBulletinSubject;
		BulletinBranche curBulletinBranche;
		//
		List<Key<BulletinSubject>> keyListSubject = new ArrayList<Key<BulletinSubject>>();
		List<Key<BulletinBranche>> keyListBranche = new ArrayList<Key<BulletinBranche>>();
		Key<Bulletin> keyBulletin = null;
		//
		Student student = ofy().load().key(Key.create(Student.class, Long.parseLong(studentId))).now();
		Classe classe = ofy().load().key(Key.create(Classe.class, Long.parseLong(classId))).now();
		//
		bulletin.setYear(year);
		bulletin.setClasse(Key.create(Classe.class, classe.getId()));
		bulletin.setStudent(Key.create(Student.class, student.getId()));
		bulletin.setClasseName(classe.getClassName());
		bulletin.setStudentName( student.getFirstName() + " " + student.getLastName() );
		bulletin.setProfile( Key.create(Profile.class, Long.parseLong(profileId)));
		//
		try {
			//
			keyBulletin = ofy().save().entities(bulletin).now().keySet().iterator().next();
			//
			Query<ProfileSubject> profileSubjects = ofy().load().type(ProfileSubject.class)
					.filter("profile", Key.create(Profile.class, Long.parseLong(profileId)));
			
			for (ProfileSubject profileSubject : profileSubjects){
				//				
				curBulletinSubject = new BulletinSubject();
				curBulletinSubject.setSubject( profileSubject.getSubject() );
				curBulletinSubject.setSubjectCoef( profileSubject.getSubjectCoef() );
				curBulletinSubject.setBulletin( Key.create(Bulletin.class, bulletin.getId()) );
				//
				curBulletinSubject.setProfessor( profileSubject.getProfessor() );
				curBulletinSubject.setProfessor1( profileSubject.getProfessor1() );
				curBulletinSubject.setProfessor2( profileSubject.getProfessor2() );
				curBulletinSubject.setProfName( profileSubject.getProfName() );
				curBulletinSubject.setProf1Name( profileSubject.getProf1Name() );
				curBulletinSubject.setProf2Name( profileSubject.getProf2Name() );
				//
				curBulletinSubject.setSubjectName( profileSubject.getSubjectName() );
				curBulletinSubject.setSubjectCoef( profileSubject.getSubjectCoef() );
				keyListSubject.add( ofy().save().entities(curBulletinSubject).now().keySet().iterator().next() );
				//
				Query<ProfileBranche> profileBranches = ofy().load().type(ProfileBranche.class)
						.filter("profileSubject", Key.create(ProfileSubject.class, profileSubject.getId()));
				
				for (ProfileBranche profileBranche : profileBranches) {
					curBulletinBranche = new BulletinBranche();
					curBulletinBranche.setBulletinBranche( profileBranche.getProfileBranche() );
					curBulletinBranche.setBrancheCoef( profileBranche.getBrancheCoef() );
					curBulletinBranche.setBulletinBrancheName( profileBranche.getProfileBrancheName() );
					curBulletinBranche.setBulletinSubject( Key.create(BulletinSubject.class, curBulletinSubject.getId()));
					keyListBranche.add( ofy().save().entities(curBulletinBranche).now().keySet().iterator().next() );
				}
			}
			//
			return bulletin;
			//
		} catch (Exception e) {
			//
			if (keyBulletin != null)
				ofy().delete().entities(keyBulletin);
			if (keyListSubject.size() >0)
				ofy().delete().entities(keyListSubject);
			if (keyListBranche.size()>0)
				ofy().delete().entities(keyListBranche);
			
			throw new RuntimeException(e);
		} finally {
			//
		}
	}
	
	
	/*
	 * */
	public Boolean removeBulletin(Bulletin bulletin){
		//
		Key<Bulletin> keyBulletin = Key.create(Bulletin.class, bulletin.getId());
		//
		Query<BulletinSubject> bulletinSubjects = ofy().load().type(BulletinSubject.class)
				.filter("bulletin", keyBulletin);
		
		for (BulletinSubject bulletinSubject : bulletinSubjects){				
			//
			Query<BulletinBranche> bulletinBranches = ofy().load().type(BulletinBranche.class)
					.filter("bulletinSubject", Key.create(BulletinSubject.class, bulletinSubject.getId()));
			
			for (BulletinBranche bulletinBranche : bulletinBranches) {
				ofy().delete().entities( bulletinBranche );
			}
			
			ofy().delete().entities( bulletinSubject );
		}			
		//
		ofy().delete().entities(bulletin).now();
		return true;
		//		
	}
	
	
	/*
	 * */
	public void updateBulletinStatus( String studentId, Boolean status ){
		//
		Query<Bulletin> q = ofy().load().type(Bulletin.class)
				.filter("student", Key.create(Student.class, Long.parseLong( studentId )));
		for (Bulletin bulletin : q){
			bulletin.setIsActive( status );
			ofy().save().entities( bulletin ).now();
		}
	}
	
	
	/*
	 * */
	public Bulletin saveBulletinRemarqueDirection( String bulletinId, String remarqueDirection ) {
		//
		Bulletin bulletin = ofy().load().key( Key.create(Bulletin.class, Long.parseLong(bulletinId))).now();
		bulletin.setRemarqueDirection(remarqueDirection);
		Key<Bulletin> keyB = ofy().save().entities(bulletin).now().keySet().iterator().next();
		return ofy().load().key( keyB ).now();
	}
	
	
	/*
	 * */
	public void populateStudentName( List<Bulletin> bulletins ) {
		//
		Student st = new Student();
		for ( Bulletin b : bulletins ) {
			st = ofy().load().key( b.getStudent() ).now();
			b.setStudentName( st.getLastName() + " " + st.getFirstName());
		}
	}
}
