use gameDB;

create table user(
    id mediumint unsigned auto_increment not null,
    email char(128) not null unique,
    login char(64) not null,
    password char(64) not null,
    score int not null default '0',
    isSuperUser tinyint unsigned not null default '0',
    primary key (id)
) engine=InnoDB default charset=cp1251;
