package com.oreilly.springdata.hadoop.pig;

import org.apache.pig.tools.pigstats.PigStats;
import org.springframework.dao.DataAccessException;

public class PigExceptionTranslator {

	public DataAccessException translateException(PigStats pigStats, Exception ex) {
		
		//TODO - map exception, error code to Spring's DAO exception heirarchy.
		return new UncategorizedPigException(ex.getMessage(), ex);
	}
	
}
