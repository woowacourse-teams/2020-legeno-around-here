CREATE TABLE `user_report`
(
    `id`             BIGINT(20) NOT NULL AUTO_INCREMENT,
    `report_writing` LONGTEXT   NOT NULL,
    `reporter_id`    BIGINT(20) NOT NULL,
    `user_nickname`  LONGTEXT   NOT NULL,
    `user_image_url` VARCHAR(255),
    `created_at`     DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at`    DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`     DATETIME            DEFAULT NULL,
    PRIMARY KEY (`id`)
) DEFAULT CHARSET = utf8;