package com.oreilly.springdata.hadoop.pig;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pig.PigServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

/**
 * Simple runner for submitting Pig jobs sequentially. By default, the runner
 * waits for the jobs to finish and returns a boolean indicating whether all the
 * jobs succeeded or not (when there's no waiting, the status cannot be
 * determined and null is returned).
 * <p/>
 * For more control over the job execution and outcome consider using using
 * Spring Batch (see the reference documentation for more info).
 * 
 * @author Costin Leau
 * @author Mark Pollack
 */
public class PigRunner implements InitializingBean, ApplicationContextAware {

	private static final Log log = LogFactory.getLog(PigRunner.class);

	private boolean runAtStartup = false;
	private String pigServerName;

	private Collection<String> preJobScripts = new ArrayList<String>();
	private Collection<String> postJobScripts = new ArrayList<String>();

	private ApplicationContext context;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(pigServerName, "at least one job needs to be specified");

		if (runAtStartup) {
			run();
		}
	}

	/**
	 * Execute hdfs scripts before pig script execution, then executes pig scripts, then hdfs scripts
	 * after job execution
	 * 
	 * @throws Exception
	 *             If an exception is thrown the simple flow will stop.
	 */
	public void run() throws Exception {
				
		for (String scriptFactoryBeanName : preJobScripts) {
			context.getBean(scriptFactoryBeanName); //triggers execution of script 
		}
		
		// Need a new instance for each execution
		PigServer pigServer = context.getBean(pigServerName, PigServer.class);
		pigServer.setBatchOn();
		pigServer.getPigContext().connect();
		pigServer.executeBatch();
		pigServer.shutdown();
		
		for (String scriptFactoryBeanName : postJobScripts) {
			context.getBean(scriptFactoryBeanName); //triggers execution of script 
		}

	}

	/**
	 * Indicates whether the jobs should be submitted at startup or not.
	 * 
	 * @param runAtStartup
	 *            The runAtStartup to set.
	 */
	public void setRunAtStartup(boolean runAtStartup) {
		this.runAtStartup = runAtStartup;
	}

	/**
	 * Sets the PigServer to run scripts with
	 * 
	 * @param pigServer
	 *            t
	 */
	public void setPigServerName(String pigServerName) {
		this.pigServerName = pigServerName;
	}

	public void setPreJobScripts(Collection<String> preJobScripts) {
		this.preJobScripts = preJobScripts;
	}

	public void setPostJobScripts(Collection<String> postJobScripts) {
		this.postJobScripts = postJobScripts;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;
		
	}
}
