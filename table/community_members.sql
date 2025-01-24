create table community_members
(
    community_member_id bigserial
        primary key,
    community_id        bigint    not null
        constraint community_id
            references communities,
    user_id             bigint    not null
        constraint user_id
            references users,
    joined_at           timestamp not null,
    id                  bigserial
);

alter table community_members
    owner to postgres;

