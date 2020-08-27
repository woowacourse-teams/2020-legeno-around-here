CREATE TABLE `notification`
(
    `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `content`     VARCHAR(255) NOT NULL,
    `comment_id`  BIGINT(20),
    `sector_id`   BIGINT(20),
    `post_id`     BIGINT(20),
    `user_id`     BIGINT(20),
    `receiver_id` BIGINT(20),
    `is_read`     TINYINT(1)   NOT NULL,
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`  DATETIME              DEFAULT NULL,
    PRIMARY KEY (`id`)
) DEFAULT CHARSET = utf8;
