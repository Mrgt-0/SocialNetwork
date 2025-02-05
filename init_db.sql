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
create table messages
(
    message_id        bigserial
        primary key,
    created_at        timestamp(6) not null,
    updated_at        timestamp(6) not null,
    text              text         not null,
    recipient_user_id bigint
        constraint fk5t39pvpoewf0qx2680p09yjil
            references users,
    sender_user_id    bigint
        constraint fkk4mpqp6gfuaelpcamqv01brkr
            references users
);
create table messages
(
    message_id        bigserial
        primary key,
    created_at        timestamp(6) not null,
    updated_at        timestamp(6) not null,
    text              text         not null,
    recipient_user_id bigint
        constraint fk5t39pvpoewf0qx2680p09yjil
            references users,
    sender_user_id    bigint
        constraint fkk4mpqp6gfuaelpcamqv01brkr
            references users
);
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

create table user_roles
(
    user_id   bigint       not null
        references users
            on delete cascade,
    user_role varchar(255) not null,
    role      varchar(255),
    primary key (user_id, user_role)
);

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

alter table communities
    owner to postgres;

alter table community_members
    owner to postgres;

alter table friends
    owner to postgres;

alter table messages
    owner to postgres;

alter table posts
    owner to postgres;

alter table user_roles
    owner to postgres;

alter table users
    owner to postgres;