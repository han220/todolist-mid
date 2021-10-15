package com.todo;

public enum PriorityItem {
	URGENT(1, "긴급"), NORMAL(2, "일반"), LATER(3, "나중에");
	
	private int no;
	private String kor;
	
	private PriorityItem(int no, String kor) {
		this.kor = kor;
		this.no = no;
	}
	
	public String getKor() {
		return kor;
	}
	
	public int getNo() {
		return no;
	}
	
	public static PriorityItem fromNo(int no) {
		for(PriorityItem p : PriorityItem.values())
			if(p.no == no) return p;
		return null;
	}
	
	public static String getAllOptions() {
		String output = "";
		for(PriorityItem p : PriorityItem.values()) {
			output += (p.no + ". " + p.kor + "\n");
		}
		return output;
	}

}
