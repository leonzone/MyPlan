package com.sunday.myplan.bean;

import java.io.Serializable;
import java.util.Date;

import org.litepal.crud.DataSupport;


public class Plan extends DataSupport implements Serializable{
	private int id;
	private String planame;
	private String username;
	private int time;
	private String src;
	private Date date;
	private int flag;
	private PlanName plan;

	public Plan(String planame, int time, String src) {
		super();
		this.planame = planame;
		this.time = time;
		this.src = src;
	}
	public Plan( String planame, String username, int time, String src,
			Date date, int flag) {
		super();
		this.id = id;
		this.planame = planame;
		this.username = username;
		this.time = time;
		this.src = src;
		this.date = date;
		this.flag = flag;
	}
	public Plan()
	{
		
	}
	public PlanName getPlan() {
		return plan;
	}
	public void setPlan(PlanName plan) {
		this.plan = plan;
	}
	public String getPlaname() {
		return planame;
	}
	public void setPlaname(String planame) {
		this.planame = planame;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return username;
	}
	public void setName(String name) {
		this.username = name;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

}
