package com.oreilly.springdata.jdbc.querydsl.repository;

import com.oreilly.springdata.jdbc.querydsl.TestConfig;
import com.oreilly.springdata.jdbc.querydsl.domain.Address;
import com.oreilly.springdata.jdbc.querydsl.domain.Customer;
import com.oreilly.springdata.jdbc.querydsl.domain.EmailAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Thomas Risberg
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestConfig.class})
@Transactional
public class QueryDslCustomerRepositoryTests {

	@Autowired
	CustomerRepository repository;

	@Test
	public void testFindAll() {
		List<Customer> result = repository.findAll();
		assertThat(result, is(notNullValue()));
		assertThat(result, hasSize(3));
		assertThat(result.get(0), notNullValue());
		assertThat(result.get(1), notNullValue());
		assertThat(result.get(2), notNullValue());
	}

	@Test
	public void testFindOne() {
		Customer result = repository.findOne(100);
		assertThat(result, is(notNullValue()));
		assertThat(result.getFirstName(), is("John"));
	}

	@Test
	public void saveNewCustomer() {
		Customer c = new Customer();
		c.setFirstName("Sven");
		c.setLastName("Svensson");
		c.setEmailAddress(new EmailAddress("sven@svensson.org"));
		Address a = new Address("Storgaten 6", "Trosa", "Sweden");
		c.addAddress(a);
		repository.save(c);
		System.out.println(repository.findAll());
		Customer result = repository.findOne(c.getId());
		assertThat(result, is(notNullValue()));
		assertThat(result.getFirstName(), is("Sven"));
		assertThat(result.getEmailAddress().toString(), is(notNullValue()));
	}

	@Test
	public void saveNewCustomerWithoutEmail() {
		Customer c = new Customer();
		c.setFirstName("Sven");
		c.setLastName("Svensson");
		Address a = new Address("Storgaten 6", "Trosa", "Sweden");
		c.addAddress(a);
		repository.save(c);
		System.out.println(repository.findAll());
		Customer result = repository.findOne(c.getId());
		assertThat(result, is(notNullValue()));
		assertThat(result.getFirstName(), is("Sven"));
		assertThat(result.getEmailAddress(), is(nullValue()));
	}

	@Test
	public void deleteCustomer() {
		Customer c = repository.findOne(100);
		repository.delete(c);
		Customer result = repository.findOne(100);
		assertThat(result, is(nullValue()));
	}
}
