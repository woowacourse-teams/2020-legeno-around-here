CREATE TABLE `comment_report`
(
    `id`              BIGINT(20) NOT NULL AUTO_INCREMENT,
    `report_writing`  LONGTEXT   NOT NULL,
    `reporter_id`     BIGINT(20) NOT NULL,
    `comment_writing` LONGTEXT   NOT NULL,
    `created_at`      DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at`     DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`      DATETIME            DEFAULT NULL,
    PRIMARY KEY (`id`)
) DEFAULT CHARSET = utf8;