INSERT INTO Area(id, full_name, first_depth_name, second_depth_name, third_depth_name,
                 fourth_depth_name, created_at, modified_at)
SELECT id
     , full_name
     , first_depth_name
     , second_depth_name
     , third_depth_name
     , fourth_depth_name
     , NOW()
     , NOW()
FROM CSVRead('src/test/resources/area.csv');

INSERT INTO User(id, email, nickname, password, area_id, created_at, modified_at)
VALUES (1, 'admin@test.com', 'adminName',
        '$2a$10$3tWu7vV57AaV4m96qwTkieQbY8R5TWhDE401ff7XGlSmRC/ItL0F.', null, now(), now()),
       (2, 'user@test.com', 'userName',
        '$2a$10$jDX7aPOqQ8Z2D9mNvka72OyUOOIfy6bJKq4cqL3RXCfiIt4wYSev.', null, now(), now()),
       (3, 'another@test.com', 'userName',
        '$2a$10$jDX7aPOqQ8Z2D9mNvka72OyUOOIfy6bJKq4cqL3RXCfiIt4wYSev.', 1, now(), now()),
       (4, 'theother@test.com', 'userName',
        '$2a$10$jDX7aPOqQ8Z2D9mNvka72OyUOOIfy6bJKq4cqL3RXCfiIt4wYSev.', null, now(), now()),
       (5, 'update@test.com', 'userName',
        '$2a$10$jDX7aPOqQ8Z2D9mNvka72OyUOOIfy6bJKq4cqL3RXCfiIt4wYSev.', null, now(), now()),
       (6, 'a@capzzang.co.kr', 'userName',
        '$2a$10$jDX7aPOqQ8Z2D9mNvka72OyUOOIfy6bJKq4cqL3RXCfiIt4wYSev.', null, now(), now()),
       (7, 'b@capzzang.co.kr', 'userName',
        '$2a$10$jDX7aPOqQ8Z2D9mNvka72OyUOOIfy6bJKq4cqL3RXCfiIt4wYSev.', null, now(), now()),
       (8, 'c@capzzang.co.kr', 'userName',
        '$2a$10$jDX7aPOqQ8Z2D9mNvka72OyUOOIfy6bJKq4cqL3RXCfiIt4wYSev.', null, now(), now());

INSERT INTO User_roles(user_id, roles)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER'),
       (3, 'ROLE_USER'),
       (4, 'ROLE_USER'),
       (5, 'ROLE_USER'),
       (6, 'ROLE_USER'),
       (7, 'ROLE_USER'),
       (8, 'ROLE_USER');

INSERT INTO Mail_auth(id, email, auth_number, created_at, modified_at)
VALUES (1, 'admin@test.com', 111111, now(), now()),
       (2, 'user@test.com', 111111, now(), now()),
       (3, 'another@test.com', 111111, now(), now()),
       (4, 'theother@test.com', 111111, now(), now()),
       (5, 'update@test.com', 111111, now(), now()),
       (6, 'a@capzzang.co.kr', 111111, now(), now()),
       (7, 'b@capzzang.co.kr', 111111, now(), now()),
       (8, 'c@capzzang.co.kr', 111111, now(), now());
