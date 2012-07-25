package com.oreilly.springdata.jdbc.domain;

import java.util.ArrayList;
import java.util.List;

public class Person extends AbstractEntity {

	private String name;

	private List<String> nickNames = new ArrayList<String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getNickNames() {
		return nickNames;
	}

	public void addNickName(String nickName) {
		this.nickNames.add(nickName);
	}
}
