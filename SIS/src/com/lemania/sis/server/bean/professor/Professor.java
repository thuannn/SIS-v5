package com.lemania.sis.server.bean.professor;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.server.DatastoreObject;

@Entity
@Index
public class Professor extends DatastoreObject implements Comparable<Professor> {

	private String profName;
	private String profEmail;
	private Boolean profActive;
	private String logModifyDate = "";
	
	@IgnoreSave
	double total_01 = 0;
	@IgnoreSave
	double total_02 = 0;
	@IgnoreSave
	double total_03 = 0;
	@IgnoreSave
	double total_04 = 0;
	@IgnoreSave
	double total_05 = 0;
	@IgnoreSave
	double total_06 = 0;
	@IgnoreSave
	double total_07 = 0;
	@IgnoreSave
	double total_08 = 0;
	@IgnoreSave
	double total_09 = 0;
	@IgnoreSave
	double total_10 = 0;
	@IgnoreSave
	double total_11 = 0;
	@IgnoreSave
	double total_12 = 0;
	
	@IgnoreSave
	double fee_01 = 0;
	@IgnoreSave
	double fee_02 = 0;
	@IgnoreSave
	double fee_03 = 0;
	@IgnoreSave
	double fee_04 = 0;
	@IgnoreSave
	double fee_05 = 0;
	@IgnoreSave
	double fee_06 = 0;
	@IgnoreSave
	double fee_07 = 0;
	@IgnoreSave
	double fee_08 = 0;
	@IgnoreSave
	double fee_09 = 0;
	@IgnoreSave
	double fee_10 = 0;
	@IgnoreSave
	double fee_11 = 0;
	@IgnoreSave
	double fee_12 = 0;
	
	public String getProfName() {
		return profName;
	}
	
	public void setProfName(String profName) {
		this.profName = profName;
	}
	
	public Boolean getProfActive() {
		return profActive;
	}
	
	public void setProfActive(Boolean profActive) {
		this.profActive = profActive;
	}

	@Override
	public int compareTo(Professor prof) {
		int lastProfName = profName.compareTo(prof.getProfName());
		return lastProfName;
	}

	public double getTotal_01() {
		return total_01;
	}

	public void setTotal_01(double total_01) {
		this.total_01 = total_01;
	}

	public double getTotal_02() {
		return total_02;
	}

	public void setTotal_02(double total_02) {
		this.total_02 = total_02;
	}

	public double getTotal_03() {
		return total_03;
	}

	public void setTotal_03(double total_03) {
		this.total_03 = total_03;
	}

	public double getTotal_04() {
		return total_04;
	}

	public void setTotal_04(double total_04) {
		this.total_04 = total_04;
	}

	public double getTotal_05() {
		return total_05;
	}

	public void setTotal_05(double total_05) {
		this.total_05 = total_05;
	}

	public double getTotal_06() {
		return total_06;
	}

	public void setTotal_06(double total_06) {
		this.total_06 = total_06;
	}

	public double getTotal_07() {
		return total_07;
	}

	public void setTotal_07(double total_07) {
		this.total_07 = total_07;
	}

	public double getTotal_08() {
		return total_08;
	}

	public void setTotal_08(double total_08) {
		this.total_08 = total_08;
	}

	public double getTotal_09() {
		return total_09;
	}

	public void setTotal_09(double total_09) {
		this.total_09 = total_09;
	}

	public double getTotal_10() {
		return total_10;
	}

	public void setTotal_10(double total_10) {
		this.total_10 = total_10;
	}

	public double getTotal_11() {
		return total_11;
	}

	public void setTotal_11(double total_11) {
		this.total_11 = total_11;
	}

	public double getTotal_12() {
		return total_12;
	}

	public void setTotal_12(double total_12) {
		this.total_12 = total_12;
	}

	public double getFee_01() {
		return fee_01;
	}

	public void setFee_01(double fee_01) {
		this.fee_01 = fee_01;
	}

	public double getFee_02() {
		return fee_02;
	}

	public void setFee_02(double fee_02) {
		this.fee_02 = fee_02;
	}

	public double getFee_03() {
		return fee_03;
	}

	public void setFee_03(double fee_03) {
		this.fee_03 = fee_03;
	}

	public double getFee_04() {
		return fee_04;
	}

	public void setFee_04(double fee_04) {
		this.fee_04 = fee_04;
	}

	public double getFee_05() {
		return fee_05;
	}

	public void setFee_05(double fee_05) {
		this.fee_05 = fee_05;
	}

	public double getFee_06() {
		return fee_06;
	}

	public void setFee_06(double fee_06) {
		this.fee_06 = fee_06;
	}

	public double getFee_07() {
		return fee_07;
	}

	public void setFee_07(double fee_07) {
		this.fee_07 = fee_07;
	}

	public double getFee_08() {
		return fee_08;
	}

	public void setFee_08(double fee_08) {
		this.fee_08 = fee_08;
	}

	public double getFee_09() {
		return fee_09;
	}

	public void setFee_09(double fee_09) {
		this.fee_09 = fee_09;
	}

	public double getFee_10() {
		return fee_10;
	}

	public void setFee_10(double fee_10) {
		this.fee_10 = fee_10;
	}

	public double getFee_11() {
		return fee_11;
	}

	public void setFee_11(double fee_11) {
		this.fee_11 = fee_11;
	}

	public double getFee_12() {
		return fee_12;
	}

	public void setFee_12(double fee_12) {
		this.fee_12 = fee_12;
	}

	public String getLogModifyDate() {
		return logModifyDate;
	}

	public void setLogModifyDate(String logModifyDate) {
		this.logModifyDate = logModifyDate;
	}

	public String getProfEmail() {
		return profEmail;
	}

	public void setProfEmail(String profEmail) {
		this.profEmail = profEmail;
	}	
}
