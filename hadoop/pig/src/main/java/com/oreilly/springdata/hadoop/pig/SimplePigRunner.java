package com.oreilly.springdata.hadoop.pig;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapreduce.Job;
import org.apache.pig.PigServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.hadoop.scripting.HdfsScriptFactoryBean;
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
public class SimplePigRunner implements InitializingBean, DisposableBean, ApplicationContextAware {

	private static final Log log = LogFactory.getLog(SimplePigRunner.class);

	private boolean runAtStartup = false;
	private boolean waitForJobs = true;
	private PigServer pigServer;
	/*
	private Collection<HdfsScriptFactoryBean> preJobScripts;
	private Collection<HdfsScriptFactoryBean> postJobScripts;
	*/
	private Collection<String> preJobScripts;
	private Collection<String> postJobScripts;

	private ApplicationContext context;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(pigServer, "at least one job needs to be specified");

		if (runAtStartup) {
			run();
		}
	}

	@Override
	public void destroy() throws Exception {
		if (!waitForJobs) {
			try {
				pigServer.shutdown();
			} catch (Exception ex) {
				log.warn("Cannot shutdown PigServer cleanly", ex);
			}
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
//			//
			//scriptFactoryBean.getObject();
			context.getBean(scriptFactoryBeanName); //triggers execution of script 
		}
		pigServer.setBatchOn();
		pigServer.getPigContext().connect();
		pigServer.executeBatch();
		pigServer.shutdown();

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
	 * Indicates whether the runner should wait for the jobs to finish (the
	 * default) or not.
	 * 
	 * @param waitForJobs
	 *            The waitForJobs to set.
	 */
	public void setWaitForJobs(boolean waitForJobs) {
		this.waitForJobs = waitForJobs;
	}

	/**
	 * Sets the PigServer to run scripts with
	 * 
	 * @param pigServer
	 *            t
	 */
	public void setPigServer(PigServer pigServer) {
		this.pigServer = pigServer;
	}

	public void setPreJobScripts(Collection<String> preJobScripts) {
		this.preJobScripts = preJobScripts;
	}

	public void setPostScripts(Collection<String> postJobScripts) {
		this.postJobScripts = postJobScripts;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;
		
	}
}
