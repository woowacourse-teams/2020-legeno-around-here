create table `popularity_post_creator_award`
(
    `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(255) NOT NULL,
    `post_id`     BIGINT(20)   NOT NULL,
    `ranking`     INTEGER      NOT NULL,
    `start_date`  DATE         NOT NULL,
    `end_date`    DATE         NOT NULL,
    `awardee_id`  BIGINT(20)   NOT NULL,
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`  DATETIME              DEFAULT NULL,
    PRIMARY KEY (`id`)
) DEFAULT CHARSET = utf8;

create table `sector_creator_award`
(
    `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(255) NOT NULL,
    `sector_id`   BIGINT(20)   NOT NULL,
    `date`        DATE         NOT NULL,
    `awardee_id`  BIGINT(20)   NOT NULL,
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`  DATETIME              DEFAULT NULL,
    PRIMARY KEY (`id`)
) DEFAULT CHARSET = utf8;