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
package com.oreilly.springdata.neo4j.order;

import com.oreilly.springdata.neo4j.AbstractIntegrationTest;
import com.oreilly.springdata.neo4j.core.*;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.neo4j.helpers.collection.IteratorUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.oreilly.springdata.neo4j.core.CoreMatchers.named;
import static com.oreilly.springdata.neo4j.core.CoreMatchers.with;
import static com.oreilly.springdata.neo4j.order.OrderMatchers.LineItem;
import static com.oreilly.springdata.neo4j.order.OrderMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class OrderRepositoryIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	OrderRepository repository;

	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	ProductRepository productRepository;

	@Test
	public void createOrder() {

		Customer dave = customerRepository.findByEmailAddress(new EmailAddress("dave@dmband.com").getEmail());
		Product iPad = productRepository.findByPropertyValue("name", "iPad");

		Order order = new Order(dave);
		order.add(new LineItem(order,iPad));

		order = repository.save(order);
		assertThat(order.getId(), is(notNullValue()));
	}

	@Test
	public void readOrder() {

        final EmailAddress email = new EmailAddress("dave@dmband.com");
        Customer dave = customerRepository.findByEmailAddress(email.getEmail());
		// TODO JIRA List<Order> orders = repository.findByCustomer(dave);
        List<Order> orders = repository.findByCustomerEmailAddress(email.getEmail());

		assertThat(orders, hasSize(1));

        final Set<LineItem> lineItems = orders.get(0).getLineItems();
        assertThat(lineItems, hasSize(2));
        final Iterator<LineItem> it = lineItems.iterator();

        final LineItem firstItem = it.next();
        assertThat(firstItem.getProduct(),is(iPad));
        assertThat(firstItem.getAmount(), is(2));

        final LineItem secondItem = it.next();
        assertThat(secondItem.getProduct(),is(mbp));
        assertThat(secondItem.getAmount(), is(1));
	}
}
