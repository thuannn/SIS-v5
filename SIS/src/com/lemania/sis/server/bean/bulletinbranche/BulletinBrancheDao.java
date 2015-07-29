package com.lemania.sis.server.bean.bulletinbranche;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.Branche;
import com.lemania.sis.server.Profile;
import com.lemania.sis.server.ProfileBranche;
import com.lemania.sis.server.bean.bulletin.Bulletin;
import com.lemania.sis.server.bean.bulletinsubject.BulletinSubject;
import com.lemania.sis.server.bean.profilesubject.ProfileSubject;
import com.lemania.sis.server.service.MyDAOBase;

public class BulletinBrancheDao extends MyDAOBase {
	/*
	 * */
	public void initialize(){
		return;
	}
	
	
	/*
	 * */
	public List<BulletinBranche> listAll(){
		//
		Query<BulletinBranche> q = ofy().load().type(BulletinBranche.class).order("bulletinBrancheName");
		List<BulletinBranche> returnList = new ArrayList<BulletinBranche>();
		for (BulletinBranche bulletinBranche : q){
			bulletinBranche.setBulletinBrancheName( ofy().load().key( bulletinBranche.getBulletinBranche()).now().getBrancheName() );
			returnList.add(bulletinBranche);
		}
		return returnList;
	}
	
	/*
	 * */
	public void refreshBulletinBrancheNames(String oldBrancheName, String newBrancheName){
		//
		Query<BulletinBranche> q = ofy().load().type(BulletinBranche.class).filter("bulletinBrancheName", oldBrancheName);
		List<BulletinBranche> branches = new ArrayList<BulletinBranche>();
		for (BulletinBranche bulletinBranche : q) {
			bulletinBranche.setBulletinBrancheName( newBrancheName );
			branches.add(bulletinBranche);
		}
		ofy().save().entities(branches);
	}
	
