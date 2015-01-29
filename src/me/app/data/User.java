package me.app.data;

public class User {
	private int id = -1;
	private String name;
	private String pwd;
	private int score = 0;
	private int member;
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private boolean remerberPwd = false;
	private boolean isLogin = false;

	public boolean isRemerberPwd() {
		return remerberPwd;
	}

	public void setRemerberPwd(boolean remerberPwd) {
		this.remerberPwd = remerberPwd;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

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

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getMember() {
		return member;
	}

	public void setMember(int member) {
		this.member = member;
	}

}
