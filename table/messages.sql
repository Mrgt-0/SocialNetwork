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

alter table messages
    owner to postgres;

