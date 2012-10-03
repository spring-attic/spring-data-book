package com.oreilly.springdata.hadoop.hive.core;

import org.springframework.dao.DataAccessException;

import com.oreilly.springdata.hadoop.hive.HiveClientCallback;


public interface HiveOperations {

	<T> T execute(HiveClientCallback<T> action) throws DataAccessException;	
	
	int queryForInt(String hql) throws DataAccessException;
	
	long queryForLong(String hql) throws DataAccessException;	
	
	void executeScript(final String scriptResource) throws DataAccessException;
}
