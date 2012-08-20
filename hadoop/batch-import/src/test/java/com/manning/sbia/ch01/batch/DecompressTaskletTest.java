/**
 * 
 */
package com.manning.sbia.ch01.batch;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * @author acogoluegnes
 *
 */
public class DecompressTaskletTest {
	
	private static final String[] EXPECTED_CONTENT = new String [] {
		"PRODUCT_ID,NAME,DESCRIPTION,PRICE",
		"PR....210,BlackBerry 8100 Pearl,,124.60",
		"PR....211,Sony Ericsson W810i,,139.45",
		"PR....212,Samsung MM-A900M Ace,,97.80",
		"PR....213,Toshiba M285-E 14,,166.20",
		"PR....214,Nokia 2610 Phone,,145.50",
		"PR....215,CN Clogs Beach/Garden Clog,,190.70",
		"PR....216,AT&T 8525 PDA,,289.20",
		"PR....217,Canon Digital Rebel XT 8MP Digital SLR Camera,,13.70",
	};

	@Test public void execute() throws Exception {
		DecompressTasklet tasklet = new DecompressTasklet();
		tasklet.setInputResource(new ClassPathResource("/input/products.zip"));
		File outputDir = new File("./target/decompresstasklet");
		if(outputDir.exists()) {
			FileUtils.deleteDirectory(outputDir);
		}
		tasklet.setTargetDirectory(outputDir.getAbsolutePath());
		tasklet.setTargetFile("products.txt");
		
		tasklet.execute(null, null);
		
		File output = new File(outputDir,"products.txt");
		Assert.assertTrue(output.exists());
		
		Assert.assertArrayEquals(EXPECTED_CONTENT, FileUtils.readLines(output).toArray());
		
	}
	
	@Test public void corruptedArchive() throws Exception {
		DecompressTasklet tasklet = new DecompressTasklet();
		tasklet.setInputResource(new ClassPathResource("/input/products_corrupted.zip"));
		File outputDir = new File("./target/decompresstasklet");
		if(outputDir.exists()) {
			FileUtils.deleteDirectory(outputDir);
		}
		tasklet.setTargetDirectory(outputDir.getAbsolutePath());
		tasklet.setTargetFile("products.txt");
		
		try {
			tasklet.execute(null, null);
			Assert.fail("corrupted archive, the tasklet should have thrown an exception");
		} catch (Exception e) {
			// OK
		}
	}
	
}
