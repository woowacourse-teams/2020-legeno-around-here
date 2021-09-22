CREATE INDEX `idx_comment_post` ON `comment` (post_id);
CREATE INDEX `idx_mailauth_email` ON `mail_auth` (email);
CREATE INDEX `idx_notificatiON_receiver` ON `notification` (receiver_id);
CREATE INDEX `idx_popular_post_award_awardee` ON `popular_post_award` (awardee_id);
CREATE INDEX `idx_post_creator` ON `post` (creator_id);
CREATE INDEX `idx_post_deleted_at_area` ON `post` (deleted_at, area_id);
CREATE INDEX `idx_post_deleted_at_sector` ON `post` (deleted_at, sector_id);
CREATE INDEX `idx_sector_name` ON `sector` (name);
CREATE INDEX `idx_sector_creator` ON `sector` (creator_id);
CREATE INDEX `idx_sector_creator_award_awardee` ON `sector_creator_award` (awardee_id);
CREATE INDEX `idx_sector_creator_award_sector` ON `sector_creator_award` (sector_id);
CREATE INDEX `idx_user_email` ON `user` (email);