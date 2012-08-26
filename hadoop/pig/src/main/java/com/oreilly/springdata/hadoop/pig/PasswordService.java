package com.oreilly.springdata.hadoop.pig;

import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.util.Assert;

public class PasswordService {

	private String baseOutputPath = "/data/output";
	
	private PasswordRepository passwordRepository;
	
	public PasswordService(PasswordRepository passwordRepository) {
		this.passwordRepository = passwordRepository;
	}
	
	public void setBaseOutputPath(String baseOutputPath) {
		Assert.notNull(baseOutputPath);
		this.baseOutputPath = baseOutputPath;
	}

	@ServiceActivator
	public void process(@Header("hdfs_path") String inputDir) {
		passwordRepository.processPasswordFile(inputDir, baseOutputPath);
	}
}
