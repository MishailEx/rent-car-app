CREATE TABLE category
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

INSERT INTO category (name)
VALUES ('Седан'),
       ('Хэтчбек'),
       ('Универсал'),
       ('Внедорожник'),
       ('Пикап');

CREATE TABLE mark
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

INSERT INTO mark (name)
VALUES ('BMW'),
       ('Audi'),
       ('Mercedes-Benz'),
       ('Lexus'),
       ('Honda'),
       ('Toyota'),
       ('Volkswagen'),
       ('Ford'),
       ('Subaru'),
       ('Mitsubishi'),
       ('Renault'),
       ('Jeep'),
       ('Land Rover'),
       ('Range Rover'),
       ('Ford'),
       ('Nissan'),
       ('Toyota'),
       ('Chevrolet'),
       ('Ford'),
       ('GMC');

CREATE TABLE model
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    category_id INTEGER      NOT NULL,
    mark_id     INTEGER      NOT NULL
);

INSERT INTO model (name, mark_id, category_id)
VALUES ('320i', 1, 1),
       ('520i', 1, 1),
       ('A4', 2, 1),
       ('A6', 2, 1),
       ('C180', 3, 1),
       ('C200', 3, 1),
       ('ES 350', 4, 1),
       ('IS 300', 4, 1),
       ('Accord', 5, 2),
       ('Civic', 5, 2),
       ('Corolla', 6, 2),
       ('Camry', 6, 2),
       ('Golf', 7, 2),
       ('Jetta', 7, 2),
       ('Focus', 8, 2),
       ('Fiesta', 8, 2),
       ('Impreza', 9, 3),
       ('Outback', 9, 3),
       ('Lancer', 10, 3),
       ('Pajero', 10, 3),
       ('Logan', 11, 3),
       ('Duster', 11, 3),
       ('Cherokee', 12, 4),
       ('Grand Cherokee', 12, 4),
       ('Range Rover Sport', 13, 4),
       ('Defender', 13, 4),
       ('F-150', 14, 5),
       ('Silverado', 15, 5),
       ('Ranger', 16, 5),
       ('Frontier', 17, 5),
       ('Tundra', 18, 5);