CREATE TABLE customer (
  id NUMBER(10),
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  email_address VARCHAR(255),
  PRIMARY KEY (id));
/
CREATE OR REPLACE TYPE customer_type
  AS OBJECT (id NUMBER(10), first_name VARCHAR(255), last_name VARCHAR(255), email_address VARCHAR(255));
/
CREATE OR REPLACE TYPE customer_tab_type AS TABLE OF customer_type;
/
CREATE OR REPLACE PROCEDURE add_customer (in_customer IN customer_type)
AS
BEGIN
  INSERT into customer (id, first_name, last_name, email_address) VALUES(in_customer.id, in_customer.first_name, in_customer.last_name , in_customer.email_address);
END;
/
CREATE OR REPLACE PROCEDURE save_customer (in_customer IN customer_type)
AS
  m_exists NUMBER;
BEGIN
  SELECT count(*) INTO m_exists FROM customer WHERE id = in_customer.id;
  IF m_exists > 0 THEN
    UPDATE customer SET first_name = in_customer.first_name, last_name = in_customer.last_name, email_address = in_customer.email_address WHERE id = in_customer.id;
  ELSE
    INSERT into customer (id, first_name, last_name, email_address) VALUES(in_customer.id, in_customer.first_name, in_customer.last_name , in_customer.email_address);
  END IF;
END;
/
CREATE OR REPLACE PROCEDURE get_customer (in_customer_id IN NUMBER, out_customer OUT customer_type)
AS
BEGIN
  SELECT customer_type(id, first_name, last_name, email_address) INTO out_customer FROM customer WHERE id = in_customer_id;
END;
/
CREATE OR REPLACE FUNCTION get_all_customer_types RETURN customer_tab_type
AS
  m_customer_tab customer_tab_type;
  cursor c_customer is
    select id, first_name, last_name, email_address from customer a;
BEGIN
  m_customer_tab := customer_tab_type();
  FOR r_customer IN c_customer loop
    m_customer_tab.extend;
    m_customer_tab(m_customer_tab.count) := customer_type(r_customer.id, r_customer.first_name, r_customer.last_name, r_customer.email_address);
  END LOOP;
  RETURN m_customer_tab;
END;
/
