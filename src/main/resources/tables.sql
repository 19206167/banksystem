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
    iban VARCHAR(34) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20),
    accountType VARCHAR(20),
    SecurityCode VARCHAR(3) NOT NULL,
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    create_time TIMESTAMP default CURRENT_TIMESTAMP NOT NULL,
    update_time TIMESTAMP default CURRENT_TIMESTAMP NOT NULL
);