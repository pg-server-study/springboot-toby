drop table if exists users;

create table users
(
    id       varchar(10) primary key,
    name     varchar(20) not null,
    password varchar(10) not null,
    level tinyint not null,
    login number not null,
    recommend number not null
)