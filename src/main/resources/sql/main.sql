drop table if exists users cascade ;
drop table if exists company cascade ;
drop table if exists profile cascade ;
drop table if exists chats cascade ;
drop table if exists users_chat cascade ;

create table company(
    id int primary key,
    name varchar(64) not null unique
);

create sequence company_id_seq
owned by company.id;

create table public.users
(
    id bigint primary key,
    user_name  varchar not null unique,
    first_name varchar not null,
    last_name  varchar not null,
    birth_date date    not null,
    age        integer,
    role varchar,
    company_id int references company(id)
);

create sequence users_id_seq
OWNED BY users.id;


create table profile(
    id bigint primary key,
    user_id bigint references users(id) not null unique,
    street varchar(128) not null,
    language varchar(2) not null
);

create sequence profile_id_seq
owned by profile.id;

create table chats(
    id bigint primary key,
    name varchar not null unique
);

create sequence chats_id_seq
owned by chats.id;


create table users_chat(
    user_id bigint references users(id) not null  unique ,
    chat_id bigint references chats(id) not null unique ,
    primary key (user_id, chat_id)
);
