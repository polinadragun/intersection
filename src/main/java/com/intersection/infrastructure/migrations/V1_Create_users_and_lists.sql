CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE lists (
                       id SERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                       is_published BOOLEAN DEFAULT FALSE,
                       created_at TIMESTAMP DEFAULT NOW()
);
