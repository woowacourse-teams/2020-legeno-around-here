package wooteco.team.ittabi.legenoaroundhere.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.State;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @DisplayName("Post 객체 저장")
    @Test
    void save() {
        //given
        String writing = "writing";
        Post notSavedPost = new Post(writing);
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
