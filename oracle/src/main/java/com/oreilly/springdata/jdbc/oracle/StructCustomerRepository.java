package com.oreilly.springdata.jdbc.oracle;

import com.oreilly.springdata.jdbc.domain.Customer;
import com.oreilly.springdata.jdbc.domain.EmailAddress;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.support.oracle.SqlReturnStruct;
import org.springframework.data.jdbc.support.oracle.SqlStructValue;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructCustomerRepository implements CustomerRepository {

	private SimpleJdbcCall getCustomerCall;

	private SimpleJdbcCall saveCustomerCall;


	@Autowired
	public void setDataSource(DataSource dataSource) {

		this.getCustomerCall = new SimpleJdbcCall(dataSource).withProcedureName("get_customer")
				.declareParameters(new SqlOutParameter("out_customer",
						OracleTypes.STRUCT, "CUSTOMER_TYPE",
						new SqlReturnStruct(Customer.class)));

		this.saveCustomerCall = new SimpleJdbcCall(dataSource).withProcedureName("save_customer")
				.declareParameters(new SqlParameter("in_customer", OracleTypes.STRUCT, "CUSTOMER_TYPE"));
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
	 	in.put("in_customer", new SqlStructValue(customer));
     	saveCustomerCall.execute(in);
	}

	@Override
	public void delete(Customer customer) {

	}

	@Override
	public Customer findByEmailAddress(EmailAddress emailAddress) {
		return null;
	}
}
