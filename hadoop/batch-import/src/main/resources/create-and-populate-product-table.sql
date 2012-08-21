drop table if exists product;

create table product (
	id varchar(255) not null,
	name varchar(255) not null,
	description varchar(255) not null,
	price float not null,
	primary key (id)
);

insert into product (id,name,description,price) values('PR....210','BlackBerry 8100 Pearl','',124.60);
insert into product (id,name,description,price) values('PR....211','Sony Ericsson W810i','',139.45);
insert into product (id,name,description,price) values('PR....212','Samsung MM-A900M Ace','',97.80);
insert into product (id,name,description,price) values('PR....213','Toshiba M285-E 14','',166.20);
insert into product (id,name,description,price) values('PR....214','Nokia 2610 Phone','',145.50);
insert into product (id,name,description,price) values('PR....215','CN Clogs Beach/Garden Clog','',190.70);
insert into product (id,name,description,price) values('PR....216','AT&T 8525 PDA','',289.20);
insert into product (id,name,description,price) values('PR....217','Canon Digital Rebel XT 8MP Digital SLR Camera','',13.70);