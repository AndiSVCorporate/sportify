package com.msports.sportify.shared;

import java.io.Serializable;

import javax.jdo.annotations.Index;
import javax.persistence.Id;

import com.googlecode.objectify.annotation.Entity;

@Entity
public class DailyStepsEntryOfy implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2048095068430043377L;
	
	@Id
	private Long id;
	private int stepsToday;
	@Index
	private long date;
	private String user;

	public DailyStepsEntryOfy() {	}
	
	public DailyStepsEntryOfy(int stepsToday, long date) {
		this.stepsToday = stepsToday;
		this.date = date;
		this.user = "test";
	}
	
	public int getStepsToday() {
		return stepsToday;
	}
	
	public void setStepsToday(int stepsToday) {
		this.stepsToday = stepsToday;
	}
	
	public Long getId() {
		return id;
	}
	
	public long getDate() {
		return date;
	}
	
	public void setDate(long date) {
		this.date = date;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}	
}
