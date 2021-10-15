package com.todo.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.todo.Main;
import com.todo.PriorityItem;
import com.todo.TodoMain;

public class TodoItem {
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private int id;
	private int is_completed = 0;
	private String title;
	private String desc;
	private String current_date;
	private String owner = "noowner";
	private PriorityItem priority; // Number between 1(urgent)~9
	
	public static final SimpleDateFormat dueSdf = new SimpleDateFormat("yyyy/MM/dd");
	private String duedate, category;

	/**
	 * Create new item.
	 */
	public TodoItem(String category, String title, String desc, String duedate, PriorityItem p) {
		this.title = title;
		this.desc = desc;
		this.category = category;
		this.duedate = duedate;
		this.current_date = sdf.format(new Date());
		this.priority = p;
		this.owner = TodoMain.getUserName();
	}

	public TodoItem(String category, String title, String desc, String duedate, String current_date, PriorityItem p, int is_completed) {
		this.title = title;
		this.desc = desc;
		this.current_date = current_date;
		this.category = category;
		this.duedate = duedate;
		this.priority = p;
		this.is_completed = is_completed;
		this.owner = TodoMain.getUserName();
	}
	
	/**
	 * Full data
	 */
	public TodoItem(int id, String category, String title, String desc, String duedate, String current_date, int is_completed, PriorityItem p, String owner) {
		this.id = id;
		this.title = title;
		this.desc = desc;
		this.current_date = current_date;
		this.category = category;
		this.duedate = duedate;
		this.is_completed = is_completed;
		this.priority = p;
		this.owner = owner;
	}
	
	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getCurrent_date() {
		try {
			return sdf.parse(current_date);
		} catch (ParseException e) {
			return null;
		}
	}

	public String getCurrent_date_str() {
		return current_date;
	}

	public void setCurrent_date(Date current_date) {
		this.current_date = sdf.format(current_date);
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getDueDate() {
		return duedate;
	}
	
	public Date getDueDate_date() {
		try {
			return dueSdf.parse(current_date);
		} catch (ParseException e) {
			return null;
		} 
	}
	
	public PriorityItem getPrioirty() {
		return priority;
	}
	
	public String getOwner() {
		return owner;
	}

	public String toSaveString() {
		return category + "##" + title + "##" + desc + "##" + duedate + "##" + current_date + "##" + priority.getNo() + "##" + is_completed + "\n";
	}
	
	/**
	 * @deprecated Use print() instead as now item contains the index number.
	 * @param i index to print out
	 * @return
	 */
	@Deprecated
	public String print(int i) {
		return String.format("%d. [%s] %s - %s - %s - %s", i, category, title, desc, duedate, current_date);
	}
	
	@Override
	public String toString() {
		String checked = is_completed == 1 ? "[V]" : "";
		return String.format("%d. [%s] [%s] %s%s - %s - %s - %s (owner: %s)", id, priority.getKor().charAt(0) ,category, title, checked, desc, duedate, current_date, owner);
	}
}
