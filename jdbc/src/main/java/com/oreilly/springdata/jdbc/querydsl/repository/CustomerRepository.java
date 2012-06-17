package com.oreilly.springdata.jdbc.querydsl.repository;

import com.oreilly.springdata.jdbc.querydsl.domain.Customer;
import com.oreilly.springdata.jdbc.querydsl.domain.EmailAddress;

import java.util.List;

/**
 * @author Thomas Risberg
 */
public interface CustomerRepository {

	Customer findOne(Integer id);

	List<Customer> findAll();

	void save(Customer customer);

	void delete(Customer customer);

	Customer findByEmailAddress(EmailAddress emailAddress);
}