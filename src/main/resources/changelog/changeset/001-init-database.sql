create table if not exists users(
    id bigserial primary key,
    name varchar(128) not null,
    surname varchar(128) not null,
    birth_date date not null,
    email varchar(128) not null unique,
    active boolean not null default true,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);

create table if not exists payment_cards(
    id bigserial primary key,
    user_id bigint references users(id),
    number varchar(16) not null unique,
    holder varchar(128) not null,
    expiration_date date not null,
    active boolean not null default true,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);

create index if not exists idx_user_name_surname on users(name,surname);
create index if not exists idx_user_email on users(email);
create index if not exists idx_payment_card_user_id on payment_cards(user_id);
create index if not exists idx_payment_card_holder on payment_cards(holder);







