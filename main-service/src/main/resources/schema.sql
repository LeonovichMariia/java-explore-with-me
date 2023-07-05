DROP TABLE IF EXISTS users, categories, compilations, events, requests CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(254) UNIQUE NOT NULL,
    name VARCHAR(250) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN DEFAULT FALSE,
    title VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation VARCHAR(2000),
    confirmed_requests INTEGER,
    category_id BIGINT REFERENCES categories(id) ON DELETE RESTRICT,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description VARCHAR(7000),
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_id BIGINT REFERENCES users(id),
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL,
    paid BOOLEAN DEFAULT FALSE,
    participant_limit INT DEFAULT 0,
    published_on TIMESTAMP WITHOUT TIME ZONE ,
    request_moderation BOOLEAN DEFAULT TRUE,
    state VARCHAR(255),
    title VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS compilation_event (
    event_id BIGINT REFERENCES events(id),
    compilation_id BIGINT REFERENCES compilations(id),
    CONSTRAINT compilation_event_pk PRIMARY KEY(event_id, compilation_id)
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id BIGINT REFERENCES events(id) ON DELETE CASCADE,
    requester_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(12) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);