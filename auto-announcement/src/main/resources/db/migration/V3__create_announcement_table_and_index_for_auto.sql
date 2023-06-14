CREATE TABLE announcement (
                              id SERIAL PRIMARY KEY,
                              name VARCHAR(200) NOT NULL,
                              price INTEGER NOT NULL,
                              description VARCHAR(500) NOT NULL,
                              user_uuid VARCHAR(50) NOT NULL,
                              auto_id INTEGER,
                              chat_id INTEGER[],
                              image_id VARCHAR(200)[]
);

CREATE INDEX announcement_auto_id_idx ON announcement (auto_id);