package com.oreilly.springdata.hadoop.pig;

import java.io.File;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.Assert;

public class PigPasswordRepository implements PasswordRepository {

	private PigOperations pigOperations;
	
	private String pigScript = "password-analysis.pig";
	
	private AtomicInteger counter = new AtomicInteger();
	
	public PigPasswordRepository(PigOperations pigOperations) {
		Assert.notNull(pigOperations);
		this.pigOperations = pigOperations;
	}
		
	public void setPigScript(String pigScript) {
		this.pigScript = pigScript;
	}
	
	@Override
	public void processPasswordFile(String inputFile, String baseOutputDir) {
		Assert.notNull(inputFile);
		Assert.notNull(baseOutputDir);
		String outputDir = baseOutputDir + File.separator + counter.incrementAndGet();
		Properties scriptParameters = new Properties();
		scriptParameters.put("inputFile", inputFile);
		scriptParameters.put("outputDir", outputDir);
		pigOperations.executeScript(pigScript, scriptParameters);
	}
	
	@Override
	public void processPasswordFiles(Collection<String> inputFiles, String baseOutputDir) {
		for (String inputFile : inputFiles) {
			String outputDirectory = baseOutputDir + File.separator + counter.incrementAndGet();
			processPasswordFile(inputFile, outputDirectory);
		}
	}
}
