SELECT count(1) FROM POST where size is null;

SELECT count(1) FROM POST where size is not null;

select title, size, url from post where size is not null;

explain select title, size, url from post where size is not null;
explain select title, size, url from post where size is not null and url = '11';
help;
explain update post set title = '1', content='1', size='1', releaseyear ='1', studio ='1', format ='1', duration ='1', video ='1', audio ='1', genres ='1', errmsg=null where url='1'
