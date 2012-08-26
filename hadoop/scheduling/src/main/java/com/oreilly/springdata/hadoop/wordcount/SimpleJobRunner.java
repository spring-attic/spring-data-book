package com.oreilly.springdata.hadoop.wordcount;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapreduce.Job;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.hadoop.scripting.HdfsScriptFactoryBean;
import org.springframework.util.Assert;

/**
 * Simple runner for submitting Hadoop jobs sequentially. By default, the runner
 * waits for the jobs to finish and returns a boolean indicating whether all the
 * jobs succeeded or not (when there's no waiting, the status cannot be
 * determined and null is returned).
 * <p/>
 * For more control over the job execution and outcome consider querying the
 * {@link Job}s or using Spring Batch (see the reference documentation for more
 * info).
 * 
 * @author Costin Leau
 */
public class SimpleJobRunner implements InitializingBean, DisposableBean {

	private static final Log log = LogFactory.getLog(SimpleJobRunner.class);

	private boolean runAtStartup = false;
	private boolean waitForJobs = true;
	private Collection<Job> jobs;
	private Collection<HdfsScriptFactoryBean> preJobScripts;
	private Collection<HdfsScriptFactoryBean> postJobScripts;



	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notEmpty(jobs, "at least one job needs to be specified");

		if (runAtStartup) {
			run();
		}
	}

	@Override
	public void destroy() throws Exception {
		if (!waitForJobs) {
			for (Job job : jobs) {
				try {
					job.killJob();
				} catch (Exception ex) {
					log.warn(
							"Cannot kill job [" + job.getJobID() + "|"
									+ job.getJobName() + " ] failed", ex);
				}
			}
		}
	}

	/**
	 * Execute scripts before job execution, then executes jobs, then scripts after job execution
	 * 
	 * @throws Exception If an exception is thrown the simple flow will stop.
	 */
	public void run() throws Exception {
		for (HdfsScriptFactoryBean scriptFactoryBean: preJobScripts) {
				scriptFactoryBean.getObject();			
		}
		for (Job job : jobs) {
			if (!waitForJobs) {
				job.submit();
			} else {
				job.waitForCompletion(true);
			}
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
	 * Sets the Jobs to run.
	 * 
	 * @param jobs
	 *            The jobs to run.
	 */
	public void setJobs(Collection<Job> jobs) {
		this.jobs = jobs;
	}

	public void setPreJobScripts(Collection<HdfsScriptFactoryBean> preJobScripts) {
		this.preJobScripts = preJobScripts;
	}
	
	public void setPostJobScripts(Collection<HdfsScriptFactoryBean> postJobScripts) {
		this.postJobScripts = postJobScripts;
	}
}
