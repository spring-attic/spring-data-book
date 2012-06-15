package com.oreilly.springdata.jdbc.oracle;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 *
 * @author Thomas Risberg
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/application-context-oracle.xml")
@Transactional
public class HelloOracleTests {

	@Autowired
	HelloOracle helloOracle;

	@Test
	public void testConnection() {
		String result = helloOracle.getDualFromDataSource();
		assertThat(result, is(notNullValue()));
		assertThat(result, is("X"));
	}

	@Test
	public void testMetaData() throws MetaDataAccessException {
		Map<String, String> result = helloOracle.getDatabaseMetaData();
		assertThat(result, is(notNullValue()));
		assertThat(result.get("Database Name"), is("Oracle"));
	}
}
