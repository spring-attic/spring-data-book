package com.oreilly.springdata.hadoop.hive;

import org.springframework.dao.UncategorizedDataAccessException;

public class UncategorizedHiveException extends
		UncategorizedDataAccessException {

	private static final long serialVersionUID = 3928973515786901287L;

	public UncategorizedHiveException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
