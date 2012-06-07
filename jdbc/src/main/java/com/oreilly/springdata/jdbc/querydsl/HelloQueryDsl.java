package com.oreilly.springdata.jdbc.querydsl;

import com.mysema.query.sql.SQLQuery;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jdbc.query.QueryDslJdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class HelloQueryDsl {

	private final QProduct qProduct = QProduct.product;

	public static void main(String[] args) {
		System.out.println("Hello QueryDSL");
		HelloQueryDsl app = new HelloQueryDsl();
		app.test();
	}

	public void test() {

		SimpleDriverDataSource ds = new SimpleDriverDataSource(
				new org.hsqldb.jdbc.JDBCDriver(),
				"jdbc:hsqldb:hsql://localhost:9001/test", "sa", "");

		try {
			printConnectionMetaData(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}

		QueryDslJdbcTemplate template = new QueryDslJdbcTemplate(ds);

		try {
			SQLQuery sqlQuery = template.newSqlQuery()
					.from(qProduct)
			        .where(qProduct.name.eq("Thing"));
			List<Product> results = template.query(sqlQuery,
					BeanPropertyRowMapper.newInstance(Product.class),
					qProduct.all());
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
