drop table if exists raw_legal_area;

create table raw_legal_area (
    id bigint auto_increment,
    legal_code varchar(30),
    depth_one varchar(30),
    depth_two varchar(30),
    depth_three varchar(30),
    depth_four varchar(30),
    primary key(id)
);

LOAD DATA INFILE '/legeno_around_here/initial_data/raw_legal_area.csv'
    INTO TABLE raw_legal_area
    CHARACTER SET utf8
    FIELDS TERMINATED BY ','
    ENCLOSED BY '"'
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS
    (legal_code, depth_one, depth_two, depth_three, depth_four);

insert into area (dtype, id, created_at, code)
select "Legal", null, current_time(), raw.legal_code
from raw_legal_area as raw;

insert ignore into region (name)
select distinct depth_one from raw_legal_area
union
select distinct depth_two from raw_legal_area
union
select distinct depth_three from raw_legal_area
union
select distinct depth_four from raw_legal_area;

-- todo:

insert ignore into regional_relationship (area_id, region_id)
    select c.area_id, c.region_id from
        (select distinct area_id, region_id from
            raw_legal_area  join
            (select id as area_id, code from area) as area
            on raw_legal_area.legal_code = area.code
            join (select id as region_id, name from region) as region
            on raw_legal_area.depth_one = region.name

        union

        select distinct area_id, region_id from
            raw_legal_area join
            (select id as area_id, code from area) as area
            on raw_legal_area.legal_code = area.code
            join (select id as region_id, name from region) as region
            on raw_legal_area.depth_two = region.name

        union

        select distinct area_id, region_id from
            raw_legal_area join
            (select id as area_id, code from area) as area
            on raw_legal_area.legal_code = area.code
            join (select id as region_id, name from region) as region
            on raw_legal_area.depth_three = region.name

        union

        select area_id, region_id from
            raw_legal_area join
            (select id as area_id, code from area) as area
            on raw_legal_area.legal_code = area.code
            join (select id as region_id, name from region) as region
            on raw_legal_area.depth_four = region.name
        ) as c;