	/*
	 * */
	public List<BulletinBranche> listAllActive() {
		//
		Query<BulletinBranche> q = ofy().load().type(BulletinBranche.class)
				.filter("isActive", true);
		List<BulletinBranche> returnList = new ArrayList<BulletinBranche>();
		for ( BulletinBranche bulletinBranche : q ){
			bulletinBranche.setBulletinBrancheName( ofy().load().key( bulletinBranche.getBulletinBranche()).now().getBrancheName() );
			returnList.add( bulletinBranche );
		}
		Collections.sort(returnList);
		return returnList;
	}
	
	
	/*
	 * */
	public List<BulletinBranche> listAll( String bulletinSubjectId ){
		//
		Query<BulletinBranche> q = ofy().load().type(BulletinBranche.class)
				.filter("bulletinSubject", Key.create(BulletinSubject.class, Long.parseLong(bulletinSubjectId)));
		List<BulletinBranche> returnList = new ArrayList<BulletinBranche>();
		for ( BulletinBranche bulletinBranche : q ){
			bulletinBranche.setBulletinBrancheName( ofy().load().key( bulletinBranche.getBulletinBranche()).now().getBrancheName() );
			returnList.add( bulletinBranche );
		}
		Collections.sort(returnList);
		return returnList;
	}
	
	
	/*
	 * */
	public List<BulletinBranche> listAllByBulletin( String bulletinId ) {
		//
		List<BulletinBranche> returnList = new ArrayList<BulletinBranche>();
		Query<BulletinBranche> q;
		//
		Query<BulletinSubject> qSubject = ofy().load().type(BulletinSubject.class)
				.filter("bulletin", Key.create(Bulletin.class, Long.parseLong(bulletinId)));
		for (BulletinSubject subject : qSubject ){
			//
			q = ofy().load().type(BulletinBranche.class)
					.filter("bulletinSubject", Key.create(BulletinSubject.class, subject.getId()))
					.order("bulletinBrancheName");
			for ( BulletinBranche bulletinBranche : q ){
				bulletinBranche.setBulletinSubjectId( subject.getId() );
				bulletinBranche.setBulletinBrancheName( ofy().load().key( bulletinBranche.getBulletinBranche()).now().getBrancheName() );
				returnList.add( bulletinBranche );
			}
		}
		Collections.sort(returnList);
		return returnList;
	}
	
	
	/*
	 * 
	 * */
	public void save(BulletinBranche bulletinBranche){
		ofy().save().entities( bulletinBranche );
	}
	
	
	/*
	 * 
	 * */
	public BulletinBranche saveAndReturn(BulletinBranche branche){
		//
		Key<BulletinBranche> key = ofy().save().entities(branche).now().keySet().iterator().next();
		try {
			BulletinBranche ps = ofy().load().key(key).now();
			ps.setBulletinBrancheName( ofy().load().key( ps.getBulletinBranche()).now().getBrancheName() );
			return ps;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * 
	 * */
	public BulletinBranche saveAndReturn(String bulletinSubjectId, String brancheId, String brancheCoef ){
		//
		BulletinBranche ps = new BulletinBranche();
		ps.setBulletinSubject( Key.create( BulletinSubject.class, Long.parseLong(bulletinSubjectId)) );
		ps.setBulletinBranche(Key.create( Branche.class, Long.parseLong(brancheId)) );
		ps.setBulletinBrancheName( ofy().load().key( ps.getBulletinBranche()).now().getBrancheName() );
		ps.setBrancheCoef( Double.parseDouble(brancheCoef) );
		
		Key<BulletinBranche> key = ofy().save().entities( ps ).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * 
	 * */
	public void removeBulletinBranche(BulletinBranche bulletinBranche){
		//
		ofy().delete().entities( bulletinBranche );
	}
	
	
	/*
	 *
	 * */
	public List<BulletinBranche> addRelatedBranches( BulletinSubject bulletinSubject ) {
		//
		BulletinBranche curBulletinBranche;
		List<BulletinBranche> returnList = new ArrayList<BulletinBranche>();
		//
		Profile profile;
		Bulletin bulletin = ofy().load().key(bulletinSubject.getBulletin()).now();
		if ( bulletin.getProfile() != null ){
			profile = ofy().load().key( bulletin.getProfile() ).now();
		} else {
			Query<Profile> profiles = ofy().load().type(Profile.class)
					.filter("classe", bulletin.getClasse());
			profile = profiles.list().get(0);
		}
		//
		Query<ProfileSubject> profileSubjects = ofy().load().type(ProfileSubject.class)
				.filter("profile", profile)
				.filter("subject", bulletinSubject.getSubject())
				.filter("professor", bulletinSubject.getProfessor());		
		//
		Query<ProfileBranche> profileBranches = ofy().load().type(ProfileBranche.class)
				.filter("profileSubject", profileSubjects.keys().list().get(0));
		//
		Key<BulletinBranche> key = null;
		for (ProfileBranche profileBranche : profileBranches) {
			curBulletinBranche = new BulletinBranche();
			curBulletinBranche.setBulletinBranche( profileBranche.getProfileBranche() );
			curBulletinBranche.setBrancheCoef( profileBranche.getBrancheCoef() );
			curBulletinBranche.setBulletinBrancheName( profileBranche.getProfileBrancheName() );
			curBulletinBranche.setBulletinSubject( Key.create(BulletinSubject.class, bulletinSubject.getId()));
			//
			key = ofy().save().entities(curBulletinBranche).now().keySet().iterator().next();
			returnList.add( ofy().load().key(key).now() );
			//
			bulletinSubject.setTotalBrancheCoef( bulletinSubject.getTotalBrancheCoef() + profileBranche.getBrancheCoef() );
		}
		//
		ofy().save().entities( bulletinSubject );
		//
		return returnList;
	}
	
	
	/*
	 * */
	public BulletinBranche updateBranche( String bulletinBrancheId, String brancheId, String coef ) {
		//
		BulletinBranche bb = ofy().load().key( Key.create(BulletinBranche.class, Long.parseLong(bulletinBrancheId))).now();
		if (bb != null) {
			Key<Branche> keyBranche = Key.create( Branche.class, Long.parseLong(brancheId) );
			Branche branche = ofy().load().key( keyBranche ).now();
			//
			bb.setBulletinBranche( keyBranche );
			bb.setBulletinBrancheName( branche.getBrancheName() );
			bb.setBrancheCoef( Double.parseDouble(coef) );
			//
			Key<BulletinBranche> keyBulletinBranche = ofy().save().entities(bb).now().keySet().iterator().next();
			return ofy().load().key( keyBulletinBranche ).now();
		}
		return null;
	}
}
