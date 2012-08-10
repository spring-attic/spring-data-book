package com.oreilly.springdata.hadoop.wordcount;

import java.util.Map;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

public class WordCount {

	private static final Log log = LogFactory.getLog(WordCount.class);

	public static void main(String[] args) throws Exception {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"/launch-context.xml", WordCount.class);
		log.info("Batch WordCount Application Running");		
		context.registerShutdownHook();
		startJobs(context);
		
	}
	public static void startJobs(ApplicationContext ctx) {
		JobLauncher launcher = ctx.getBean(JobLauncher.class);
		Map<String, Job> jobs = ctx.getBeansOfType(Job.class);

		for (Map.Entry<String, Job> entry : jobs.entrySet()) {
			System.out.println("Executing job " + entry.getKey());
			try {
				if (launcher.run(entry.getValue(), new JobParameters()).getStatus().equals(BatchStatus.FAILED)){
					throw new BeanInitializationException("Failed executing job " + entry.getKey());
				}
			} catch (Exception ex) {
				throw new BeanInitializationException("Cannot execute job " + entry.getKey(), ex);
			}
		}
	}
}
