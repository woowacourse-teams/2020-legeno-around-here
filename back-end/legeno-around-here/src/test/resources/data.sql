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
        '$2a$10$jDX7aPOqQ8Z2D9mNvka72OyUOOIfy6bJKq4cqL3RXCfiIt4wYSev.', null, now(), now()),
       (9, 'notices1@capzzang.co.kr', 'userName',
        '$2a$10$jDX7aPOqQ8Z2D9mNvka72OyUOOIfy6bJKq4cqL3RXCfiIt4wYSev.', null, now(), now()),
       (10, 'notices2@capzzang.co.kr', 'userName',
        '$2a$10$jDX7aPOqQ8Z2D9mNvka72OyUOOIfy6bJKq4cqL3RXCfiIt4wYSev.', null, now(), now());

INSERT INTO User(id, email, nickname, password, area_id, created_at, modified_at, deactivated_at)
VALUES (11, 'deactivated@test.com', 'daNickname',
        '$2a$10$jDX7aPOqQ8Z2D9mNvka72OyUOOIfy6bJKq4cqL3RXCfiIt4wYSev.', null, now(), now(), now());

INSERT INTO User_roles(user_id, roles)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER'),
       (3, 'ROLE_USER'),
       (4, 'ROLE_USER'),
       (5, 'ROLE_USER'),
       (6, 'ROLE_USER'),
       (7, 'ROLE_USER'),
       (8, 'ROLE_USER'),
       (9, 'ROLE_USER'),
       (10, 'ROLE_USER'),
       (11, 'ROLE_USER');

INSERT INTO Mail_auth(id, email, auth_number, created_at, modified_at)
VALUES (1, 'admin@test.com', 111111, now(), now()),
       (2, 'user@test.com', 111111, now(), now()),
       (3, 'another@test.com', 111111, now(), now()),
       (4, 'theother@test.com', 111111, now(), now()),
       (5, 'update@test.com', 111111, now(), now()),
       (6, 'a@capzzang.co.kr', 111111, now(), now()),
       (7, 'b@capzzang.co.kr', 111111, now(), now()),
       (8, 'c@capzzang.co.kr', 111111, now(), now()),
       (9, 'notices1@capzzang.co.kr', 111111, now(), now()),
       (10, 'notices2@capzzang.co.kr', 111111, now(), now());

INSERT INTO notification
(id, created_at, deleted_at, modified_at, comment_id, content, post_id, is_read, receiver_id,
 sector_id, user_id)
VALUES (1, now(), null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 9, null, null),
       (2, now() - 1, null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 9, null, null),
       (3, now() - 2, null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 9, null, null),
       (4, now() - 3, null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 9, null, null),
       (5, now() - 4, null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 9, null, null),
       (6, now() - 5, null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 9, null, null),
       (7, now() - 6, null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 9, null, null),
       (8, now() - 7, null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 9, null, null),
       (9, now() - 8, null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 9, null, null),
       (10, now() - 9, null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 9, null,
        null),
       (11, now(), null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 10, null, null),
       (12, now() - 1, null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 10, null,
        null),
       (13, now() - 2, null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 10, null,
        null),
       (14, now() - 3, null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 10, null,
        null),
       (15, now() - 4, null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 10, null,
        null),
       (16, now() - 5, null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 10, null,
        null),
       (17, now() - 6, null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 10, null,
        null),
       (18, now() - 7, null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 10, null,
        null),
       (19, now() - 8, null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 10, null,
        null),
       (20, now() - 9, null, now(), null, 'userName님, 가입을 진심으로 축하드립니다.', null, false, 10, null,
        null);

