package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostTestConstants.TEST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_MY_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_PASSWORD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest extends AuthServiceTest {

    @Mock
    CommentService commentService;

    private User user;

    @BeforeEach
    void setUp() {
        user = createUser(TEST_MY_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
    }

    @DisplayName("댓글 생성 - 성공")
    @Test
    void createComment_SuccessToCreate() {
        CommentResponse commentResponse = new CommentResponse(1L, "", UserResponse.from(user));
        when(commentService.createComment(anyLong(), any(CommentRequest.class)))
            .thenReturn(commentResponse);
        CommentRequest commentRequest = new CommentRequest(TEST_WRITING);

        CommentResponse createdCommentResponse = commentService.createComment(1L, commentRequest);

        assertThat(createdCommentResponse.getId()).isEqualTo(commentResponse.getId());
    }

//    @DisplayName("댓글 생성 - 실패, 로그인을 하지 않았을 경우")
//    @Test
//    void createComment_IfNotOwner_ThrowException() {
//        CommentRequest commentRequest = new CommentRequest(TEST_WRITING);
//        commentService.createComment(1L, commentRequest);
//    }

    @DisplayName("댓글 조회 - 성공")
    @Test
    void findComment_SuccessToFind() {
        CommentResponse commentResponse = new CommentResponse(1L, "", UserResponse.from(user));
        when(commentService.createComment(anyLong(), any(CommentRequest.class)))
            .thenReturn(commentResponse);
        when(commentService.findComment(anyLong(), anyLong())).thenReturn(commentResponse);
        CommentRequest commentRequest = new CommentRequest(TEST_WRITING);
        commentService.createComment(1L, commentRequest);

        CommentResponse foundCommentResponse = commentService.findComment(1L, 1L);

        assertThat(commentResponse.getId()).isEqualTo(foundCommentResponse.getId());
    }

//    @DisplayName("댓글 조회 - 실패, 찾는 Comment 삭제 됐을 경우")
//    @Test
//    void findComment_AlreadyDeletedCommentId_ThrownException() {
//        CommentRequest commentRequest = new CommentRequest(TEST_WRITING);
//        CommentResponse commentResponse = commentService.createComment(commentRequest);
//
//        commentService.findComment(commentResponse.getId());
//    }

//    @DisplayName("댓글 조회 - 실패, 찾는 Comment ID가 없을 경우")
//    @Test
//    void findComment_HasNotCommentId_ThrownException() {
//        CommentRequest commentRequest = new CommentRequest(TEST_WRITING);
//        CommentResponse commentResponse = commentService.createComment(commentRequest);
//
//        commentService.findComment(commentResponse.getId());
//    }

//    @DisplayName("댓글 조회 - 실패, 찾는 Post가 삭제 상태일 경우")
//    @Test
//    void findComment_HasDeletedPost_ThrownException() {
//
//    }
//
//    @DisplayName("댓글 조회 - 실패, 찾는 Post ID가 없을 경우")
//    @Test
//    void findComment_HasNotPostId_ThrownException() {
//
//    }


}
