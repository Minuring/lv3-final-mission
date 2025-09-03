create table gym
(
    id     binary(16) not null,
    detail varchar(255),
    name   varchar(255),
    street varchar(255),
    primary key (id)
);

create table member
(
    id       varchar(255) not null,
    name     varchar(255),
    password varchar(255),
    role     tinyint      not null default 0,
    primary key (id)
);

create table booking
(
    id        binary(16) not null,
    date      date,
    gym_id    binary(16),
    member_id varchar(255),
    primary key (id),
    constraint fk_booking_gym foreign key (gym_id) references gym (id),
    constraint fk_booking_member foreign key (member_id) references member (id)
);
