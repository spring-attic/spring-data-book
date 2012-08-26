package com.oreilly.springdata.hadoop.hive;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.service.HiveClient;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.hadoop.hive.HiveClientFactoryBean;
import org.springframework.data.hadoop.hive.HiveScriptRunner;
import org.springframework.util.Assert;

public class HiveTemplate implements HiveOperations, ApplicationContextAware {

	private static final Log log = LogFactory.getLog(HiveTemplate.class);

	private final HiveExceptionTranslator exceptionTranslator = new HiveExceptionTranslator();
	private String hiveClientName;

	private ApplicationContext context;

	public HiveTemplate(String hiveClientName) {
		Assert.notNull(hiveClientName,
				"hive client name name needs to be specified");
		this.hiveClientName = hiveClientName;
	}

	@Override
	public void executeScript(final String scriptResource) {

		final Resource resource = this.context.getResource(scriptResource);
		if (!resource.exists()) {
			throw new InvalidDataAccessApiUsageException(String.format(
					"Resource %s not found!", resource));
		}

		execute(new HiveClientCallback<Void>() {
			@Override
			public Void doInHive(HiveClient hiveClient)
					throws DataAccessException, Exception {
				HiveScriptRunner.run(hiveClient, resource);
				return null;
			}
		});
	}

	private HiveClientFactoryBean getNewHiveClientFactoryBean() {
		// TODO use different stategy
		HiveClientFactoryBean cfb = context.getBean("&" + hiveClientName,
				HiveClientFactoryBean.class);
		HiveClientFactoryBean newCfb = new HiveClientFactoryBean();
		newCfb.setHost(cfb.getHost());
		newCfb.setPort(cfb.getPort());
		newCfb.setTimeout(cfb.getTimeout());
		newCfb.afterPropertiesSet();
		return newCfb;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;

	}

	@Override
	public <T> T execute(HiveClientCallback<T> action) {
		Assert.notNull(action);
		HiveClientFactoryBean cfb = this.getNewHiveClientFactoryBean();
		HiveClient hiveClient = cfb.getObject();
		try {
			cfb.start();
			return action.doInHive(hiveClient);
		} catch (Exception e) {
			throw translateException(e);
		} finally {
			cfb.destroy();
		}
	}

	private RuntimeException translateException(Exception ex) {
		return exceptionTranslator.translateException(ex);
	}

	@Override
	public int queryForInt(String hql) throws DataAccessException {
		return execute(new HiveClientCallback<Integer>() {

			public Integer doInHive(HiveClient hiveClient)
					throws DataAccessException, Exception {
				// TODO Auto-generated method stub
				return 0;
			}
		});
	}

}
