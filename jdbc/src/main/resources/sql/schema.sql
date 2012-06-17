CREATE TABLE product (id IDENTITY, name VARCHAR(255), description VARCHAR(255), price DECIMAL(9,2));
CREATE TABLE customer (id IDENTITY, first_name VARCHAR(255), last_name VARCHAR(255), email_address VARCHAR(255));
CREATE TABLE address (id IDENTITY PRIMARY KEY, customer_id INTEGER CONSTRAINT address_customer_ref FOREIGN KEY REFERENCES customer (id), street VARCHAR(255), city VARCHAR(255), country VARCHAR(255));
