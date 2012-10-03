package com.oreilly.springdata.hadoop.hive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;
import org.springframework.util.Assert;

@Repository
public class JdbcPasswordRepository implements PasswordRepository, ResourceLoaderAware {

	private JdbcOperations jdbcOperations;

	private ResourceLoader resourceLoader;
	private @Value("${hive.table}")	String tableName;

	@Autowired
	public JdbcPasswordRepository(JdbcOperations jdbcOperations) {
		Assert.notNull(jdbcOperations);
		this.jdbcOperations = jdbcOperations;
	}

	@Override
	public long count() {
		return jdbcOperations.queryForLong("select count(*) from " + tableName);
	}

	/*
	@Override
	public void processPasswordFile(String inputFile) throws Exception {
		SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(jdbcOperations), 
											 resourceLoader.getResource(inputFile),
											 true);
	}*/

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
