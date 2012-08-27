/*
 * Copyright 2011-2012 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oreilly.springdata.hadoop.hive;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.service.HiveClient;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HiveApp {

	private static final Log log = LogFactory.getLog(HiveApp.class);

	public static void main(String[] args) throws Exception {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"/META-INF/spring/hive-apache-log-context.xml", HiveApp.class);
		log.info("Hive Application Running");
		context.registerShutdownHook();	
		
		/*
		JdbcPasswordRepository repo = context.getBean(JdbcPasswordRepository.class);		
		repo.processPasswordFile("password-analysis.hql");	
		log.info("Count of password entrires = " + repo.count());
		*/		
		
		AnalysisService analysis = context.getBean(AnalysisService.class);
		analysis.performAnalysis();
		
		
		/*
		HiveTemplate pigTemplate = context.getBean(HiveTemplate.class);
		Properties scriptParameters = new Properties();
		scriptParameters.put("piggybanklib","./lib/piggybank-0.9.2.jar");
		scriptParameters.put("inputFile","./data/apache.log");
		pigTemplate.executeScript("apache-log-simple.pig", scriptParameters);
		*/
		
		/*
		PasswordRepository repo = context.getBean(PigPasswordRepository.class);
		Collection<String> files = new ArrayList<String>();
		files.add("/etc/passwd");
		files.add("/etc/passwd");
			
		repo.processPasswordFiles(files, "/tmp/pwdout");
		*/
		//repo.processPasswordFile("/etc/passwd", "/tmp/pwdout");
		
		//System.out.println("hit enter to run again");
		//Scanner scanIn = new Scanner(System.in);
	    //scanIn.nextLine();
	    
		
	    
		/*
		PigServer pigServer = context.getBean(PigServer.class);
		pigServer.setBatchOn();
		pigServer.getPigContext().connect();
		pigServer.executeBatch();
		pigServer.shutdown();
		
		System.out.println("hit enter to run again");
		Scanner scanIn = new Scanner(System.in);
	    scanIn.nextLine();
	    
	    pigServer = context.getBean(PigServer.class);
		pigServer.setBatchOn();
		pigServer.getPigContext().connect();
		pigServer.executeBatch();
		pigServer.shutdown();
	    */
	    
	}
}
