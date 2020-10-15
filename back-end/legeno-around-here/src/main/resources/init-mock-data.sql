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
FROM CSVRead('src/main/resources/area.csv');

INSERT INTO User(id, email, nickname, password, area_id, created_at,
                 modified_at)
VALUES (1, 'admin@email.com', '어드민씨',
        '$2a$10$dEcia20EDmjecE7dlxmSJeb6pHk/cKixUxVdFDKfdABSovUxySFgK', null, now(), now()),
       (2, 'user-area@email.com', '서울지역유저씨',
        '$2a$10$.x/ntq./VhbmHZn/RTAyIOnN08cTQqqpgVcw1zHxdUa.VIuXeAwZ6', 1, now(), now()),
       (3, 'user@email.com', '유저씨',
        '$2a$10$.x/ntq./VhbmHZn/RTAyIOnN08cTQqqpgVcw1zHxdUa.VIuXeAwZ6', null, now(), now());

INSERT INTO User_roles(user_id, roles)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER'),
       (3, 'ROLE_USER');

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

INSERT INTO notification
(id, created_at, deleted_at, modified_at, comment_id, content, post_id, is_read, receiver_id,
 sector_id, user_id)
VALUES (1, now(), null, now(), null, '이것은 안읽은 글 짱 알림.', 1, false, 3, null, null),
       (2, now() - 1, null, now(), null, '이것은 읽은 글 짱 알림', 1, false, 3, null, null),
       (3, now() - 2, null, now(), null, '이것은 안읽은 글 댓글 알림', 1, false, 3, null, null),
       (4, now() - 3, null, now(), null, '이것은 안읽은 유저 수상 알림', null, false, 3, null, 3),
       (5, now() - 4, null, now(), null, '이것은 안읽은 부문 승인 알림', null, false, 3, 1, null),
       (6, now() - 5, null, now(), null, '이것은 읽은 부문 반려 알림', null, false, 3, 2, null),
       (7, now() - 6, null, now(), null, '유저씨님, 가입을 진심으로 축하드립니다.', null, false, 3, null, null),
       (8, now() - 7, null, now(), null, '유저씨님, 가입을 진심으로 축하드립니다.', null, false, 3, null, null),
       (9, now() - 8, null, now(), null, '유저씨님, 가입을 진심으로 축하드립니다.', null, false, 3, null, null),
       (10, now() - 9, null, now(), null, '유저씨님, 가입을 진심으로 축하드립니다.', null, false, 3, null,
        null);

INSERT INTO popular_post_award(name, post_id, ranking, start_date, end_date, awardee_id)
VALUES ('월간 서울특별시 비싼 음식 냠냠쓰 부문 1위', 1, 1, NOW() - 30, NOW(), 3),
       ('월간 서울특별시 비싼 음식 냠냠쓰 부문 2위', 2, 2, NOW() - 30, NOW(), 3),
       ('월간 서울특별시 비싼 음식 냠냠쓰 부문 3위', 3, 3, NOW() - 30, NOW(), 3),
       ('월간 서울특별시 비싼 음식 냠냠쓰 부문 1위', 4, 1, NOW() - 60, NOW() - 30, 3),
       ('월간 서울특별시 음식 빨리 챱챱쓰 부문 2위', 5, 2, NOW() - 60, NOW() - 30, 3),
       ('월간 서울특별시 음식 빨리 챱챱쓰 부문 1위', 6, 1, NOW() - 30, NOW(), 3),
       ('월간 서울특별시 음식 빨리 챱챱쓰 부문 3위', 7, 3, NOW() - 30, NOW(), 3),
       ('월간 서울특별시 카트 존잘러 부문 3위', 8, 3, NOW() - 30, NOW(), 3);

INSERT
INTO sector_creator_award(name, sector_id, date, awardee_id)
VALUES ('비싼 음식 냠냠쓰 부문 창시자', 1, NOW() - 5, 3),
       ('음식 빨리 챱챱쓰 부문 창시자', 2, NOW() - 4, 3),
       ('카트 존잘러 부문 창시자', 2, NOW() - 3, 3),
       ('모바일 카트 존잘 부문 창시자', 2, NOW() - 2, 3),
       ('썩은 사과 부문 창시자', 2, NOW() - 1, 3);
