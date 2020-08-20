package wooteco.team.ittabi.legenoaroundhere.domain.comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAvailableException;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"post", "superComment", "cocomments"})
@SQLDelete(sql = "UPDATE comment SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Comment extends BaseEntity {

    private static final int MAX_LENGTH = 200;

    @Lob
    @Column(nullable = false)
    private String writing;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CommentState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "super_comment_id")
    private Comment superComment;

    @OneToMany(mappedBy = "superComment")
    private List<Comment> cocomments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<CommentZzang> zzangs = new ArrayList<>();

    public Comment(User creator, String writing) {
        validateLength(writing);
        this.writing = writing;
        this.state = CommentState.PUBLISHED;
        this.creator = creator;
    }

    private void validateLength(String writing) {
        if (writing.length() > MAX_LENGTH) {
            throw new WrongUserInputException(MAX_LENGTH + "글자를 초과했습니다!");
        }
    }

    public void pressZzang(User user) {
        this.validateAvailable();
        this.post.validateAvailable();
        Optional<CommentZzang> foundZzang = zzangs.stream()
            .filter(commentZzang -> commentZzang.isSameCreator(user))
            .findFirst();

        if (foundZzang.isPresent()) {
            this.zzangs.remove(foundZzang.get());
            return;
        }
        zzangs.add(new CommentZzang(this, user));
    }

    private void validateAvailable() {
        if (!state.isAvailable()) {
            throw new NotAvailableException("ID [" + this.getId() + "]에 해당하는 Comment가 유효하지 않습니다.");
        }
    }

    public void setPost(Post post) {
        if (Objects.nonNull(this.post)) {
            this.post.removeComments(this);
        }
        this.post = post;
        post.addComment(this);
    }

    public boolean hasPost() {
        return Objects.nonNull(this.post);
    }

    public int getZzangCounts() {
        return zzangs.size();
    }

    public String getWriting() {
        return state.changeWriting(writing);
    }

    public boolean hasZzangCreator(User user) {
        return zzangs.stream()
            .anyMatch(zzang -> zzang.isSameCreator(user));
    }

    public boolean hasCocoments() {
        return !cocomments.isEmpty();
    }

    public boolean isAvailable() {
        return state.isAvailable();
    }

    public boolean isSuperComment() {
        return Objects.isNull(superComment);
    }

    public void setWriting(String writing) {
        this.writing = writing;
    }

    public void setSuperComment(Comment comment) {
        this.superComment = comment;
        this.post = comment.post;
        comment.cocomments.add(this);
    }

    public void setState(CommentState state) {
        this.state = state;
    }

    public boolean isOnlyCocommentOfSuperComment() {
        return Objects.nonNull(superComment)
            && superComment.cocomments.size() == 1;
    }
}
