package wooteco.team.ittabi.legenoaroundhere.domain.post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.post.image.PostImage;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAvailableException;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(exclude = {"comments", "postImages", "zzangs"})
@SQLDelete(sql = "UPDATE post SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Post extends BaseEntity {

    private static final int MAX_LENGTH = 2000;

    @Lob
    @Column(nullable = false)
    private String writing;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PostZzang> zzangs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @ManyToOne
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @Builder
    public Post(String writing, Area area, Sector sector, User creator) {
        validateLength(writing);
        this.writing = writing;
        this.area = area;
        this.sector = sector;
        this.state = State.PUBLISHED;
        this.creator = creator;
    }

    private void validateLength(String writing) {
        if (writing.length() > MAX_LENGTH) {
            throw new WrongUserInputException(MAX_LENGTH + "글자를 초과했습니다!");
        }
    }

    public boolean isAvailable() {
        return this.state.isAvailable();
    }

    public int getPostZzangCount() {
        return zzangs.size();
    }

    public int getPostZzangCountByDate(LocalDateTime startDate, LocalDateTime endDate) {
        long zzangCount = zzangs.stream()
            .filter(postZzang -> isDateBetween(postZzang.getCreatedAt(), startDate, endDate))
            .count();
        return Math.toIntExact(zzangCount);
    }

    private boolean isDateBetween(LocalDateTime targetDate, LocalDateTime startDate,
        LocalDateTime endDate) {
        return ((targetDate.isAfter(startDate))
            && (targetDate.isBefore(endDate)));
    }

    public PostZzang findPostZzangBy(User user) {
        return zzangs.stream()
            .filter(postZzang -> postZzang.isSameCreator(user))
            .findFirst()
            .orElseGet(() -> new PostZzang(this, user));
    }

    public boolean hasZzangCreator(User user) {
        return zzangs.stream()
            .anyMatch(zzang -> zzang.isSameCreator(user));
    }

    public void addComment(Comment comment) {
        if (!comments.contains(comment)) {
            comments.add(comment);
        }
        if (!comment.hasPost()) {
            comment.setPost(this);
        }
    }

    public void removeComments(Comment comment) {
        comments.remove(comment);
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public int getAvailableCommentsSize() {
        return (int) comments.stream()
            .filter(Comment::isAvailable)
            .count();
    }

    public void pressZzang(User user) {
        validateAvailable();
        Optional<PostZzang> foundZzang = zzangs.stream()
            .filter(PostZzang -> PostZzang.isSameCreator(user))
            .findFirst();

        if (foundZzang.isPresent()) {
            this.zzangs.remove(foundZzang.get());
            return;
        }
        zzangs.add(new PostZzang(this, user));
    }

    public void validateAvailable() {
        if (!this.state.isAvailable()) {
            throw new NotAvailableException(
                "ID [" + this.getId() + "]에 해당하는 Post가 유효하지 않습니다.");
        }
    }
}
