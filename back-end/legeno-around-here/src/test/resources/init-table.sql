DELETE
FROM POST_ZZANG;
DELETE
FROM POST_IMAGE;
DELETE
FROM COMMENT_ZZANG;
DELETE
FROM COMMENT;
DELETE
FROM POST;
DELETE
FROM SECTOR;
DELETE
FROM USER_ROLES
WHERE USER_ID >= 3;
DELETE
FROM USER
WHERE ID >= 3;

