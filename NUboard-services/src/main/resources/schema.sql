-- Create locations table
CREATE TABLE IF NOT EXISTS locations (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Create colleges table
CREATE TABLE IF NOT EXISTS colleges (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    program VARCHAR(255) NOT NULL,
    email VARCHAR(320) NOT NULL UNIQUE,
    location_id BIGINT NOT NULL,
    college_id BIGINT NOT NULL,
    FOREIGN KEY (location_id) REFERENCES locations(id),
    FOREIGN KEY (college_id) REFERENCES colleges(id)
);

-- Create event table
CREATE TABLE IF NOT EXISTS event (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1024),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    location BIGINT NOT NULL,
    address VARCHAR(255) NOT NULL,
    creator_id BIGINT NOT NULL,
    organizer_type VARCHAR(20) NOT NULL,
    FOREIGN KEY (location) REFERENCES locations(id)
);

-- Create event_registration table
CREATE TABLE IF NOT EXISTS event_registration (
    id BIGINT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (event_id) REFERENCES event(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE(event_id, user_id)
); 