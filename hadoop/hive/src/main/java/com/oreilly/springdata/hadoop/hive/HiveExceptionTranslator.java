package com.oreilly.springdata.hadoop.hive;

import org.springframework.dao.DataAccessException;

public class HiveExceptionTranslator {

	public DataAccessException translateException(Exception ex) {
		
		//TODO - map exception, error code to Spring's DAO exception heirarchy.
		return new UncategorizedHiveException(ex.getMessage(), ex);
	}
	
}
