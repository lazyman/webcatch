drop table post;
create table post(
	id varchar(10) null,
   title varchar(100) null,
   url varchar(1000) null,
   content varchar(4000) null,
   size varchar(10) null,
   releaseYear varchar(10) null,
	studio varchar(300) null,
   format varchar(100) null,
   duration varchar(1000) null,
   video varchar(4000) null,
   audio varchar(10) null,
   genres varchar(10) null
   
);

alter table post alter column errmsg text;

alter table post alter column genres varchar(300);
alter table post alter column title varchar(300);

create index post_url on post(url);