package com.oreilly.springdata.hadoop.pig;

import org.springframework.dao.UncategorizedDataAccessException;

public class UncategorizedPigException extends
		UncategorizedDataAccessException {

	private static final long serialVersionUID = -8108309874593636936L;

	public UncategorizedPigException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
