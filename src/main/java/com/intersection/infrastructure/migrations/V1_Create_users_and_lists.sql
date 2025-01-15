CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE lists (
                       id UUID PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                       is_published BOOLEAN DEFAULT FALSE,
                       created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE list_items (
                    id UUID PRIMARY KEY,
                    list_id UUID NOT NULL REFERENCES lists(id) ON DELETE CASCADE,
                    content TEXT NOT NULL
);
