create DATABASE bank;

CREATE TABLE t_user (
    id SERIAL PRIMARY KEY,
    card_id int unique not null,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    create_time TIMESTAMP default CURRENT_TIMESTAMP NOT NULL,
    update_time TIMESTAMP default CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE t_card (
    id SERIAL PRIMARY KEY,
    card_number CHAR(16) UNIQUE NOT NULL,
    security_code CHAR(3) NOT NULL,
    amount decimal NOT NULL DEFAULT 0.0,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone CHAR(11) NOT NULL,
    create_time TIMESTAMP default CURRENT_TIMESTAMP NOT NULL,
    update_time TIMESTAMP default CURRENT_TIMESTAMP NOT NULL
);