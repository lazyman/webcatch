SELECT count(1) FROM POST where size is null;

SELECT count(1) FROM POST where size is not null;

select title, size, url from post where size is not null;

explain select title, size, url from post where size is not null;
explain select title, size, url from post where size is not null and url = '11';
help;
explain update post set title = '1', content='1', size='1', releaseyear ='1', studio ='1', format ='1', duration ='1', video ='1', audio ='1', genres ='1', errmsg=null where url='1'


select  replace(replace(size, 'GB',''), 'MB', '') ,substr(size, -3,5) from post where size is not null ;
update post set filesize=replace(replace(size, 'GB',''), 'MB', ''), unit = substr(size, -3,5) where size is not null and length(size) <15;


SELECT p.TITLE, c.catename, p.SIZE, p.URL FROM POST p, catemap c 
where p.url = c.posturl and filesize < 110 and unit = 'MB'
and c.catename not in ('Comments (0)', 'Comments Off', 'Insex', 'All BDSM')
order by filesize desc