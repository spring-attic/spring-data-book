package com.oreilly.springdata.hadoop.hive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class HiveTemplatePasswordRepository implements PasswordRepository {

	private @Value("${hive.table}") String tableName;
	
	private HiveOperations hiveOperations;
	
	@Autowired
	public HiveTemplatePasswordRepository(HiveOperations hiveOperations) {
		this.hiveOperations = hiveOperations;
	}
	
	@Override
	public long count() {
		return hiveOperations.queryForLong("select count(*) from " + tableName);
	}

	@Override
	public void processPasswordFile(String inputFile) throws Exception {
		hiveOperations.executeScript(inputFile);
	}

}
