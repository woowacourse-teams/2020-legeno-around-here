CREATE TABLE `area`
(
    `id`                BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `full_name`         VARCHAR(255) NOT NULL,
    `first_depth_name`  VARCHAR(255) NOT NULL,
    `second_depth_name` VARCHAR(255) NOT NULL,
    `third_depth_name`  VARCHAR(255) NOT NULL,
    `fourth_depth_name` VARCHAR(255) NOT NULL,
    `created_at`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`        DATETIME              DEFAULT NULL,
    PRIMARY KEY (`id`)
) DEFAULT CHARSET = utf8;

CREATE TABLE `user`
(
    `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `email`       VARCHAR(255) NOT NULL UNIQUE,
    `nickname`    VARCHAR(255) NOT NULL,
    `password`    VARCHAR(255) NOT NULL,
    `area_id`     BIGINT(20)            DEFAULT NULL,
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`  DATETIME              DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`area_id`) REFERENCES `area` (`id`)
) DEFAULT CHARSET = utf8;

CREATE TABLE `user_image`
(
    `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `user_id`     BIGINT(20)            DEFAULT NULL,
    `name`        VARCHAR(255) NOT NULL,
    `url`         VARCHAR(255) NOT NULL,
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`  DATETIME              DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) DEFAULT CHARSET = utf8;

CREATE TABLE `user_roles`
(
    `user_id` BIGINT(20)   NOT NULL,
    `roles`   VARCHAR(255) NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) DEFAULT CHARSET = utf8;

CREATE TABLE `sector`
(
    `id`               BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `name`             VARCHAR(255) NOT NULL,
    `description`      VARCHAR(255) NOT NULL,
    `state`            VARCHAR(255) NOT NULL,
    `reason`           VARCHAR(255) NOT NULL,
    `creator_id`       BIGINT(20)   NOT NULL,
    `last_modifier_id` BIGINT(20)   NOT NULL,
    `created_at`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`       DATETIME              DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`),
    FOREIGN KEY (`last_modifier_id`) REFERENCES `user` (`id`)
) DEFAULT CHARSET = utf8;

CREATE TABLE `post`
(
    `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `writing`     LONGTEXT     NOT NULL,
    `area_id`     BIGINT(20)   NOT NULL,
    `sector_id`   BIGINT(20)   NOT NULL,
    `state`       VARCHAR(255) NOT NULL,
    `creator_id`  BIGINT(20)   NOT NULL,
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`  DATETIME              DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`sector_id`) REFERENCES `sector` (`id`),
    FOREIGN KEY (`area_id`) REFERENCES `area` (`id`),
    FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`)
) DEFAULT CHARSET = utf8;

CREATE TABLE `post_image`
(
    `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `post_id`     BIGINT(20)            DEFAULT NULL,
    `name`        VARCHAR(255) NOT NULL,
    `url`         VARCHAR(255) NOT NULL,
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`  DATETIME              DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) DEFAULT CHARSET = utf8;

CREATE TABLE `post_zzang`
(
    `id`          BIGINT(20) NOT NULL AUTO_INCREMENT,
    `post_id`     BIGINT(20) NOT NULL,
    `creator_id`  BIGINT(20) NOT NULL,
    `created_at`  DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`  DATETIME            DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`),
    FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) DEFAULT CHARSET = utf8;

CREATE TABLE `comment`
(
    `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `post_id`     BIGINT(20)   NOT NULL,
    `writing`     LONGTEXT     NOT NULL,
    `state`       VARCHAR(255) NOT NULL,
    `creator_id`  BIGINT(20)   NOT NULL,
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`  DATETIME              DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`),
    FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) DEFAULT CHARSET = utf8;

CREATE TABLE `comment_zzang`
(
    `id`          BIGINT(20) NOT NULL AUTO_INCREMENT,
    `comment_id`  BIGINT(20) NOT NULL,
    `creator_id`  BIGINT(20) NOT NULL,
    `created_at`  DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`  DATETIME            DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`),
    FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`)
) DEFAULT CHARSET = utf8;