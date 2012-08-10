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
package com.oreilly.springdata.gemfire.customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.stereotype.Repository;

import com.gemstone.gemfire.cache.query.SelectResults;

/**
 * A Data access object using a native Gemfire interfaces for data access.
 * @author David Turanski
 *
 */
@Repository
public class CustomerDao {
	@Autowired
	GemfireTemplate template;
	
	/**
	 * Returns all objects in the region.
	 * Not advisable for very large data sets.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> findAll() {
		return new ArrayList<Customer>((Collection<? extends Customer>)template.getRegion().values());
	}

	/**
	 * Save a customer
	 * @param customer
	 */
	public void save(Customer customer) {
		template.put(customer.getId(), customer);
	}

	/**
	 * Find a customer by last name
	 * @param lastname
	 * @return
	 */
	public List<Customer> findByLastname(String lastname) {
		String queryString = "lastname = '" + lastname + "'";
		SelectResults<Customer> results = template.query(queryString);
		return results.asList();
	}

	/**
	 * Delete a customer
	 * @param customer
	 */
	public void delete(Customer customer) {
		template.remove(customer.getId());
	}
}
