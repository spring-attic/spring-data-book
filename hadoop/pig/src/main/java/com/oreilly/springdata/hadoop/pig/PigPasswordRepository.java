package com.oreilly.springdata.hadoop.pig;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.Assert;

public class PigPasswordRepository implements PasswordRepository {

	private org.springframework.data.hadoop.pig.PigOperations pigOperations;
	
	private String pigScript = "classpath:password-analysis.pig";
		
	private String baseOutputDir = "/data/password-repo/output";
	
	private AtomicInteger counter = new AtomicInteger();
	
	public PigPasswordRepository(org.springframework.data.hadoop.pig.PigOperations pigOperations) {
		Assert.notNull(pigOperations);
		this.pigOperations = pigOperations;
	}
		
	public void setPigScript(String pigScript) {
		this.pigScript = pigScript;
	}
	
	public void setBaseOutputDir(String baseOutputDir) {
		this.baseOutputDir = baseOutputDir;
	}
	
	@Override
	public void processPasswordFile(String inputFile) {
		Assert.notNull(inputFile);
		String outputDir = baseOutputDir + File.separator + counter.incrementAndGet();
		Properties scriptParameters = new Properties();
		scriptParameters.put("inputDir", inputFile);
		scriptParameters.put("outputDir", outputDir);		
		pigOperations.executeScript(pigScript, scriptParameters);
	}
	
	@Override
	public void processPasswordFiles(Collection<String> inputFiles) {
		for (String inputFile : inputFiles) {
			processPasswordFile(inputFile);
		}
	}
}
