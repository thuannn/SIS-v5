package com.lemania.sis.shared.bulletinbranche;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.bean.bulletinbranche.BulletinBranche;

@ProxyFor(value=BulletinBranche.class, locator=ObjectifyLocator.class)
public interface BulletinBrancheProxy extends EntityProxy {
	//
	Long getId();
	
	public Double getBrancheCoef();
	public void setBrancheCoef(Double brancheCoef);

	public String getBulletinBrancheName();
	public void setBulletinBrancheName(String bulletinBrancheName);

	//
	public String getT1_1();
	public void setT1_1(String t1_1);

	public String getT1_2();
	public void setT1_2(String t1_2);

	public String getT1_3();
	public void setT1_3(String t1_3);

	public String getT1_4();
	public void setT1_4(String t1_4);

	public String getT1_5();
	public void setT1_5(String t1_5);

	//
	public String getT2_1();
	public void setT2_1(String t2_1);

	public String getT2_2();
	public void setT2_2(String t2_2);

	public String getT2_3();
	public void setT2_3(String t2_3);

	public String getT2_4();
	public void setT2_4(String t2_4);

	public String getT2_5();
	public void setT2_5(String t2_5);
	
	//
	public String getT3_1();
	public void setT3_1(String t3_1);

	public String getT3_2();
	public void setT3_2(String t3_2);

	public String getT3_3();

	public void setT3_3(String t3_3);

	public String getT3_4();
	public void setT3_4(String t3_4);

	public String getT3_5();
	public void setT3_5(String t3_5);
	
	//
	public String getT4_1();
	public void setT4_1(String t4_1);
	
	public String getT4_2();
	public void setT4_2(String t4_2);
	
	public String getT4_3();
	public void setT4_3(String t4_3);
	
	public String getT4_4();
	public void setT4_4(String t4_4);
	
	public String getT4_5();
	public void setT4_5(String t4_5);
	
	//
	public String getT2();
	public void setT2(String t2);

	public String getT3();
	public void setT3(String t3);

	public String getT1();
	public void setT1(String t1);
	
	public String getT4();
	public void setT4(String t4);
	
	//
	public String getC1_1();
	public void setC1_1(String c1_1);

	public String getC1_2();
	public void setC1_2(String c1_2);

	public String getC1_3();	
	public void setC1_3(String c1_3);

	public String getC1_4();
	public void setC1_4(String c1_4);

	public String getC1_5();
	public void setC1_5(String c1_5);

	public String getC2_1();
	public void setC2_1(String c2_1);

	public String getC2_2();
	public void setC2_2(String c2_2);

	public String getC2_3();
	public void setC2_3(String c2_3);

	public String getC2_4();
	public void setC2_4(String c2_4);

	public String getC2_5();
	public void setC2_5(String c2_5);

	public String getC3_1();	
	public void setC3_1(String c3_1);

	public String getC3_2();
	public void setC3_2(String c3_2);

	public String getC3_3();
	public void setC3_3(String c3_3);

	public String getC3_4();
	public void setC3_4(String c3_4);

	public String getC3_5();
	public void setC3_5(String c3_5);

	public String getC4_1();
	public void setC4_1(String c4_1);

	public String getC4_2();
	public void setC4_2(String c4_2);

	public String getC4_3();
	public void setC4_3(String c4_3);

	public String getC4_4();
	public void setC4_4(String c4_4);

	public String getC4_5();
	public void setC4_5(String c4_5);

	//
	public String getAn();
	public void setAn(String an);
	
	public Long getBulletinSubjectId();
	public void setBulletinSubjectId(Long bulletinSubjectId);
}
