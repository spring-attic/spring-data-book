/**
 * 
 */
package com.manning.sbia.ch01.batch;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/import-products-job-context.xml","/test-context.xml"})
public class ImportProductsIntegrationTest {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job job;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void setUp() throws Exception {
		jdbcTemplate.update("delete from product");
		jdbcTemplate.update(
			"insert into product (id,name,description,price) values(?,?,?,?)",
			"PR....214","Nokia 2610 Phone","",102.23	
		);
	}

	@Test public void importProducts() throws Exception {
		int initial = jdbcTemplate.queryForInt("select count(1) from product");
		
		jobLauncher.run(job, new JobParametersBuilder()
			.addString("inputResource", "classpath:/input/products.zip")
			.addString("targetDirectory", "./target/importproductsbatch/")
			.addString("targetFile","products.txt")
			.addLong("timestamp", System.currentTimeMillis())
			.toJobParameters()
		);
		int nbOfNewProducts = 7;
		Assert.assertEquals(initial+nbOfNewProducts,jdbcTemplate.queryForInt("select count(1) from product"));
	}
	
	@Test public void importProductsWithErrors() throws Exception {
		int initial = jdbcTemplate.queryForInt("select count(1) from product");
		
		jobLauncher.run(job, new JobParametersBuilder()
			.addString("inputResource", "classpath:/input/products_with_errors.zip")
			.addString("targetDirectory", "./target/importproductsbatch/")
			.addString("targetFile","products.txt")
			.addLong("timestamp", System.currentTimeMillis())
			.toJobParameters()
		);
		int nbOfNewProducts = 6;
		Assert.assertEquals(initial+nbOfNewProducts,jdbcTemplate.queryForInt("select count(1) from product"));
	}
	
}
