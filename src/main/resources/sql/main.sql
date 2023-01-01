drop table if exists users;
drop table if exists company;

create table company(
    id int primary key,
    name varchar(64) not null
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
