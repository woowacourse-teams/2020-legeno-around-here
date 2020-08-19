package wooteco.team.ittabi.legenoaroundhere.domain.post;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PostTest {

    @Test
    void getWriting_wordCount10_thenReturn10() {
        StringBuilder writing = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            writing.append("0");
        }
        Post post = new Post(writing.toString(), null, null, null);

        int expected = 10;
        assertThat(post.getWriting(expected).length()).isEqualTo(expected);
    }
}