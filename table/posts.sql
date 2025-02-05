create table posts
(
    post_id    bigserial
        primary key,
    created_at timestamp(6) not null,
    image      text,
    text       text         not null,
    updated_at timestamp(6) not null,
    user_id    bigint
        constraint fk3m7wp2fcoamknkfua1sav2li6
            references users,
    id         bigserial
);

alter table posts
    owner to postgres;

