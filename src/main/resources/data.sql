drop table if exists blog_search_entity CASCADE;
create table blog_search_entity (keyword varchar(300) not null, hit_count  bigint, primary key (keyword));
insert into blog_search_entity(keyword, hit_count) values ('test', 1);