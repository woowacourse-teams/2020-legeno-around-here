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

INSERT INTO User(id, email, nickname, password, created_at, modified_at)
VALUES (1, 'admin@email.com', '어드민씨',
        '$2a$10$dEcia20EDmjecE7dlxmSJeb6pHk/cKixUxVdFDKfdABSovUxySFgK', now(), now()),
       (2, 'user@email.com', '유저씨',
        '$2a$10$dEcia20EDmjecE7dlxmSJeb6pHk/cKixUxVdFDKfdABSovUxySFgK', now(), now());

INSERT INTO User_roles(user_id, roles)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER');

INSERT INTO Sector(id, name, description, state, reason,
                   creator_id, last_modifier_id, created_at, modified_at)
VALUES (1, '비싼 음식 냠냠쓰', '비싼 음식 냠냠 나야나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (2, '음식 빨리 챱챱쓰', '음식 빨리 챱챱해쒀여', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (3, '카트 존잘러', '가장 카트 잘함', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (4, '모바일 카트 존잘', '모바일 카트 가장 잘함', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (5, '썩은 사과', '썩은 사과 만난 썰', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (6, '월급 루팡', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now());

INSERT INTO Post(id, writing, state, area_id, sector_id,
                 creator_id, created_at, modified_at)
VALUES (1, '탕수육 먹었서요', 'PUBLISHED', 1, 1, 2, now(), now()),
       (2, '짬뽕 먹었서요', 'PUBLISHED', 1, 1, 2, now(), now()),
       (3, '짜장면 먹었서요', 'PUBLISHED', 1, 1, 2, now(), now()),
       (4, '울면 먹었서요', 'PUBLISHED', 1, 1, 2, now(), now()),
       (5, '빨리 먹었서요', 'PUBLISHED', 1, 2, 2, now(), now()),
       (6, '엄청 빨리 먹었서요', 'PUBLISHED', 1, 2, 2, now(), now()),
       (7, '진짜 빨리 먹었서요', 'PUBLISHED', 1, 2, 2, now(), now()),
       (8, '카트 잘해여', 'PUBLISHED', 1, 3, 2, now(), now());
