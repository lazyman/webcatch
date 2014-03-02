create table catemap(
	id varchar(10) null,
   catename varchar(100) null,
   posturl varchar(300) null,
   cateurl varchar(300) null
   
);

create index catemap_idx on catemap(posturl, cateurl);

insert into catemap(catename, posturl, cateurl) values(?, ?, ?)

SELECT catename, count(1) FROM CATEMAP group by catename£»