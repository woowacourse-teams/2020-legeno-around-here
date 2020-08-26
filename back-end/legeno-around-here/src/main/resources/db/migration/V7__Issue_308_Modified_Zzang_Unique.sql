ALTER TABLE post_zzang
    ADD UNIQUE (post_id, creator_id);
ALTER TABLE comment_zzang
    ADD UNIQUE (comment_id, creator_id);