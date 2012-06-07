package com.oreilly.springdata.jdbc.oracle;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class HelloOracle {

	public static void main(String[] args) {
		System.out.println("Hello Oracle");
		HelloOracle app = new HelloOracle();
		app.test();
	}

	public void test() {

		SimpleDriverDataSource ds = new SimpleDriverDataSource(new oracle.jdbc.OracleDriver(),"jdbc:oracle:thin:@redwood:1521:XE", "spring", "spring");

		try {
			printConnectionMetaData(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}

		JdbcTemplate db = new JdbcTemplate(ds);

		try {
			List<Map<String, Object>> results = db.queryForList("select * from dual");
			System.out.println(results);
		} catch (DataAccessException e) {
			if (e.getRootCause() instanceof SQLException) {
				SQLException se = (SQLException) e.getRootCause();
				System.out.println(se.getErrorCode() + " " + se.getSQLState() + " " + se.getMessage());
			}
			else {
				System.out.println(e.getMessage());
			}
		}
	}

	private void printConnectionMetaData(DataSource dataSource) throws Exception {
     JdbcUtils.extractDatabaseMetaData(dataSource,
             new DatabaseMetaDataCallback() {
                 public Object processMetaData(DatabaseMetaData databaseMetaData) throws SQLException, MetaDataAccessException {
					 System.out.println("Database Name: " + databaseMetaData.getDatabaseProductName());
                     System.out.println("Database Version: " + databaseMetaData.getDatabaseProductVersion());
                     System.out.println("Driver Name: " + databaseMetaData.getDriverName());
                     System.out.println("Driver Version: " + databaseMetaData.getDriverVersion());
                     return null;
                 }
             });
 }


}
