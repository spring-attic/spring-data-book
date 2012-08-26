package com.oreilly.springdata.hadoop.hive;

import org.apache.hadoop.hive.service.HiveClient;
import org.apache.hadoop.hive.service.HiveServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class WebLogRepository {

	  private final HiveClient hiveClient;
	   
	  private @Value("${hive.table}") String tableName;

	  @Autowired
	  public WebLogRepository(HiveClient hiveClient) {
	    Assert.notNull(hiveClient);
	    this.hiveClient = hiveClient;
	  }

	  public void load() throws Exception {
		  hiveClient.execute("drop table passwords");
		  hiveClient.execute("create table passwords (user string, passwd string, uid int, gid int, userinfo string, home string, shell string)");
		  hiveClient.execute("load data local inpath '/etc/passwd' into table passwords");
	  }
	  
	  
	  public long count() {
	    
	    try {

	      hiveClient.execute("select count(1) from passwords");
	      return Long.parseLong(hiveClient.fetchOne());

	    } catch (HiveServerException ex) {
	      throw translateExcpetion(ex);
	    } catch (org.apache.thrift.TException tex) { // checked exception
	      throw translateExcpetion(tex);
	    }
	  }

	private RuntimeException translateExcpetion(Exception ex) {
		return new RuntimeException(ex);
	}
	  
}
