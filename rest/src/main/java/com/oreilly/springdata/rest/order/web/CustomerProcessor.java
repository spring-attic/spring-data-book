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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import com.oreilly.springdata.rest.core.Customer;

/**
 * {@link ResourceProcessor} to enrich {@link Customer} {@link Resource}s to link to the manually implemented
 * {@link CustomerOrdersController}.
 * 
 * @author Oliver Gierke
 */
@Component
public class CustomerProcessor implements ResourceProcessor<Resource<Customer>> {

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.hateoas.ResourceProcessor#enrich(org.springframework.hateoas.ResourceSupport)
	 */
	@Override
	public Resource<Customer> process(Resource<Customer> resource) {

		Customer customer = resource.getContent();

		ControllerLinkBuilder builder = linkTo(CustomerOrdersController.class, customer.getId());
		resource.add(builder.slash("orders").withRel("customer.orders"));
		return resource;
	}
}
