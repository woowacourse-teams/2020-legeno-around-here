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
       (6, '월급 루팡', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (7, '월급 루팡2', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (8, '월급 루팡3', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (9, '월급 루팡4', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (10, '월급 루팡6', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (11, '월급 루팡7', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (12, '월급 루팡8', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (13, '월급 루팡9', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (14, '월급 루팡10', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (15, '월급 루팡11', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (16, '월급 루팡12', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (17, '월급 루팡13', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (18, '월급 루팡14', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (19, '월급 루팡15', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (20, '월급 루팡16', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (21, '월급 루팡17', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (22, '월급 루팡18', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (23, '월급 루팡19', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (24, '월급 루팡20', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now());