package com.oreilly.springdata.jdbc;

import com.oreilly.springdata.jdbc.repository.CustomerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 *
 * @author Thomas Risberg
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackageClasses = CustomerRepository.class)
public class TestConfig {

	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()
		            .setType(EmbeddedDatabaseType.HSQL)
		            .addScript("classpath:sql/schema.sql")
		            .addScript("classpath:sql/test-data.sql")
		            .build();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager txManager = new DataSourceTransactionManager();
		txManager.setDataSource(dataSource());
		return txManager;
	}
}
