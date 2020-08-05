-- [1] csv 파일을 데이터베이스 내 임시 테이블에 저장
-- raw_legal_area는 법적으로 주소를 분류한 테이블입니다.
-- 애플리케이션 시작 시 raw_legal_area가 존재한다면 삭제 합니다.
-- raw_legal_area 테이블을 생성합니다. legal_code는 법정동 코드, county는 시도명, city는 시군구명, town은 읍면동명, village는 동리명을 뜻합니다.
DROP TABLE IF EXISTS raw_legal_area;
CREATE TABLE raw_legal_area
(
    id         bigint auto_increment,
    legal_code varchar(30),
    county     varchar(30),
    city       varchar(30),
    town       varchar(30),
    village    varchar(30),
    primary key (id)
);

-- 컨테이너 내부로 옮겨진 raw_legal_area.csv 파일을 활용하여 raw_legal_area 테이블을 초기화합니다.
LOAD DATA INFILE '/legeno_around_here/initial_data/test_legal_area.csv'
    INTO TABLE raw_legal_area
    CHARACTER SET utf8
    FIELDS TERMINATED BY ','
    ENCLOSED BY '"'
    LINES
    STARTING BY ''
    TERMINATED BY '\r\n'
    IGNORE 1 ROWS
(legal_code, county, city, town, village);





-- [2] 임시 테이블을 활용하여 지역 관련 초기 데이터 셋팅
-- raw_legal_area 테이블을 활용하여 area 테이블에 데이터를 셋팅합니다.
INSERT INTO area (dtype, id, created_at, code)
SELECT 'Legal', null, current_time(), raw.legal_code
FROM raw_legal_area as raw;

-- raw_legal_area 테이블을 활용하여 region 테이블에 데이터를 셋팅합니다.
INSERT IGNORE INTO region (name, depth)
SELECT DISTINCT county, 'ONE'
FROM raw_legal_area
WHERE county is not null and county != ''

UNION
SELECT DISTINCT city, 'TWO'
FROM raw_legal_area
WHERE city is not null and city != ''

UNION
SELECT DISTINCT town, 'THREE'
FROM raw_legal_area
WHERE town is not null and town != ''

UNION
SELECT DISTINCT village, 'FOUR'
FROM raw_legal_area
WHERE village is not null and village != '';

-- area, region, raw_legal_area 테이블을 활용하여 area 테이블과 region의 관계를 맺어줍니다. 즉 regional_relationship 테이블을 셋팅합니다.
INSERT IGNORE INTO regional_relationship (area_id, region_id, created_at, regional_relationships_key)
SELECT prcessed_area_region.area_id, prcessed_area_region.region_id, current_time(), depth
FROM (SELECT DISTINCT area_id, region_id, depth
      FROM raw_legal_area
               JOIN
           (SELECT id AS area_id, code FROM area) AS area
           ON raw_legal_area.legal_code = area.code
               JOIN (SELECT id AS region_id, name, depth FROM region) AS region
                    ON raw_legal_area.county = region.name

      UNION

      SELECT DISTINCT area_id, region_id, depth
      FROM raw_legal_area
               JOIN
           (SELECT id AS area_id, code FROM area) AS area
           ON raw_legal_area.legal_code = area.code
               JOIN (SELECT id AS region_id, name, depth FROM region) AS region
                    ON raw_legal_area.city = region.name

      UNION

      SELECT DISTINCT area_id, region_id, depth
      FROM raw_legal_area
               JOIN
           (SELECT id AS area_id, code FROM area) AS area
           ON raw_legal_area.legal_code = area.code
               JOIN (SELECT id AS region_id, name, depth FROM region) AS region
                    ON raw_legal_area.town = region.name

      UNION

      SELECT area_id, region_id, depth
      FROM raw_legal_area
               JOIN
           (SELECT id AS area_id, code FROM area) AS area
           ON raw_legal_area.legal_code = area.code
               JOIN (SELECT id AS region_id, name, depth FROM region) AS region
                    ON raw_legal_area.village = region.name
     ) AS prcessed_area_region;

-- [3] 불필요한 임시 테이블 삭제
DROP TABLE IF EXISTS raw_legal_area;
