create table friends
(
    id         bigserial
        primary key,
    user_id    bigint    not null
        references users,
    friend_id  bigint    not null
        references users,
    created_at timestamp not null
);

alter table friends
    owner to postgres;

