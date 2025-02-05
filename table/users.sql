create table users
(
    user_id         bigserial
        constraint "User_pkey"
            primary key,
    first_name      text not null,
    last_name       text not null,
    password_hash   text not null,
    email           text not null,
    birthdate       date,
    profile_picture text,
    user_name       text not null,
    user_role       varchar(255)
);

alter table users
    owner to postgres;

