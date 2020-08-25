package wooteco.team.ittabi.legenoaroundhere.domain.post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.State;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.post.image.PostImage;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PostUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAvailableException;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
    public Post(String writing, List<PostImage> postImages, Area area, Sector sector,
        User creator) {
        validateLength(writing);
        this.writing = writing;
        this.postImages = postImages;
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

    public void addPostImages(List<PostImage> postImages) {
        postImages.forEach(this::addPostImage);
    }

    private void addPostImage(PostImage postImage) {
        if (!this.postImages.contains(postImage)) {
            this.postImages.add(postImage);
        }
        if (!postImage.hasPost()) {
            postImage.setPost(this);
        }
    }

    public void removeNotExistPostImages(List<Long> updatePostImageIds) {
        List<Long> deletePostImageIds = getDeletePostImageIds(updatePostImageIds);
        List<PostImage> deletePostImages = getDeletePostImages(deletePostImageIds);

        removePostImages(deletePostImages);
    }

    private void removePostImages(List<PostImage> postImages) {
        this.postImages.removeAll(postImages);
    }

    public void addComment(Comment comment) {
        if (!this.comments.contains(comment)) {
            this.comments.add(comment);
        }
        if (!comment.hasPost()) {
            comment.setPost(this);
        }
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    public int getAvailableCommentsSize() {
        return (int) this.comments.stream()
            .filter(Comment::isAvailable)
            .count();
    }

    public void pressZzang(User user) {
        validateAvailable();
        Optional<PostZzang> foundZzang = this.zzangs.stream()
            .filter(PostZzang -> PostZzang.isSameCreator(user))
            .findFirst();

        if (foundZzang.isPresent()) {
            this.zzangs.remove(foundZzang.get());
            return;
        }
        this.zzangs.add(new PostZzang(this, user));
    }

    public void validateAvailable() {
        if (!this.state.isAvailable()) {
            throw new NotAvailableException(
                "ID [" + this.getId() + "]에 해당하는 Post가 유효하지 않습니다.");
        }
    }

    private List<Long> getDeletePostImageIds(List<Long> updatePostImageIds) {
        List<Long> originPostImageIds = this.postImages.stream()
            .map(PostImage::getId)
            .collect(Collectors.toList());
        originPostImageIds.removeAll(updatePostImageIds);
        return originPostImageIds;
    }

    private List<PostImage> getDeletePostImages(List<Long> deletePostImageIds) {
        return this.postImages.stream()
            .filter(postImage -> postImage.isContainsOf(deletePostImageIds))
            .collect(Collectors.toList());
    }

    public void update(PostUpdateRequest postUpdateRequest) {
        this.writing = postUpdateRequest.getWriting();
    }

    public int getPostZzangCount() {
        return this.zzangs.size();
    }

    public int getPostZzangCountByDate(LocalDateTime startDate, LocalDateTime endDate) {
        long zzangCount = this.zzangs.stream()
            .filter(postZzang -> isDateBetween(postZzang.getCreatedAt(), startDate, endDate))
            .count();
        return Math.toIntExact(zzangCount);
    }

    private boolean isDateBetween(LocalDateTime targetDate, LocalDateTime startDate,
        LocalDateTime endDate) {
        return targetDate.isAfter(startDate)
            && targetDate.isBefore(endDate);
    }

    public boolean hasZzangCreator(User user) {
        return this.zzangs.stream()
            .anyMatch(zzang -> zzang.isSameCreator(user));
    }

    public void setWriting(String writing) {
        this.writing = writing;
    }

    public List<PostImage> getPostImages() {
        return Collections.unmodifiableList(this.postImages);
    }

    public List<PostZzang> getZzangs() {
        return Collections.unmodifiableList(this.zzangs);
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(this.comments);
    }

    public void setState(State state) {
        this.state = state;
    }
}
