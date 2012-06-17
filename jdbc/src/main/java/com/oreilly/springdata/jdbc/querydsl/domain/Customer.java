package com.oreilly.springdata.jdbc.querydsl.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 */
public class Customer {

	private Integer id;
	private String firstName;
	private String lastName;
	private EmailAddress emailAddress;
	private Set<Address> addresses = new HashSet<Address>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public EmailAddress getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(EmailAddress emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Set<Address> getAddresses() {
		return Collections.unmodifiableSet(addresses);
	}

	public void addAddress(Address address) {
		this.addresses.add(address);
	}

	public void clearAddresses() {
		this.addresses.clear();
	}

	@Override
	public String toString() {
		return "Customer: [" + id + "] " + firstName + " " + lastName + " " + emailAddress + " " + addresses;
	}
}
