CREATE TABLE `post_report`
(
    `id`             BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `post_writing`   LONGTEXT     NOT NULL,
    `report_writing` LONGTEXT     NOT NULL,
    `reporter_id`    BIGINT(20)   NOT NULL,
    `created_at`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`     DATETIME              DEFAULT NULL,
    PRIMARY KEY (`id`)
) DEFAULT CHARSET = utf8;

CREATE TABLE `post_report_post_image_urls`
(
    `post_report_id`  BIGINT(20) NOT NULL,
    `post_image_urls` VARCHAR(255),
    PRIMARY KEY (`post_report_id`)
) DEFAULT CHARSET = utf8;

ALTER TABLE `post_report`
    ADD CONSTRAINT `FK_POST_REPORT_REPORTER`
    FOREIGN KEY(`reporter_id`)
    REFERENCES `user` (`id`);

ALTER TABLE `post_report_post_image_urls`
    ADD CONSTRAINT `FK_POST_REPORT_POST_IMAGE_URLS_POST_REPORT`
    FOREIGN KEY(`post_report_id`)
    REFERENCES `post_report`;
