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
package com.oreilly.springdata.rest.order.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oreilly.springdata.rest.core.Customer;
import com.oreilly.springdata.rest.core.CustomerRepository;

/**
 * Manual controller implementation to simulate a controller working with Spring Data Hateoas {@link Resource}/
 * {@link Resources} objects.
 * 
 * @author Oliver Gierke
 */
@Controller
@RequestMapping("/mycustomer")
public class ManualCustomerController {

	private final CustomerRepository repository;

	/**
	 * Creates a new {@link ManualCustomerController} using the given {@link CustomerRepository}.
	 * 
	 * @param repository must not be {@literal null}.
	 */
	@Autowired
	public ManualCustomerController(CustomerRepository repository) {

		Assert.notNull(repository);
		this.repository = repository;
	}

	@RequestMapping
	public HttpEntity<Resources<Resource<Customer>>> showCustomers() {

		Resources<Resource<Customer>> resources = Resources.wrap(repository.findAll());
		return new ResponseEntity<Resources<Resource<Customer>>>(resources, HttpStatus.OK);
	}

	@RequestMapping("/{id}")
	public HttpEntity<? extends Resource<?>> showCustomer(@PathVariable("id") Customer customer) {

		return new ResponseEntity<Resource<Customer>>(new Resource<Customer>(customer), HttpStatus.OK);
	}
}
