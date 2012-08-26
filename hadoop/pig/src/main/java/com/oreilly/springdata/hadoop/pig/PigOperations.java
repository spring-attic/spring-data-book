package com.oreilly.springdata.hadoop.pig;

import java.util.Properties;

import org.apache.pig.tools.pigstats.PigStats;


public interface PigOperations {

	PigStats execute(PigServerCallback action);		
	
	PigStats executeScript(final String scriptResource, final Properties scriptProperties);
}
