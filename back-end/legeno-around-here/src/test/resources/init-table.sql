DELETE
FROM POST_ZZANG;
DELETE
FROM POST_IMAGE;
DELETE
FROM COMMENT_ZZANG;
DELETE
FROM POST_REPORT;
DELETE
FROM COMMENT;
DELETE
FROM POST;
DELETE
FROM SECTOR;
DELETE
FROM USER_IMAGE;
DELETE
FROM USER_ROLES
WHERE USER_ID >= 9;
DELETE
FROM USER
WHERE ID >= 9;
