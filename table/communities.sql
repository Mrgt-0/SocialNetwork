create table communities
(
    community_id   bigserial
        constraint "communities._pkey"
            primary key,
    community_name text      not null,
    description    text      not null,
    created_at     timestamp not null,
    admin_user_id  bigint    not null
        constraint admin_user_id
            references users,
    id             bigserial
);

alter table communities
    owner to postgres;

