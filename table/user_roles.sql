create table user_roles
(
    user_id   bigint       not null
        references users
            on delete cascade,
    user_role varchar(255) not null,
    role      varchar(255),
    primary key (user_id, user_role)
);

alter table user_roles
    owner to postgres;

