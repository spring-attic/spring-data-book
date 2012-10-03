package com.oreilly.springdata.hadoop.hive;

import org.apache.hadoop.hive.service.HiveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.hadoop.hive.HiveClientCallback;
import org.springframework.data.hadoop.hive.HiveOperations;
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
		/*
		Long number = hiveOperations.queryForLong("select count(*) from " + tableName);
		*/
		Long number = queryForLong("select count(*) from " + tableName);
		return (number != null ? number.longValue() : 0);
	}
	
	
	
	public Long queryForLong(final String hql) throws DataAccessException {
		return hiveOperations.execute(new HiveClientCallback<Long>() {
			@Override
			public Long doInHive(HiveClient hiveClient) throws Exception,
					DataAccessException {
				hiveClient.execute(hql);
				return Long.parseLong(hiveClient.fetchOne());
			}
		});
	}

}
