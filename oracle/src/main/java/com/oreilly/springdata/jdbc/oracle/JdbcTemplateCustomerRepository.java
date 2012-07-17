package com.oreilly.springdata.jdbc.oracle;

import com.oreilly.springdata.jdbc.domain.Address;
import com.oreilly.springdata.jdbc.domain.Customer;
import com.oreilly.springdata.jdbc.domain.EmailAddress;
import oracle.sql.ARRAY;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.support.oracle.StructMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JdbcTemplateCustomerRepository implements CustomerRepository {

	private JdbcTemplate jdbcTemplate;

	private static CustomerMapper customerMapper = new CustomerMapper();

	private static AddressMapper addressMapper = new AddressMapper();


	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Customer findById(Long id) {
		return null;
	}

	@Override
	public List<Customer> findAll() {
		return null;
	}

	@Override
	public void save(Customer customer) {
	}

	@Override
	public void delete(Customer customer) {

	}

	@Override
	public Customer findByEmailAddress(EmailAddress emailAddress) {
		return null;
	}

	@Override
	public void saveAddresses(Customer customer) {
	}


	private static class CustomerMapper implements StructMapper<Customer> {

		private static AddressMapper addressMapper = new AddressMapper();

		@Override
		public STRUCT toStruct(Customer customer, Connection conn, String typeName) throws SQLException {
			StructDescriptor descriptor = new StructDescriptor(typeName, conn);
			Object[] values = new Object[5];
			values[0] = customer.getId();
			values[1] = customer.getFirstName();
			values[2] = customer.getLastName();
			values[3] = customer.getEmailAddress().toString();
			return new STRUCT(descriptor, conn, values);
		}

		@Override
		public Customer fromStruct(STRUCT struct) throws SQLException {
			Object[] attr = struct.getAttributes();
			Customer customer = new Customer();
			if (attr[0] != null && attr[0] instanceof Number) {
				customer.setId(Long.valueOf(((Number)attr[0]).longValue()));
			}
			if (attr[1] != null && attr[1] instanceof String) {
				customer.setFirstName((String) attr[1]);
			}
			if (attr[2] != null && attr[2] instanceof String) {
				customer.setLastName((String) attr[2]);
			}
			if (attr[3] != null && attr[3] instanceof String) {
				customer.setEmailAddress(new EmailAddress((String)attr[3]));
			}
			if (attr[4] != null && attr[4] instanceof ARRAY) {
				ARRAY addrArray = (ARRAY) attr[4];
				Object[] addrStructs = (Object[]) addrArray.getArray();
				for (Object o : addrStructs) {
					if (o instanceof STRUCT) {
						customer.addAddress(addressMapper.fromStruct((STRUCT) o));
					}
				}
			}
			return customer;
		}
	}

	private static class AddressMapper implements StructMapper<Address> {

		@Override
		public STRUCT toStruct(Address source, Connection conn, String typeName) throws SQLException {
			StructDescriptor descriptor = new StructDescriptor("ADDRESS_TYPE", conn);
			Object[] values = new Object[3];
			values[0] = source.getStreet();
			values[1] = source.getCity();
			values[2] = source.getCountry().toString();
			return new STRUCT(descriptor, conn, values);
		}

		@Override
		public Address fromStruct(STRUCT struct) throws SQLException {
			Object[] attr = struct.getAttributes();
			Address address = new Address();
			if (attr[0] != null && attr[0] instanceof String) {
				address.setStreet((String) attr[0]);
			}
			if (attr[1] != null && attr[1] instanceof String) {
				address.setCity((String) attr[1]);
			}
			if (attr[2] != null && attr[2] instanceof String) {
				address.setCountry((String) attr[2]);
			}
			return address;
		}
	}
}
