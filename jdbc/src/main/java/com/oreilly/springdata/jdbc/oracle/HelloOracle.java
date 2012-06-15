package com.oreilly.springdata.jdbc.oracle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class HelloOracle {

	DataSource dataSource;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getDualFromDataSource() {
		JdbcTemplate db = new JdbcTemplate(dataSource);
		String results = db.queryForObject("select dummy from dual", String.class);
		return results;
	}

	public Map<String, String> getDatabaseMetaData() throws MetaDataAccessException {

		final Map<String, String> metaData = new HashMap<String, String>();
     	return (Map<String, String>) JdbcUtils.extractDatabaseMetaData(dataSource,
				 new DatabaseMetaDataCallback() {
					 public Object processMetaData(DatabaseMetaData databaseMetaData) throws SQLException, MetaDataAccessException {
						 metaData.put("Database Name", databaseMetaData.getDatabaseProductName());
						 metaData.put("Database Version", databaseMetaData.getDatabaseProductVersion());
						 metaData.put("Driver Name", databaseMetaData.getDriverName());
						 metaData.put("Driver Version", databaseMetaData.getDriverVersion());
						 return metaData;
					 }
				 });

 }


}
