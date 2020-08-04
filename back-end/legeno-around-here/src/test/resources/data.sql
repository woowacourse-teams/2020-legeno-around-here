CREATE TABLE raw_legal_area
(
    legal_code varchar(30),
    county     varchar(30),
    city       varchar(30),
    town       varchar(30),
    village    varchar(30)
)
AS
SELECT *
FROM CSVREAD('classpath:test_legal_area.csv');

INSERT INTO area (dtype, id, created_at, code)
SELECT 'Legal', null, current_time(), raw.legal_code
FROM raw_legal_area as raw;


INSERT INTO region (name, depth)
SELECT DISTINCT county, 'ONE'
FROM raw_legal_area
WHERE county is not null

UNION
SELECT DISTINCT city, 'TWO'
FROM raw_legal_area
WHERE city is not null

UNION
SELECT DISTINCT town, 'THREE'
FROM raw_legal_area
WHERE town is not null

UNION
SELECT DISTINCT village, 'FOUR'
FROM raw_legal_area
WHERE village is not null;

INSERT INTO regional_relationship (area_id, region_id, created_at)
SELECT prcessed_area_region.area_id, prcessed_area_region.region_id, current_time()
FROM (SELECT DISTINCT area_id, region_id
      FROM raw_legal_area
               JOIN
           (SELECT id AS area_id, code FROM area) AS area
           ON raw_legal_area.legal_code = area.code
               JOIN (SELECT id AS region_id, name FROM region) AS region
                    ON raw_legal_area.county = region.name

      UNION

      SELECT DISTINCT area_id, region_id
      FROM raw_legal_area
               JOIN
           (SELECT id AS area_id, code FROM area) AS area
           ON raw_legal_area.legal_code = area.code
               JOIN (SELECT id AS region_id, name FROM region) AS region
                    ON raw_legal_area.city = region.name

      UNION

      SELECT DISTINCT area_id, region_id
      FROM raw_legal_area
               JOIN
           (SELECT id AS area_id, code FROM area) AS area
           ON raw_legal_area.legal_code = area.code
               JOIN (SELECT id AS region_id, name FROM region) AS region
                    ON raw_legal_area.town = region.name

      UNION

      SELECT area_id, region_id
      FROM raw_legal_area
               JOIN
           (SELECT id AS area_id, code FROM area) AS area
           ON raw_legal_area.legal_code = area.code
               JOIN (SELECT id AS region_id, name FROM region) AS region
                    ON raw_legal_area.village = region.name
     ) AS prcessed_area_region;
