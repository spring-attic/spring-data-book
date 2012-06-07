create table product(id IDENTITY, name VARCHAR(255), description VARCHAR(255), price DECIMAL(9,2));
insert into product(name, description, price) values('Widget', 'A general widget', 25.99);
insert into product(name, description, price) values('Thing', 'Just a regular thing', 12.50);
