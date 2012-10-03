package com.oreilly.springdata.hadoop.hive;

import org.springframework.core.io.Resource;
import org.springframework.data.hadoop.hive.HiveOperations;
import org.springframework.data.hadoop.hive.HiveScript;
import org.springframework.util.Assert;

public class AnalysisService {

	private HiveOperations hiveOperations;
	private Resource scriptResource;
	
	public AnalysisService(HiveOperations hiveOperations, Resource scriptResource) {
		Assert.notNull(hiveOperations);
		Assert.notNull(scriptResource);
		this.hiveOperations = hiveOperations;
		this.scriptResource = scriptResource;
	}
	
	public void performAnalysis() throws Exception {
		hiveOperations.executeScript(new HiveScript((scriptResource)));
	}
}
