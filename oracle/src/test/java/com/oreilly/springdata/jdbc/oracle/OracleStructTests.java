package com.oreilly.springdata.jdbc.oracle;

import com.oreilly.springdata.jdbc.domain.Customer;
import com.oreilly.springdata.jdbc.domain.EmailAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Thomas Risberg
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/application-context-oracle.xml")
@Transactional
public class OracleStructTests {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	StructCustomerRepository customerRepository;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Transactional @Test
	public void testStruct() {
		Customer cust = new Customer();
		cust.setId(4L);
		cust.setFirstName("Rod");
		cust.setLastName("Johnson");
		cust.setEmailAddress(new EmailAddress("rod@springsource.org"));
		customerRepository.save(cust);
		int count = jdbcTemplate.queryForInt("select count(*) from customer where id = 4");
		assertEquals("customer not added", 1, count);
		Customer result = customerRepository.findById(4L);
		assertEquals("wrong customer", "Rod", result.getFirstName());
		assertEquals("wrong email", cust.getEmailAddress(), result.getEmailAddress());
	}
}
