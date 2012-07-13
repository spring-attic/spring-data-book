package org.springframework.data.mongodb.test;

import com.mongodb.Mongo;

import de.flapdoodle.embedmongo.MongoDBRuntime;
import de.flapdoodle.embedmongo.MongodExecutable;
import de.flapdoodle.embedmongo.MongodProcess;
import de.flapdoodle.embedmongo.config.MongodConfig;
import de.flapdoodle.embedmongo.distribution.Version;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 */
public class MongodExeFactoryBean implements FactoryBean<Mongo>, InitializingBean, DisposableBean {

	private MongodExecutable mongodExe;
	private MongodProcess mongod;

	private Mongo mongo;

	private int port = 12345;

	public MongodExeFactoryBean() {
	}

	public MongodExeFactoryBean(int port) {
		this.port = port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public Mongo getObject() throws Exception {
		return mongo;
	}

	@Override
	public Class<?> getObjectType() {
		return Mongo.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		mongodExe = MongoDBRuntime.getDefaultInstance().prepare(new MongodConfig(Version.V2_0, port, false));
		mongod = mongodExe.start();
		mongo = new Mongo("localhost", port);
	}

	@Override
	public void destroy() throws Exception {
		mongod.stop();
		mongodExe.cleanup();
	}
}
