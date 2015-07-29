package com.lemania.sis.server.bean.classe;

import java.util.Comparator;

/*
 * Sort class list by class name
 * */

public class ClassNameComparator implements Comparator<Classe> {

	@Override
	public int compare(Classe o1, Classe o2) {
		//
		return o1.getClassName().compareTo( o2.getClassName() );
	}

}
