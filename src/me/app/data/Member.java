package me.app.data;

public class Member {

	private int id;
	private String name;
	private int score;
	private int viewNum;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getViewNum() {
		return viewNum;
	}
	public void setViewNum(int viewNum) {
		this.viewNum = viewNum;
	}
	public Member(int id, String name, int score, int viewNum) {
		this.id = id;
		this.name = name;
		this.score = score;
		this.viewNum = viewNum;
	}
	
}
