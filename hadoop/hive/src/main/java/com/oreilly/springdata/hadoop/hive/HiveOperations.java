package com.oreilly.springdata.hadoop.hive;

import org.springframework.dao.DataAccessException;


public interface HiveOperations {

	<T> T execute(HiveClientCallback<T> action) throws DataAccessException;	
	
	int queryForInt(String hql) throws DataAccessException;
	
	long queryForLong(String hql) throws DataAccessException;	
	
	void executeScript(final String scriptResource) throws DataAccessException;
}
