CREATE TABLE `mail_auth`
(
    `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `email`       VARCHAR(255) NOT NULL UNIQUE,
    `auth_number` INTEGER      NOT NULL,
    `creator_id`  BIGINT(20)   NOT NULL,
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`  DATETIME              DEFAULT NULL,
    PRIMARY KEY (`id`)
) DEFAULT CHARSET = utf8;
