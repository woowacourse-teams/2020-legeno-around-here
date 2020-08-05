package wooteco.team.ittabi.legenoaroundhere.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_MY_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_PASSWORD;

import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.State;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Nickname;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Password;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Post 객체 저장")
    @Test
    void save() {
        //given
        String writing = "writing";
        User user = User.builder()
            .email(new Email(TEST_MY_EMAIL))
            .nickname(new Nickname(TEST_NICKNAME))
            .password(new Password(TEST_PASSWORD))
            .build();

        userRepository.save(user);
        Post notSavedPost = new Post(user, writing);
        assertThat(notSavedPost.getId()).isNull();
        assertThat(notSavedPost.getCreatedAt()).isNull();
        assertThat(notSavedPost.getModifiedAt()).isNull();
        assertThat(notSavedPost.getWriting()).isEqualTo(writing);
        assertThat(notSavedPost.getState()).isEqualTo(State.PUBLISHED);

        //when
        Post savedPost = postRepository.save(notSavedPost);

        //then
        assertThat(savedPost.getId()).isNotNull();
        assertThat(savedPost.getCreatedAt()).isNotNull();
        assertThat(savedPost.getModifiedAt()).isNotNull();
        assertThat(savedPost.getWriting()).isEqualTo(writing);
        assertThat(savedPost.getState()).isEqualTo(State.PUBLISHED);
    }
}
