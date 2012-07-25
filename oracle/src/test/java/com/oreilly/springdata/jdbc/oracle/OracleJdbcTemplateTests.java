package com.oreilly.springdata.jdbc.oracle;

import com.oreilly.springdata.jdbc.domain.Person;
import com.oreilly.springdata.jdbc.domain.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.support.oracle.OracleXmlHandler;
import org.springframework.data.jdbc.support.oracle.OracleXmlTypeValue;
import org.springframework.data.jdbc.support.oracle.SqlArrayValue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.xml.SqlXmlHandler;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import static junit.framework.Assert.assertEquals;

/**
 *
 * @author Thomas Risberg
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/application-context-oracle.xml")
@Transactional
public class OracleJdbcTemplateTests {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Transactional @Test
	@Rollback(false)
	public void testArrayOfVarchar() {
		Person p1 = new Person();
		p1.setId(1L);
		p1.setName("Robert");
		p1.addNickName("Bob");
		p1.addNickName("Bobby");
		p1.addNickName("Robbie");
		jdbcTemplate.update(
				"insert into person (id, name, nick_names) values(?, ?, ?)",
						p1.getId(),
						p1.getName(),
						new SqlArrayValue<String>(p1.getNickNames().toArray(new String[0]), "NICK_NAMES_ARRAY"));
	}

	@Transactional @Test
	@Rollback(false)
	public void testXmlType() {
		Product p1 = new Product("Widget", new BigDecimal("22.55"), "A simple widget");
		p1.setId(1L);
		final SqlXmlHandler xmlHandler = new OracleXmlHandler();
		jdbcTemplate.update(
				"insert into product (id, name, description, price, attributes) values(?, ?, ?, ?, ?)",
						p1.getId(),
						p1.getName(),
						p1.getDescription(),
						p1.getPrice(),
						xmlHandler.newSqlXmlValue("<attributes><attribute name=\"color\">Blue</attribute></attributes>"));
		String result = jdbcTemplate.queryForObject(
				"select attributes from product where id = ?",
				new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						return xmlHandler.getXmlAsString(rs, 1);
					}
				},
				p1.getId());
		System.out.println("--> " + result);
	}

}
