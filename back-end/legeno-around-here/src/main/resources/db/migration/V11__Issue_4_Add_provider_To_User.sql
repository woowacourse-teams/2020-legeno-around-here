ALTER TABLE `user`
    ADD `provider` VARCHAR(255) NOT NULL DEFAULT 'LOCAL';

ALTER TABLE `user`
    ADD `provider_id` VARCHAR(255);

UPDATE `user`
SET `provider` = 'LOCAL';
