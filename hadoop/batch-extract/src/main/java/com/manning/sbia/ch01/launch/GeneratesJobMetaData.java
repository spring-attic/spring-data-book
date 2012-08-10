/**
 * 
 */
package com.manning.sbia.ch01.launch;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * Generates some execution.
 * WARNING: the job repository is deleted first!
 * @author acogoluegnes
 *
 */
public class GeneratesJobMetaData {
	
	private static final String repoDir = "./repo";
	
	private static String targetDir = "./temp";
	
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
			"/import-products-job-context.xml",
			"/com/manning/sbia/ch02/batch-infrastructure-context.xml",
			"/com/manning/sbia/ch02/connect-database-context.xml"
		);
		
		truncate(ctx);
		
		JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
		Job job = ctx.getBean(Job.class);		
		
		Calendar calendar = Calendar.getInstance(Locale.FRANCE);
		
		for(int i=0;i<10;i++) {
			if(i == 7) {
				corruptedProcessing(jobLauncher, job, calendar);
				normalProcessing(jobLauncher, job, calendar);				
			} else {
				normalProcessing(jobLauncher, job, calendar);				
			}			
			calendar.add(Calendar.DATE, 1);					
		}
		
		cleanRepoDir();
		cleanTargetDir();
		
	}
	
	private static final void normalProcessing(JobLauncher jobLauncher,Job job,Calendar calendar) throws Exception {
		initRepoDir();
		initTargetDir();
		
		copyInputFileToRepoDir("products.zip");
		
		launchJob(jobLauncher, job, calendar);		
	}
	
	private static final void corruptedProcessing(JobLauncher jobLauncher,Job job,Calendar calendar) throws Exception {
		initRepoDir();
		initTargetDir();
		
		copyInputFileToRepoDir("products_corrupted.zip");
		
		launchJob(jobLauncher, job, calendar);
		
		launchJob(jobLauncher, job, calendar);
	}
	
	private static final void launchJob(JobLauncher jobLauncher,Job job,Calendar calendar) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		jobLauncher.run(job, new JobParametersBuilder()
			.addString("inputResource", "file:"+repoDir+"/products.zip")
			.addString("targetDirectory", targetDir+"/importproductsbatch/")
			.addString("targetFile","products.txt")
			.addString("date", dateFormat.format(calendar.getTime()))
			.toJobParameters()
		);
	}
	
	private static void copyInputFileToRepoDir(String file) throws Exception {
		FileUtils.copyFile(new File("./input",file), new File(repoDir,"products.zip"));
	}
	
	private static void initRepoDir() throws Exception {
		initDir(repoDir);
	}
	
	private static void initTargetDir() throws Exception {
		initDir(targetDir);
	}
	
	private static void initDir (String dir) throws Exception {
		File dirAsFile = new File(dir);
		if(dirAsFile.exists()) {
			FileUtils.deleteDirectory(dirAsFile);
		}
		FileUtils.forceMkdir(dirAsFile);
	}
	
	private static void cleanRepoDir() throws Exception {
		cleanDir(repoDir);
	}
	
	private static void cleanTargetDir() throws Exception {
		cleanDir(targetDir);
	}
		
	private static void cleanDir(String dir) throws Exception {
		File dirAsFile = new File(dir);
		if(dirAsFile.exists()) {
			FileUtils.deleteDirectory(dirAsFile);
		}
	}
	
	private static void truncate(ApplicationContext ctx) {
		SimpleJdbcTemplate tpl = new SimpleJdbcTemplate(ctx.getBean(DataSource.class));
		tpl.update("delete from BATCH_STEP_EXECUTION_CONTEXT");
		tpl.update("delete from BATCH_JOB_EXECUTION_CONTEXT");
		tpl.update("delete from BATCH_STEP_EXECUTION");
		tpl.update("delete from BATCH_JOB_EXECUTION");
		tpl.update("delete from BATCH_JOB_PARAMS");
		tpl.update("delete from BATCH_JOB_INSTANCE");		
	}

}
