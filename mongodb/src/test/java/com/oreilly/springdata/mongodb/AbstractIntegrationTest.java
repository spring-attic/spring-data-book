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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;

/**
 * @author Oliver Gierke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
public abstract class AbstractIntegrationTest {

	@Autowired
	Mongo mongo;

	@Before
	public void setUp() {

		DB database = mongo.getDB("e-store");

		// Customers

		DBCollection collection = database.getCollection("customer");
		collection.drop();

		DBObject dave = new BasicDBObject("firstname", "Dave");
		dave.put("lastname", "Matthews");
		dave.put("email", "dave@dmband.com");

		WriteResult result = collection.insert(dave);

		// Products

		DBCollection products = database.getCollection("product");
		products.drop();

		DBObject iPad = new BasicDBObject("name", "iPad");
		iPad.put("description", "Apple tablet device");
		iPad.put("price", 499.0);

		DBObject macBook = new BasicDBObject("name", "MacBook Pro");
		macBook.put("description", "Apple notebook");
		macBook.put("price", 1299.0);

		products.insert(iPad, macBook);

		// Orders

		// insert into Orders (id, customer_id) values (1, 1);
		// insert into LineItem (id, product_id, amount, order_id) values (1, 1, 2, 1);
		// insert into LineItem (id, product_id, amount, order_id) values (2, 2, 1, 1);

		DBCollection orders = database.getCollection("order");
		orders.drop();

		// Line items

		DBObject iPadLineItem = new BasicDBObject("product", iPad);
		iPadLineItem.put("amount", 2);

		DBObject macBookLineItem = new BasicDBObject("product", macBook);
		macBookLineItem.put("amount", 1);

		BasicDBList lineItems = new BasicDBList();
		lineItems.add(iPadLineItem);
		lineItems.add(macBookLineItem);

		DBObject order = new BasicDBObject("customer", new DBRef(database, "customer", dave.get("_id")));
		order.put("lineItems", lineItems);

		orders.insert(order);
	}
}
