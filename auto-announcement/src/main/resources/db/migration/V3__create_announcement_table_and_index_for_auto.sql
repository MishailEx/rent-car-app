CREATE TABLE announcement (
                              id SERIAL PRIMARY KEY,
                              name VARCHAR(200) NOT NULL,
                              price INTEGER NOT NULL,
                              description VARCHAR(500) NOT NULL,
                              uuid VARCHAR(50)NOT NULL,
                              auto_id INTEGER
);

CREATE INDEX announcement_auto_id_idx ON announcement (auto_id);