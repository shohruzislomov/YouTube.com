CREATE TABLE tag (
                     id SERIAL PRIMARY KEY,
                     name VARCHAR(255) NOT NULL,
                     created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);