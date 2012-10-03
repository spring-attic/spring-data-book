package com.oreilly.springdata.hadoop.hive;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.service.HiveClient;
import org.apache.hadoop.hive.service.HiveServerException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class HivePasswordRepository implements PasswordRepository,
		ResourceLoaderAware {

	private static final Log logger = LogFactory.getLog(HivePasswordRepository.class);
	
	private ResourceLoader resourceLoader;

	// private final HiveClient hiveClient;
	private ObjectFactory<Object> hiveClientFactory;

	private @Value("${hive.table}")
	String tableName;

	@Autowired
	public HivePasswordRepository(ObjectFactory<Object> hiveClientFactory) {
		Assert.notNull(hiveClientFactory);
		this.hiveClientFactory = hiveClientFactory;
	}

	public long count() {

		/* this isn't compiling using mvn/javac and also fails at runtime in eclispe*/
		/*
		HiveClient hiveClient = hiveClientFactory.getObject();
		try {
			hiveClient.execute("select count(*) from " + tableName);
			return Long.parseLong(hiveClient.fetchOne());

			// checked exceptions
		} catch (HiveServerException ex) {
			throw translateExcpetion(ex);
		} catch (org.apache.thrift.TException tex) {
			throw translateExcpetion(tex);
		} finally {
			try {
				hiveClient.shutdown();
			} catch (org.apache.thrift.TException tex) {
				logger.debug("Unexpected exception on shutting down HiveClient", tex);
			}
		}
		*/
		return -1;
	}

	private RuntimeException translateExcpetion(Exception ex) {
		return new RuntimeException(ex);
	}

	/*
	 * @Override public void processPasswordFile(String inputFile) throws
	 * Exception { HiveUtils.run(createHiveClient(), scripts, true)
	 * //HiveScriptRunner.run(hiveClient,
	 * resourceLoader.getResource(inputFile)); }
	 */

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
