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
VALUES (1, 'admin@test.com', 'adminName',
        '$2a$10$3tWu7vV57AaV4m96qwTkieQbY8R5TWhDE401ff7XGlSmRC/ItL0F.', now(), now()),
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
       (6, '월급 루팡', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (7, '월급 루팡2', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (8, '월급 루팡3', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (9, '월급 루팡4', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (10, '월급 루팡6', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (11, '월급 루팡7', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (12, '월급 루팡8', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (13, '월급 루팡9', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (14, '월급 루팡10', '이 구역에 월급 루팡은 바로 나', 'PUBLISHED', 'Mock 데이터', 1, 1, now(), now()),
       (15, '월급 루팡11', '이 구역에 월급 루팡은 바로 나', 'APPROVED', 'Mock 데이터', 2, 1, now(), now()),
       (16, '월급 루팡12', '이 구역에 월급 루팡은 바로 나', 'APPROVED', 'Mock 데이터', 2, 1, now(), now()),
       (17, '월급 루팡13', '이 구역에 월급 루팡은 바로 나', 'APPROVED', 'Mock 데이터', 2, 1, now(), now()),
       (18, '월급 루팡14', '이 구역에 월급 루팡은 바로 나', 'APPROVED', 'Mock 데이터', 2, 1, now(), now()),
       (19, '월급 루팡15', '이 구역에 월급 루팡은 바로 나', 'APPROVED', 'Mock 데이터', 2, 1, now(), now()),
       (20, '월급 루팡16', '이 구역에 월급 루팡은 바로 나', 'APPROVED', 'Mock 데이터', 2, 1, now(), now()),
       (21, '월급 루팡17', '이 구역에 월급 루팡은 바로 나', 'APPROVED', 'Mock 데이터', 2, 1, now(), now()),
       (22, '월급 루팡18', '이 구역에 월급 루팡은 바로 나', 'APPROVED', 'Mock 데이터', 2, 1, now(), now()),
       (23, '월급 루팡19', '이 구역에 월급 루팡은 바로 나', 'APPROVED', 'Mock 데이터', 2, 1, now(), now()),
       (24, '월급 루팡20', '이 구역에 월급 루팡은 바로 나', 'APPROVED', 'Mock 데이터', 2, 1, now(), now());

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

INSERT INTO Post_Image(id, name, url, post_id, created_at, modified_at)
VALUES (1, '탕수육 사진1',
        'https://legeno-around-here.s3.ap-northeast-2.amazonaws.com/posts/images/mock/t1.jpeg', 1,
        now(), now()),
       (2, '탕수육 사진2',
        'https://legeno-around-here.s3.ap-northeast-2.amazonaws.com/posts/images/mock/t2.jpeg', 1,
        now(), now()),
       (3, '탕수육 사진3',
        'https://legeno-around-here.s3.ap-northeast-2.amazonaws.com/posts/images/mock/t3.jpeg', 1,
        now(), now()),
       (4, '짬뽕 사진1',
        'https://legeno-around-here.s3.ap-northeast-2.amazonaws.com/posts/images/mock/j1.jpg', 2,
        now(), now()),
       (5, '짬뽕 사진2',
        'https://legeno-around-here.s3.ap-northeast-2.amazonaws.com/posts/images/mock/j2.jpeg', 2,
        now(), now()),
       (6, '짜장면 사진',
        'https://legeno-around-here.s3.ap-northeast-2.amazonaws.com/posts/images/mock/z1.png', 3,
        now(), now());
