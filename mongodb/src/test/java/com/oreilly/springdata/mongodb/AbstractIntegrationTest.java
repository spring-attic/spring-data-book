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

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.CamelCaseAbbreviatingFieldNamingStrategy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.Mongo;

/**
 * Base class for integration tests adding some sample data through the MongoDB Java driver.
 * 
 * @author Oliver Gierke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
public abstract class AbstractIntegrationTest {

	@Autowired Mongo mongo;

	/**
	 * Setting up sample data according to the {@link CamelCaseAbbreviatingFieldNamingStrategy} configured by returning
	 * {@literal true} from {@link ApplicationConfig#abbreviateFieldNames()}. Use custom field names in cases the mapping
	 * manually configures them.
	 */
	@Before
	public void setUp() {

		DB database = mongo.getDB("e-store");

		// Customers

		DBCollection customers = database.getCollection("customer");
		customers.remove(new BasicDBObject());

		BasicDBObject address = new BasicDBObject();
		address.put("city", "New York");
		address.put("s", "Broadway");
		address.put("c", "United States");

		BasicDBList addresses = new BasicDBList();
		addresses.add(address);

		DBObject dave = new BasicDBObject("f", "Dave");
		dave.put("l", "Matthews");
		dave.put("email", "dave@dmband.com");
		dave.put("a", addresses);

		customers.insert(dave);

		// Products

		DBCollection products = database.getCollection("product");
		products.drop();

		DBObject iPad = new BasicDBObject("n", "iPad");
		iPad.put("d", "Apple tablet device");
		iPad.put("p", 499.0);
		iPad.put("a", new BasicDBObject("connector", "plug"));

		DBObject macBook = new BasicDBObject("n", "MacBook Pro");
		macBook.put("d", "Apple notebook");
		macBook.put("p", 1299.0);

		BasicDBObject dock = new BasicDBObject("n", "Dock");
		dock.put("d", "Dock for iPhone/iPad");
		dock.put("p", 49.0);
		dock.put("a", new BasicDBObject("connector", "plug"));

		products.insert(iPad, macBook, dock);

		// Orders

		DBCollection orders = database.getCollection("order");
		orders.drop();

		// Line items

		DBObject iPadLineItem = new BasicDBObject("p", iPad);
		iPadLineItem.put("a", 2);

		DBObject macBookLineItem = new BasicDBObject("p", macBook);
		macBookLineItem.put("a", 1);

		BasicDBList lineItems = new BasicDBList();
		lineItems.add(iPadLineItem);
		lineItems.add(macBookLineItem);

		DBObject order = new BasicDBObject("c", new DBRef(database, "customer", dave.get("_id")));
		order.put("li", lineItems);
		order.put("sa", address);

		orders.insert(order);
	}
}
