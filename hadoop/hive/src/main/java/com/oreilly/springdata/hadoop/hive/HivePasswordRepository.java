package com.oreilly.springdata.hadoop.hive;

import org.apache.hadoop.hive.service.HiveClient;
import org.apache.hadoop.hive.service.HiveServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.hadoop.hive.HiveScriptRunner;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class HivePasswordRepository implements PasswordRepository, ResourceLoaderAware {

	  private ResourceLoader resourceLoader;
	
	  private final HiveClient hiveClient;
	   
	  private @Value("${hive.table}") String tableName;

	  @Autowired
	  public HivePasswordRepository(HiveClient hiveClient) {
	    Assert.notNull(hiveClient);
	    this.hiveClient = hiveClient;
	  }
	  
	  public long count() {
	    
	    try {

	      hiveClient.execute("select count(*) from " + tableName);
	      return Long.parseLong(hiveClient.fetchOne());

	      // checked exceptions
	    } catch (HiveServerException ex) {
	      throw translateExcpetion(ex);
	    } catch (org.apache.thrift.TException tex) { 
	      throw translateExcpetion(tex);
	    }
	  }

	private RuntimeException translateExcpetion(Exception ex) {
		return new RuntimeException(ex);
	}

	@Override
	public void processPasswordFile(String inputFile) throws Exception {
		HiveScriptRunner.run(hiveClient, resourceLoader.getResource(inputFile));
	}


	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	  
}
