package wooteco.team.ittabi.legenoaroundhere.domain.comment;

import static wooteco.team.ittabi.legenoaroundhere.domain.State.PUBLISHED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.State;
import wooteco.team.ittabi.legenoaroundhere.domain.notification.Notification;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAvailableException;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"post", "superComment", "cocomments", "zzangs", "notifications"})
@SQLDelete(sql = "UPDATE comment SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Table(indexes = @Index(name = "idx_comment_post", columnList = "post_id"))
public class Comment extends BaseEntity {

    private static final int MAX_LENGTH = 200;

    @Lob
    @Column(nullable = false)
    private String writing;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

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

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    public Comment(User creator, String writing) {
        validateLength(writing);
        this.writing = writing;
        this.state = PUBLISHED;
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
        Optional<CommentZzang> foundZzang = this.zzangs.stream()
            .filter(commentZzang -> commentZzang.isSameCreator(user))
            .findFirst();

        if (foundZzang.isPresent()) {
            this.zzangs.remove(foundZzang.get());
            return;
        }
        this.zzangs.add(new CommentZzang(this, user));
    }

    private void validateAvailable() {
        if (!this.state.isAvailable()) {
            throw new NotAvailableException("ID [" + this.getId() + "]에 해당하는 Comment가 유효하지 않습니다.");
        }
    }

    public boolean isAvailable() {
        return this.state.isAvailable();
    }

    public boolean isNotAvailable() {
        return !this.state.isAvailable();
    }

    public boolean hasPost() {
        return Objects.nonNull(this.post);
    }

    public boolean hasSuperComment() {
        return Objects.isNull(this.superComment);
    }

    public boolean hasCocomments() {
        return !this.cocomments.isEmpty();
    }

    public boolean hasOnlyCocomment() {
        return this.cocomments.size() == 1;
    }

    public boolean hasZzangCreator(User user) {
        return this.zzangs.stream()
            .anyMatch(zzang -> zzang.isSameCreator(user));
    }

    public int getZzangCounts() {
        return this.zzangs.size();
    }

    public int getCocommentsCount() {
        return this.cocomments.size();
    }

    public void setWriting(String writing) {
        this.writing = writing;
    }

    public void setPost(Post post) {
        if (Objects.nonNull(this.post)) {
            this.post.removeComment(this);
        }
        this.post = post;
        post.addComment(this);
    }

    public void setSuperComment(Comment superComment) {
        this.superComment = superComment;
        superComment.cocomments.add(this);
        setPost(superComment.getPost());
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<Comment> getCocomments() {
        return Collections.unmodifiableList(this.cocomments);
    }

    public List<CommentZzang> getZzangs() {
        return Collections.unmodifiableList(this.zzangs);
    }
}
