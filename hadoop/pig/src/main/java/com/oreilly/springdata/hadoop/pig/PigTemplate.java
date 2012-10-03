package com.oreilly.springdata.hadoop.pig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.IOUtils;
import org.apache.pig.PigServer;
import org.apache.pig.tools.pigstats.PigStats;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.hadoop.pig.PigScript;
import org.springframework.util.Assert;

public class PigTemplate implements PigOperations, ApplicationContextAware {

	private static final Log log = LogFactory.getLog(PigTemplate.class);

	private final PigExceptionTranslator exceptionTranslator = new PigExceptionTranslator();
	private String pigServerName;

	private ApplicationContext context;

	public PigTemplate(String pigServerName) {
		Assert.notNull(pigServerName, "pig server name needs to be specified");
		this.pigServerName = pigServerName;
	}

	/**
	 * Execute hdfs scripts before pig script execution, then executes pig scripts, then hdfs scripts
	 * after job execution
	 * 
	 * @throws Exception
	 *             If an exception is thrown the simple flow will stop.
	 */
	public void executeDefaultScripts() throws Exception {

		execute(new PigServerCallback() {
			public void doInPig(PigServer pigServer) throws DataAccessException, IOException {
				pigServer.setBatchOn();
				pigServer.getPigContext().connect();
				pigServer.executeBatch();
			}
			
		});
	}
	
	
	/**
	 * Execute the scripts registered with the PigServer at startup but override the scriptParameters at runtime
	 * @param scriptParameters
	 */
	public void executeDefaultScripts(final Properties scriptParameters) {
		

		execute(new PigServerCallback() {
			public void doInPig(PigServer pigServer) throws DataAccessException, IOException {
				
				pigServer.setBatchOn();
				pigServer.getPigContext().connect();
				if (scriptParameters != null) {
					pigServer.getPigContext().getProperties().putAll(scriptParameters);
				}
				pigServer.executeBatch();
			}
			
		});
	}
	
	public PigStats executeScript(final String scriptResource, final Properties scriptProperties) {

		final Resource resource = this.context.getResource(scriptResource);
		if (!resource.exists()) {
			throw new InvalidDataAccessApiUsageException(String.format("Resource %s not found!", resource));
		}
	
		return execute(new PigServerCallback() { 
			public void doInPig(PigServer pigServer) throws DataAccessException ,IOException {				
				PigScript pigScript = new PigScript(resource, scriptProperties);
				registerScript(pigServer, pigScript);
								
				pigServer.setBatchOn();
				pigServer.getPigContext().connect();
				pigServer.executeBatch();
			}
		});
	}

	private void registerScript(PigServer pigServer, PigScript script) {
		InputStream in = null;
		try {
			in = script.getResource().getInputStream();
			pigServer.registerScript(in, script.getArguments());
		} catch (IOException ex) {
			throw new BeanInitializationException("Cannot register script " + script, ex);
		} finally {
			IOUtils.closeStream(in);
		}
	}

	/**
	 * @return
	 */
	private PigServer getNewPigServer() {
		PigServer pigServer = context.getBean(pigServerName, PigServer.class);
		return pigServer;
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;
		
	}

	@Override
	public PigStats execute(PigServerCallback action) {
		Assert.notNull(action);
		PigServer pigServer = this.getNewPigServer();
		try {
			action.doInPig(pigServer);
			pigServer.shutdown();
			return PigStats.get();
		} catch (IOException e) {
			pigServer.shutdown();
			throw translateException(PigStats.get(), e);
		}
	}

	private RuntimeException translateException(PigStats pigStats, IOException ex) {
		return exceptionTranslator.translateException(pigStats, ex);
	}
}
