package com.oreilly.springdata.hadoop.hive;


public interface PasswordRepository {

	long count();
	void processPasswordFile(String inputFile) throws Exception;

}