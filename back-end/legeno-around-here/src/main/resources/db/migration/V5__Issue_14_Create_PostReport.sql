CREATE TABLE `post_report`
(
    `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `writing`     LONGTEXT     NOT NULL,
    `post_id`     BIGINT(20)   NOT NULL,
    `creator_id`  BIGINT(20)   NOT NULL,
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`  DATETIME              DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`),
    FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) DEFAULT CHARSET = utf8;
