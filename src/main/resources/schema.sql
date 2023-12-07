CREATE SEQUENCE IF NOT EXISTS users_seq MINVALUE 1 START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT DEFAULT NEXT VALUE FOR users_seq NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
        CONSTRAINT pk_user PRIMARY KEY (id),
        CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(512) NOT NULL,
    is_available boolean,
    owner_id int NOT NULL,
    request_id int,
        CONSTRAINT pk_item PRIMARY KEY (id),
        CONSTRAINT users_items_owner_fk
            FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    item_id int,
    booker_id int,
    status VARCHAR(255) NOT NULL,
        CONSTRAINT pk_booking PRIMARY KEY (id),
         CONSTRAINT uq_bookings_ids_status UNIQUE (start_date, end_date, item_id, booker_id, status),
        CONSTRAINT items_booking_item_id_fk
            FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE,
        CONSTRAINT users_bookings_booker_id_fk
            FOREIGN KEY (booker_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description VARCHAR(512) NOT NULL,
    requester_id int,
        CONSTRAINT pk_request PRIMARY KEY (id),
        CONSTRAINT users_requests_requester_id_fk
            FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR(512) NOT NULL,
    item_id int,
    author_id int,
    created TIMESTAMP WITHOUT TIME ZONE,
        CONSTRAINT pk_comment PRIMARY KEY (id),
        CONSTRAINT items_comments_item_id_fk
            FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE,
        CONSTRAINT users_comments_author_id_fk
            FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE
);