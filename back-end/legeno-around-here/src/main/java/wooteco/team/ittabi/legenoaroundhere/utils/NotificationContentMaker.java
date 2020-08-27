package wooteco.team.ittabi.legenoaroundhere.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import wooteco.team.ittabi.legenoaroundhere.domain.award.AwardEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationContentMaker {

    public static final String KEYWORD_ZZANG = "짱";
    public static final String KEYWORD_COMMENT = "댓글";
    public static final String KEYWORD_AWARD = "수상";
    public static final String KEYWORD_SECTOR_APPROVED = "부문이 승인";
    public static final String KEYWORD_SECTOR_REJECTED = "부문이 반려";

    public static String makeJoinNotificationContent(User user) {
        return user.getNickname() + "님, 가입을 진심으로 축하드립니다.";
    }

    public static String makePressZzangNotificationContent(Post post) {
        return "당신의 글에 " + post.getPostZzangCount() + "명이 " + KEYWORD_ZZANG + "을 눌렀습니다.";
    }

    public static String makePressZzangNotificationContent(Comment comment) {
        return "당신의 댓글에 " + comment.getZzangCounts() + "명이 " + KEYWORD_ZZANG + "을 눌렀습니다.";
    }

    public static String makeCreatedCommentNotificationContent(Post post) {
        return "당신의 글에 " + post.getAvailableCommentsCount() + "명이 " + KEYWORD_COMMENT + "을 달았습니다.";
    }

    public static String makeCreatedCommentNotificationContent(Comment comment) {
        return "당신의 댓글에 " + comment.getCocommentsCount() + "명이 " + KEYWORD_COMMENT + "을 달았습니다.";
    }

    public static String makeApprovedSectorNotificationContent(Sector sector) {
        return "신청한 [" + sector.getName() + "] " + KEYWORD_SECTOR_APPROVED + "되었습니다.";
    }

    public static String makeRejectedSectorNotificationContent(Sector sector) {
        return "신청한 [" + sector.getName() + "] " + KEYWORD_SECTOR_REJECTED + "되었습니다.";
    }

    public static String makeGivenAwardNotificationContent(AwardEntity awardEntity) {
        return "[" + awardEntity.getName() + "] 상을 " + KEYWORD_AWARD + "하셨습니다.";
    }
}
