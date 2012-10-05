package com.oreilly.springdata.hadoop.hive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class JdbcPasswordRepository implements PasswordRepository {

	private JdbcOperations jdbcOperations;

	private @Value("${hive.table}")	String tableName;

	@Autowired
	public JdbcPasswordRepository(JdbcOperations jdbcOperations) {
		Assert.notNull(jdbcOperations);
		this.jdbcOperations = jdbcOperations;
	}

	@Override
	public Long count() {
		return jdbcOperations.queryForLong("select count(*) from " + tableName);
	}

}
