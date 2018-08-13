
create table csdn_users(
    id int auto_increment primary key,
    username varchar(128) not null,
    password varchar(128) not null,
    email varchar(128)
);
