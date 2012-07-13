/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oreilly.springdata.mongodb;

import com.mongodb.Mongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.test.MongodExeFactoryBean;

import java.util.List;

/**
 * Spring JavaConfig configuration class to setup a Spring container and infrastructure components like a
 * {@link javax.sql.DataSource}, and a {@link org.springframework.transaction.PlatformTransactionManager}.
 *
 * @author Oliver Gierke
 */
@Configuration
@ComponentScan(basePackageClasses = TestConfig.class, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class)})
@EnableMongoRepositories
class TestConfig extends AbstractMongoConfiguration {

	@Autowired
	private List<Converter<?, ?>> converters;

	private MongodExeFactoryBean mongoFactoryBean = new MongodExeFactoryBean(12347);

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#getDatabaseName()
	 */
	@Override
	protected String getDatabaseName() {
		return "e-store";
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#mongo()
	 */
	@Override
	public Mongo mongo() throws Exception {
		return mongoFactory().getObject();
	}

	@Bean
	public MongodExeFactoryBean mongoFactory() throws Exception {
		return mongoFactoryBean;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#getMappingBasePackage()
	 */
	@Override
	protected String getMappingBasePackage() {
		return getClass().getPackage().getName();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#customConversions()
	 */
	@Override
	public CustomConversions customConversions() {
		return new CustomConversions(converters);
	}
}
