create DATABASE bank;

CREATE TABLE t_user (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255),
                       name VARCHAR(255),
                       phone CHAR(11),
                       createTime TIMESTAMP NOT NULL,
                       updateTime TIMESTAMP NOT NULL
);