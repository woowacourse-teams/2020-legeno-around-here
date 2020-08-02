-- [1] csv 파일을 데이터베이스 내 임시 테이블에 저장
-- raw_legal_area는 법적으로 주소를 분류한 테이블입니다.
-- 애플리케이션 시작 시 raw_legal_area가 존재한다면 삭제 합니다.
-- raw_legal_area 테이블을 생성합니다. legal_code는 법정동 코드, county는 시도명, city는 시군구명, town은 읍면동명, village는 동리명을 뜻합니다.
drop table if exists raw_legal_area;
create table raw_legal_area (
    id bigint auto_increment,
    legal_code varchar(30),
    county varchar(30),
    city varchar(30),
    town varchar(30),
    village varchar(30),
    primary key(id)
);

-- 컨테이너 내부로 옮겨진 raw_legal_area.csv 파일을 활용하여 raw_legal_area 테이블을 초기화합니다.
LOAD DATA INFILE '/legeno_around_here/initial_data/raw_legal_area.csv'
    INTO TABLE raw_legal_area
    CHARACTER SET utf8
    FIELDS TERMINATED BY ','
    ENCLOSED BY '"'
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS
    (legal_code, county, city, town, village);

-- [2] 임시 테이블을 활용하여 지역 관련 초기 데이터 셋팅
-- raw_legal_area 테이블을 활용하여 area 테이블에 데이터를 셋팅합니다.
insert into area (dtype, id, created_at, code)
select "Legal", null, current_time(), raw.legal_code
from raw_legal_area as raw;

-- raw_legal_area 테이블을 활용하여 region 테이블에 데이터를 셋팅합니다.
insert ignore into region (name)
select distinct county from raw_legal_area
union
select distinct city from raw_legal_area
union
select distinct town from raw_legal_area
union
select distinct village from raw_legal_area;

-- area, region, raw_legal_area 테이블을 활용하여 area 테이블과 region의 관계를 맺어줍니다. 즉 regional_relationship 테이블을 셋팅합니다.
insert ignore into regional_relationship (area_id, region_id)
    select c.area_id, c.region_id from
        (select distinct area_id, region_id from
            raw_legal_area  join
            (select id as area_id, code from area) as area
            on raw_legal_area.legal_code = area.code
            join (select id as region_id, name from region) as region
            on raw_legal_area.county = region.name

        union

        select distinct area_id, region_id from
            raw_legal_area join
            (select id as area_id, code from area) as area
            on raw_legal_area.legal_code = area.code
            join (select id as region_id, name from region) as region
            on raw_legal_area.city = region.name

        union

        select distinct area_id, region_id from
            raw_legal_area join
            (select id as area_id, code from area) as area
            on raw_legal_area.legal_code = area.code
            join (select id as region_id, name from region) as region
            on raw_legal_area.town = region.name

        union

        select area_id, region_id from
            raw_legal_area join
            (select id as area_id, code from area) as area
            on raw_legal_area.legal_code = area.code
            join (select id as region_id, name from region) as region
            on raw_legal_area.village = region.name
        ) as c;

-- [3] 불필요한 임시 테이블 삭제
drop table if exists raw_legal_area;
