create table ready_legal_area (id bigint auto_increment, legal_code varchar(30), depth_one varchar(30), depth_two varchar(30), depth_three varchar(30), depth_four varchar(30), primary key(id));

LOAD DATA INFILE '/legeno_around_here/provision/legal_area.csv' INTO TABLE ready_legal_area CHARACTER SET utf8 FIELDS TERMINATED BY ',' ENCLOSED BY '"' LINES TERMINATED BY '\n' IGNORE 1 ROWS (legal_code, depth_one, depth_two, depth_three, depth_four);
