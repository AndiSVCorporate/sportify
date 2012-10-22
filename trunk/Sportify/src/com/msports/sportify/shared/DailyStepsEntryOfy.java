package com.msports.sportify.shared;

import java.util.Date;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Entity;

@Entity
public class DailyStepsEntryOfy {

	@Id
	private Long id;
	private int stepsToday;
	private Date date;

	public DailyStepsEntryOfy(int stepsToday, Date date) {
		this.stepsToday = stepsToday;
		this.date = date;
	}
	
	public int getStepsToday() {
		return stepsToday;
	}
	
	public Long getId() {
		return id;
	}
	
	public Date getDate() {
		return date;
	}	
}
