package com.sunday.myplan.bean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.litepal.crud.DataSupport;
public class PlanName  extends DataSupport implements Serializable{
	private int id;
	private int time;
	private String planname;
	private String src;
	public PlanName(String planname,int time ) {
		super();
		this.time = time;
		this.planname = planname;
	}
	private Date date;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	private List<Plan> mplan = new ArrayList<Plan>();
	public PlanName(){}
	public PlanName( String planname) {
		super();
		this.planname = planname;
	}
	public PlanName( int time, String planname, String src, Date date) {
		super();
		this.time = time;
		this.planname = planname;
		this.src = src;
		this.date = date;
	}
	public PlanName(int time, String planname, String src, Date date,
			List<Plan> mplan) {
		super();
		this.time = time;
		this.planname = planname;
		this.src = src;
		this.date = date;
		this.mplan = mplan;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPlanname() {
		return planname;
	}
	public void setPlanname(String planname) {
		this.planname = planname;
	}
	public List<Plan> getMplan() {
		return mplan;
	}
	public void setMplan(List<Plan> mplan) {
		this.mplan = mplan;
	}


}
