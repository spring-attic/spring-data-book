package com.oreilly.springdata.hadoop.hive;

import org.apache.hadoop.hive.service.HiveClient;
import org.springframework.core.io.Resource;
import org.springframework.data.hadoop.hive.HiveScriptRunner;
import org.springframework.util.Assert;

public class AnalysisService {

	private HiveClient hiveClient;
	private Resource scriptResource;
	
	public AnalysisService(HiveClient hiveClient, Resource scriptResource) {
		Assert.notNull(hiveClient);
		Assert.notNull(scriptResource);
		this.hiveClient = hiveClient;
		this.scriptResource = scriptResource;
	}
	
	public void performAnalysis() throws Exception {
		HiveScriptRunner.run(hiveClient, scriptResource);
	}
}
