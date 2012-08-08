package com.oreilly.springdata.hadoop.streaming;

import java.util.concurrent.atomic.AtomicLong;


/**
 * Logic for writing to files of a specified size or other strategies go here as they are shared across 
 * implementations
 *
 */
public abstract class AbstractHdfsWriter implements HdfsWriter {
	
	//TODO need to initialize the counter based on directory contents.
	private final AtomicLong counter = new AtomicLong(0L);
	
	private long rolloverThresholdInBytes = 99999;
	
	private String baseFilename = HdfsTextFileWriterFactory.DEFAULT_BASE_FILENAME;
	private String basePath = HdfsTextFileWriterFactory.DEFAULT_BASE_PATH;
	
	
	public long getRolloverThresholdInBytes() {
		return rolloverThresholdInBytes;
	}

	public void setRolloverThresholdInBytes(long rolloverThresholdInBytes) {
		this.rolloverThresholdInBytes = rolloverThresholdInBytes;
	}


	
	public String getBaseFilename() {
		return baseFilename;
	}

	public void setBaseFilename(String baseFilename) {
		this.baseFilename = baseFilename;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public long getCounter() {
		return counter.get();
	}
	
	public void incrementCounter() {
		counter.incrementAndGet();
	}
	
	public String getPathName() {
		//TODO configure file suffix
		return basePath + baseFilename + "-" + getCounter() + ".log";
	}
	
}
