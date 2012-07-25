package com.oreilly.springdata.jdbc.oracle;

import com.oreilly.springdata.jdbc.domain.Address;
import com.oreilly.springdata.jdbc.domain.Customer;
import com.oreilly.springdata.jdbc.domain.EmailAddress;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.support.oracle.SqlReturnStruct;
import org.springframework.data.jdbc.support.oracle.SqlStructArrayValue;
import org.springframework.data.jdbc.support.oracle.SqlStructValue;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.data.jdbc.support.oracle.StructMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcCallCustomerRepository implements CustomerRepository {

	private SimpleJdbcCall getCustomerCall;

	private SimpleJdbcCall saveCustomerCall;

	private SimpleJdbcCall saveAddressesCall;

	private static CustomerMapper customerMapper = new CustomerMapper();

	private static AddressMapper addressMapper = new AddressMapper();


	@Autowired
	public void setDataSource(DataSource dataSource) {

		this.getCustomerCall = new SimpleJdbcCall(dataSource).withProcedureName("get_customer")
				.declareParameters(new SqlOutParameter("out_customer",
						OracleTypes.STRUCT, "CUSTOMER_TYPE",
						new SqlReturnStruct(customerMapper)));

		this.saveCustomerCall = new SimpleJdbcCall(dataSource).withFunctionName("save_customer")
				.declareParameters(
						new SqlOutParameter("result", Types.BIGINT),
						new SqlParameter("in_customer", OracleTypes.STRUCT, "CUSTOMER_TYPE"));

		this.saveAddressesCall = new SimpleJdbcCall(dataSource).withProcedureName("save_addresses")
				.declareParameters(
						new SqlParameter("in_customer_id", Types.BIGINT),
						new SqlParameter("in_addresses", OracleTypes.ARRAY, "ADDRESS_TABLE_TYPE"));
	}

	@Override
	public Customer findById(Long id) {
		Map<String, Object> in = new HashMap<String, Object>();
		in.put("in_customer_id", id);
		return getCustomerCall.executeObject(Customer.class, in);
	}

	@Override
	public List<Customer> findAll() {
		return null;
	}

	@Override
	public void save(Customer customer) {
		Map<String, Object> in = new HashMap<String, Object>();
	 	in.put("in_customer", new SqlStructValue<Customer>(customer, customerMapper));
     	Long id = saveCustomerCall.executeObject(Long.class, in);
		if (customer.getId() == null) {
			customer.setId(id);
		}
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
		Map<String, Object> in = new HashMap<String, Object>();
	 	in.put("in_customer_id", customer.getId());
	 	in.put("in_addresses",
				 new SqlStructArrayValue<Address>(
						 customer.getAddresses().toArray(new Address[0]),
						 addressMapper,
						 "ADDRESS_TYPE"));
     	saveAddressesCall.execute(in);
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
			StructDescriptor descriptor = new StructDescriptor(typeName, conn);
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
