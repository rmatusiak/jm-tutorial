package com.acme.craft.fixme.dry;

public class User {

	
	private Integer age;
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	private String login;
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getFullName() {
		return fullName;
	}
	private String fullName;
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public boolean isAdult() {
		if (getAge() < 18){
			return false;
		} else {
			return true;
		}
		
	}
	
	
}
