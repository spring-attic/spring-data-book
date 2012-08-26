drop table passwords;
create table passwords (user string, passwd string, uid int, gid int, userinfo string, home string, shell string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ':' LINES TERMINATED BY '10';
load data local inpath '/etc/passwd' into table passwords;

drop table grpshell;
create table grpshell (shell string, count int);
INSERT OVERWRITE TABLE grpshell SELECT p.shell, count(*) FROM passwords p GROUP BY p.shell;

