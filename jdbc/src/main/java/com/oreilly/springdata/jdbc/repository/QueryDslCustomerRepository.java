package com.oreilly.springdata.jdbc.repository;

import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.Path;
import com.oreilly.springdata.jdbc.domain.Address;
import com.oreilly.springdata.jdbc.domain.Customer;
import com.oreilly.springdata.jdbc.domain.EmailAddress;
import com.oreilly.springdata.jdbc.domain.QAddress;
import com.oreilly.springdata.jdbc.domain.QCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jdbc.core.OneToManyResultSetExtractor;
import org.springframework.data.jdbc.query.QueryDslJdbcTemplate;
import org.springframework.data.jdbc.query.SqlDeleteCallback;
import org.springframework.data.jdbc.query.SqlInsertCallback;
import org.springframework.data.jdbc.query.SqlInsertWithKeyCallback;
import org.springframework.data.jdbc.query.SqlUpdateCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 */
@Repository
@Transactional
public class QueryDslCustomerRepository implements CustomerRepository {

	private final QCustomer qCustomer = QCustomer.customer;

	private final QAddress qAddress = QAddress.address;

	private QueryDslJdbcTemplate template;

	private Path[] customerAddressProjection;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.template = new QueryDslJdbcTemplate(dataSource);
		customerAddressProjection = new Path[] {
				qCustomer.id, qCustomer.firstName, qCustomer.lastName, qCustomer.emailAddress,
				qAddress.id, qAddress.customerId, qAddress.street, qAddress.city, qAddress.country};
	}

	@Override
	@Transactional(readOnly = true)
	public Customer findOne(Long id) {
		return template.queryForObject(
				template.newSqlQuery()
						.from(qCustomer)
						.leftJoin(qCustomer._addressCustomerRef, qAddress)
						.where(qCustomer.id.eq(id)),
				new CustomerExtractor(),
				customerAddressProjection);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Customer> findAll() {
		return template.query(
				template.newSqlQuery()
						.from(qCustomer)
						.leftJoin(qCustomer._addressCustomerRef, qAddress),
				new CustomerListExtractor(),
				customerAddressProjection);
	}

	@Override
	public void save(final Customer customer) {
		if (customer.getId() == null) {
			Long generatedKey = template.insertWithKey(qCustomer, new SqlInsertWithKeyCallback<Long>() {
				@Override
				public Long doInSqlInsertWithKeyClause(SQLInsertClause insert) throws SQLException {
					return insert.columns(qCustomer.firstName, qCustomer.lastName, qCustomer.emailAddress)
							.values(customer.getFirstName(), customer.getLastName(),
									customer.getEmailAddress() == null ? null : customer.getEmailAddress().toString())
							.executeWithKey(qCustomer.id);
				}
			});
			customer.setId(generatedKey);
		}
		else {
			template.update(qCustomer, new SqlUpdateCallback() {
				@Override
				public long doInSqlUpdateClause(SQLUpdateClause update) {
					return update.where(qCustomer.id.eq(customer.getId()))
						.set(qCustomer.firstName, customer.getFirstName())
						.set(qCustomer.lastName, customer.getLastName())
						.set(qCustomer.emailAddress,
								customer.getEmailAddress() == null ? null : customer.getEmailAddress().toString())
						.execute();
				}
			});
		}
		// save address data
		final List<Long> ids = new ArrayList<Long>();
		for (Address a : customer.getAddresses()) {
			if (a.getId() != null) {
				ids.add(a.getId());
			}
		}
		// first delete any potentially removed addresses
		if (ids.size() > 0) {
			template.delete(qAddress, new SqlDeleteCallback() {
				@Override
				public long doInSqlDeleteClause(SQLDeleteClause delete) {
					return delete.where(qAddress.customerId.eq(customer.getId())
							.and(qAddress.id.notIn(ids))).execute();
				}
			});
		}
		// then update existing ones and add new ones
		for(final Address a : customer.getAddresses()) {
			if (a.getId() != null) {
				template.update(qAddress, new SqlUpdateCallback() {
					@Override
					public long doInSqlUpdateClause(SQLUpdateClause update) {
						return update.where(qAddress.id.eq(a.getId()))
								.set(qAddress.customerId, customer.getId())
								.set(qAddress.street, a.getStreet())
								.set(qAddress.city, a.getCity())
								.set(qAddress.country, a.getCountry())
								.execute();
					}
				});
			}
			else {
				template.insert(qAddress, new SqlInsertCallback() {
					@Override
					public long doInSqlInsertClause(SQLInsertClause insert) {
						return insert.columns(qAddress.customerId, qAddress.street, qAddress.city, qAddress.country)
								.values(customer.getId(), a.getStreet(), a.getCity(), a.getCountry())
								.execute();
					}
				});
			}
		}
	}

	@Override
	public void delete(final Customer customer) {
		template.delete(qAddress, new SqlDeleteCallback() {
			@Override
			public long doInSqlDeleteClause(SQLDeleteClause delete) {
				return delete.where(qAddress.customerId.eq(customer.getId())).execute();
			}
		});
		template.delete(qCustomer, new SqlDeleteCallback() {
			@Override
			public long doInSqlDeleteClause(SQLDeleteClause delete) {
				return delete.where(qCustomer.id.eq(customer.getId())).execute();
			}
		});
	}

	@Override
	@Transactional(readOnly = true)
	public Customer findByEmailAddress(EmailAddress emailAddress) {
		return null;
	}

	private static class CustomerListExtractor extends OneToManyResultSetExtractor<Customer, Address, Integer> {

		private static final QCustomer qCustomer = QCustomer.customer;

		private final QAddress qAddress = QAddress.address;

		public CustomerListExtractor() {
			super(new CustomerMapper(), new AddressMapper());
		}

		public CustomerListExtractor(ExpectedResults expectedResults) {
			super(new CustomerMapper(), new AddressMapper(), expectedResults);
		}

		@Override
		protected Integer mapPrimaryKey(ResultSet rs) throws SQLException {
			return rs.getInt(qCustomer.id.toString());
		}

		@Override
		protected Integer mapForeignKey(ResultSet rs) throws SQLException {
			String columnName = qAddress.addressCustomerRef.getLocalColumns().get(0).toString();
			if (rs.getObject(columnName) != null) {
				return rs.getInt(columnName);
			}
			else {
				return null;
			}
		}

		@Override
		protected void addChild(Customer root, Address child) {
			root.addAddress(child);
		}
	}

	private static class CustomerExtractor implements ResultSetExtractor<Customer> {

		CustomerListExtractor customerListExtractor =
				new CustomerListExtractor(OneToManyResultSetExtractor.ExpectedResults.ONE_OR_NONE);

		@Override
		public Customer extractData(ResultSet rs) throws SQLException, DataAccessException {
			List<Customer> list = customerListExtractor.extractData(rs);
			return list.size() > 0 ? list.get(0) : null;
		}
	}

	private static class CustomerMapper implements RowMapper<Customer> {

		private static final QCustomer qCustomer = QCustomer.customer;

		@Override
		public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Customer c = new Customer();
			c.setId(rs.getLong(qCustomer.id.toString()));
			c.setFirstName(rs.getString(qCustomer.firstName.toString()));
			c.setLastName(rs.getString(qCustomer.lastName.toString()));
			if (rs.getString(qCustomer.emailAddress.toString()) != null) {
				c.setEmailAddress(new EmailAddress(rs.getString(qCustomer.emailAddress.toString())));
			}
			return c;
		}
	}

	private static class AddressMapper implements RowMapper<Address> {

		private final QAddress qAddress = QAddress.address;

		@Override
		public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
			String street = rs.getString(qAddress.street.toString());
			String city = rs.getString(qAddress.city.toString());
			String country = rs.getString(qAddress.country.toString());
			Address a = new Address(street, city, country);
			a.setId(rs.getLong(qAddress.id.toString()));
			return a;
		}
	}
}
