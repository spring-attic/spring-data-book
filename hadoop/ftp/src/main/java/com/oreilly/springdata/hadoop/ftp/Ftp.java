package com.oreilly.springdata.hadoop.ftp;


import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.core.PollableChannel;
import org.springframework.util.Assert;

public class Ftp {

	private static final Log log = LogFactory.getLog(Ftp.class);

	public static void main(String[] args) throws Exception {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"/META-INF/spring/application-context.xml", Ftp.class);
		log.info("Ftp Application Running");
		context.registerShutdownHook();
		Scanner scanIn = new Scanner(System.in);
	    scanIn.nextLine();
		
		/*
		PollableChannel ftpChannel = context.getBean("ftpChannel", PollableChannel.class);

		Message<?> message1 = ftpChannel.receive(10000);
		//Message<?> message2 = ftpChannel.receive(2000);
		Message<?> message3 = ftpChannel.receive(1000);

		log.info(String.format("Received first file message: %s.", message1));
		//LOGGER.info(String.format("Received second file message: %s.", message2));
		log.info(String.format("Received nothing else: %s.", message3));

		Assert.notNull(message1, "Was expecting a first message");
		//assertNotNull(message2);
		Assert.notNull(message3,"Was NOT expecting a second message.");*/

	}
}
