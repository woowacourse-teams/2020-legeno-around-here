INSERT INTO Area(id, full_name, first_depth_name, second_depth_name, third_depth_name,
                 fourth_depth_name, used, created_at, modified_at)
SELECT id
     , full_name
     , first_depth_name
     , second_depth_name
     , third_depth_name
     , fourth_depth_name
     , used
     , NOW()
     , NOW()
FROM CSVRead('src/test/resources/area-seoul.csv');

INSERT INTO User(id, email, nickname, password, created_at, modified_at)
VALUES (1, 'admin@email.com', '어드민씨',
        '$2a$10$dEcia20EDmjecE7dlxmSJeb6pHk/cKixUxVdFDKfdABSovUxySFgK', now(), now());

INSERT INTO User_roles(user_id, roles)
VALUES (1, 'ROLE_ADMIN');

INSERT INTO Sector(id, name, description, state, reason,
                   creator_id, last_modifier_id, created_at, modified_at)
VALUES (1, '비싼 음식 냠냠쓰', '비싼 음식 냠냠 나야나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (2, '음식 빨리 챱챱쓰', '음식 빨리 챱챱해쒀여', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (3, '카트 존잘러', '가장 카트 잘함', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (4, '모바일 카트 존잘', '모바일 카트 가장 잘함', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (5, '썩은 사과', '썩은 사과 만난 썰', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (6, '월급 루팡', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now());