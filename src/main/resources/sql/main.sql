drop table if exists users;
create table public.users
(
    id bigint primary key,
    user_name  varchar not null unique,
    first_name varchar not null,
    last_name  varchar not null,
    birth_date date    not null,
    age        integer,
    role varchar
);

create sequence users_id_seq
OWNED BY users.id;
