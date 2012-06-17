package com.oreilly.springdata.jdbc.querydsl.domain;

/**
 */
public class Address {
	private Integer id;
	private String street, city, country;

	public Address(String street, String city, String country) {
		this.street = street;
		this.city = city;
		this.country = country;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	@Override
	public String toString() {
		return street + ", " + city + " " + country ;
	}
}
