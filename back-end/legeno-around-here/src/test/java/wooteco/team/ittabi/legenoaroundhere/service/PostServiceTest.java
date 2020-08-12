package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_OTHER_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.EMPTY_MULTIPART_FILES;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_CONTENT_TYPE;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_INVALID_POST_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_ANOTHER_REQUEST;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_REQUEST;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ANOTHER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_PASSWORD;

import java.io.IOException;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostSearchFilterRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostWithCommentsCountResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.utils.FileConverter;

public class PostServiceTest extends ServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private SectorService sectorService;

    private User user;
    private User another;
    private SectorResponse sector;
    private Long sectorId;
    private Long sectorOtherId;

    @BeforeEach
    void setUp() {
        user = createUser(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        another = createUser(TEST_ANOTHER_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        setAuthentication(user);

        sector = sectorService.createSector(TEST_SECTOR_REQUEST);
        sectorId = sector.getId();
        sectorOtherId = sectorService.createSector(TEST_SECTOR_ANOTHER_REQUEST).getId();
    }

    @DisplayName("이미지를 포함하지 않는 포스트 생성 - 성공")
    @Test
    void createPostWithoutImage_SuccessToCreate() {
        PostCreateRequest postCreateRequest
            = new PostCreateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);

        PostResponse postResponse = postService.createPost(postCreateRequest);

        assertThat(postResponse.getId()).isNotNull();
        assertThat(postResponse.getWriting()).isEqualTo(TEST_WRITING);
        assertThat(postResponse.getCreator()).isEqualTo(UserResponse.from(user));
        assertThat(postResponse.getSector()).isEqualTo(sector);
    }

    @DisplayName("이미지를 포함한 포스트 생성 - 성공")
    @Test
    void createPostWithImage_SuccessToCreate() throws IOException {
        MultipartFile multipartFile
            = FileConverter.convert("right_image1.jpg", TEST_IMAGE_CONTENT_TYPE);
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_WRITING,
            Collections.singletonList(multipartFile), TEST_AREA_ID, sectorId);

        PostResponse postResponse = postService.createPost(postCreateRequest);

        assertThat(postResponse.getWriting()).isEqualTo(TEST_WRITING);
        assertThat(postResponse.getImages()).hasSize(1);
        assertThat(postResponse.getCreator()).isEqualTo(UserResponse.from(user));
        assertThat(postResponse.getSector()).isEqualTo(sector);
    }

    @DisplayName("ID로 포스트 조회 - 성공")
    @Test
    void findPost_HasId_SuccessToFind() {
        PostCreateRequest postCreateRequest
            = new PostCreateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        PostResponse createdPostResponse = postService.createPost(postCreateRequest);

        PostResponse postResponse = postService.findPost(createdPostResponse.getId());

        assertThat(postResponse.getId()).isEqualTo(createdPostResponse.getId());
        assertThat(postResponse.getWriting()).isEqualTo(createdPostResponse.getWriting());
        assertThat(postResponse.getCreator()).isEqualTo(UserResponse.from(user));
        assertThat(postResponse.getSector()).isEqualTo(sector);
    }

    @DisplayName("ID로 포스트 조회 - 실패, 이미 지워진 포스트")
    @Test
    void findPost_AlreadyDeletedPost_ThrownException() {
        PostCreateRequest postCreateRequest
            = new PostCreateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        PostResponse createdPostResponse = postService.createPost(postCreateRequest);
        postService.deletePost(createdPostResponse.getId());

        assertThatThrownBy(() -> postService.findPost(createdPostResponse.getId()))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("ID로 포스트 조회 - 실패, 유효하지 않은 포스트 ID")
    @Test
    void findPost_HasNotId_ThrownException() {
        assertThatThrownBy(() -> postService.findPost(TEST_INVALID_POST_ID))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("포스트 전체 목록 검색 - 성공")
    @Test
    void searchAllPost_NoFilter_SuccessToFind() {
        PostCreateRequest postCreateRequest
            = new PostCreateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        postService.createPost(postCreateRequest);
        postService.createPost(postCreateRequest);

        PostSearchFilterRequest postSearchFilterRequest = new PostSearchFilterRequest(null, null);
        Page<PostWithCommentsCountResponse> posts
            = postService.searchAllPost(Pageable.unpaged(), postSearchFilterRequest);

        assertThat(posts.getContent()).hasSize(2);
    }

    @DisplayName("포스트 전체 목록 검색(Area Filter) 단 건 - 성공")
    @Test
    void searchAllPost_AreaFilter_SuccessToFind() {
        PostCreateRequest postCreateRequest
            = new PostCreateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        postService.createPost(postCreateRequest);
        postService.createPost(postCreateRequest);

        postCreateRequest = new PostCreateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES,
            TEST_AREA_OTHER_ID, sectorOtherId);
        postService.createPost(postCreateRequest);

        PostSearchFilterRequest postSearchFilterRequest = new PostSearchFilterRequest(
            String.valueOf(TEST_AREA_OTHER_ID), null);
        Page<PostWithCommentsCountResponse> posts
            = postService.searchAllPost(Pageable.unpaged(), postSearchFilterRequest);

        assertThat(posts.getContent()).hasSize(1);
    }

    @DisplayName("포스트 전체 목록 검색(Sector Filter) - 성공")
    @Test
    void searchAllPost_SectorFilter_SuccessToFind() {
        PostCreateRequest postCreateRequest
            = new PostCreateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        postService.createPost(postCreateRequest);
        postService.createPost(postCreateRequest);

        postCreateRequest = new PostCreateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES,
            TEST_AREA_ID, sectorOtherId);
        postService.createPost(postCreateRequest);
        postService.createPost(postCreateRequest);

        postCreateRequest = new PostCreateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES,
            TEST_AREA_OTHER_ID, sectorId);
        postService.createPost(postCreateRequest);
        postService.createPost(postCreateRequest);

        postCreateRequest = new PostCreateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES,
            TEST_AREA_OTHER_ID, sectorOtherId);
        postService.createPost(postCreateRequest);

        PostSearchFilterRequest postSearchFilterRequest = new PostSearchFilterRequest(
            String.valueOf(TEST_AREA_OTHER_ID), String.valueOf(sectorOtherId));
        Page<PostWithCommentsCountResponse> posts
            = postService.searchAllPost(Pageable.unpaged(), postSearchFilterRequest);

        assertThat(posts.getContent()).hasSize(1);
    }

    @DisplayName("포스트 전체 목록 검색(Area + Sector Filter) - 성공")
    @Test
    void searchAllPost_AreaAndSectorFilter_SuccessToFind() {
        PostCreateRequest postCreateRequest
            = new PostCreateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        postService.createPost(postCreateRequest);
        postService.createPost(postCreateRequest);

        postCreateRequest = new PostCreateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES,
            TEST_AREA_OTHER_ID, sectorOtherId);
        postService.createPost(postCreateRequest);

        PostSearchFilterRequest postSearchFilterRequest = new PostSearchFilterRequest(
            null, String.valueOf(sectorOtherId));
        Page<PostWithCommentsCountResponse> posts
            = postService.searchAllPost(Pageable.unpaged(), postSearchFilterRequest);

        assertThat(posts.getContent()).hasSize(1);
    }

    @DisplayName("ID로 포스트 수정 - 성공")
    @Test
    void updatePost_HasId_SuccessToUpdate() {
        String updatedPostWriting = "Jamie and BingBong";
        PostCreateRequest createdPostCreateRequest
            = new PostCreateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        PostResponse createdPostResponse = postService.createPost(createdPostCreateRequest);
        PostUpdateRequest postUpdateRequest
            = new PostUpdateRequest(updatedPostWriting, EMPTY_MULTIPART_FILES);

        postService.updatePost(createdPostResponse.getId(), postUpdateRequest);
        PostResponse updatedPostResponse = postService.findPost(createdPostResponse.getId());

        assertThat(updatedPostResponse.getWriting()).isEqualTo(updatedPostWriting);
        assertThat(updatedPostResponse.getCreator()).isEqualTo(UserResponse.from(user));
    }

    @DisplayName("ID로 포스트 수정 - 실패")
    @Test
    void updatePost_HasNotId_ThrownException() {
        PostUpdateRequest postUpdateRequest
            = new PostUpdateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES);

        assertThatThrownBy(() -> postService.updatePost(TEST_INVALID_POST_ID, postUpdateRequest))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("ID로 포스트 수정 - 예외 발생, 작성자가 아님")
    @Test
    void updatePost_IfNotCreator_ThrowException() {
        String updatedPostWriting = "Jamie and BingBong";
        PostCreateRequest createdPostCreateRequest
            = new PostCreateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        PostResponse createdPostResponse = postService.createPost(createdPostCreateRequest);
        PostUpdateRequest postUpdateRequest
            = new PostUpdateRequest(updatedPostWriting, EMPTY_MULTIPART_FILES);

        setAuthentication(another);
        assertThatThrownBy(() -> postService
            .updatePost(createdPostResponse.getId(), postUpdateRequest))
            .isInstanceOf(NotAuthorizedException.class);
    }

    @DisplayName("ID로 포스트 삭제 - 성공")
    @Test
    void deletePost_HasId_SuccessToDelete() {
        PostCreateRequest createdPostCreateRequest
            = new PostCreateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        PostResponse createdPostResponse = postService.createPost(createdPostCreateRequest);

        postService.deletePost(createdPostResponse.getId());

        assertThatThrownBy(() -> postService.findPost(createdPostResponse.getId()))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("ID로 포스트 삭제 - 실패")
    @Test
    void deletePost_HasNotId_ThrownException() {
        assertThatThrownBy(() -> postService.deletePost(TEST_INVALID_POST_ID))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("삭제한 ID로 포스트 삭제 - 실패")
    @Test
    void deletePost_AlreadyDeletedId_ThrownException() {
        PostCreateRequest createdPostCreateRequest
            = new PostCreateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        PostResponse createdPostResponse = postService.createPost(createdPostCreateRequest);

        postService.deletePost(createdPostResponse.getId());

        assertThatThrownBy(() -> postService.deletePost(createdPostResponse.getId()))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("ID로 포스트 삭제 - 예외 발생, 작성자가 아님")
    @Test
    void deletePost_IfNotCreator_ThrowException() {
        PostCreateRequest createdPostCreateRequest
            = new PostCreateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        PostResponse createdPostResponse = postService.createPost(createdPostCreateRequest);

        setAuthentication(another);
        assertThatThrownBy(
            () -> postService.deletePost(createdPostResponse.getId()))
            .isInstanceOf(NotAuthorizedException.class);
    }
}
