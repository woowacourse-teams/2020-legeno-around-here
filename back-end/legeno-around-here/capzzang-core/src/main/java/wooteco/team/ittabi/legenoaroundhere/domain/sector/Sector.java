package wooteco.team.ittabi.legenoaroundhere.domain.sector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.notification.Notification;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"posts", "notifications"})
@SQLDelete(sql = "UPDATE sector SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Table(indexes = {
    @Index(name = "idx_sector_name", columnList = "name"),
    @Index(name = "idx_sector_creator", columnList = "creator_id")
})
public class Sector extends BaseEntity {

    private static final String DEFAULT_REASON = "";

    @Embedded
    private Name name;

    @Embedded
    private Description description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SectorState state;

    @Column(nullable = false)
    private String reason = DEFAULT_REASON;

    @OneToMany(mappedBy = "sector", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "last_modifier_id", nullable = false)
    private User lastModifier;

    @OneToMany(mappedBy = "sector", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    @Builder
    public Sector(String name, String description, User creator, User lastModifier,
        SectorState state) {
        validate(creator, lastModifier, state);
        this.name = Name.of(name);
        this.description = new Description(description);
        this.creator = creator;
        this.lastModifier = lastModifier;
        this.state = state;
    }

    private void validate(User creator, User lastModifier, SectorState state) {
        Objects.requireNonNull(creator, "Creator는 Null일 수 없습니다.");
        Objects.requireNonNull(lastModifier, "LastModifier는 Null일 수 없습니다.");
        Objects.requireNonNull(state, "SectorState는 Null일 수 없습니다.");
    }

    public boolean isUsed() {
        return state.isUsed();
    }

    public boolean isNotUsed() {
        return !state.isUsed();
    }

    public void update(Sector sector) {
        Objects.requireNonNull(sector, "Sector는 Null일 수 없습니다.");
        this.name = sector.name;
        this.description = sector.description;
        this.lastModifier = sector.lastModifier;
    }

    public void setState(SectorState state, String reason, User lastModifier) {
        Objects.requireNonNull(state, "SectorState는 Null일 수 없습니다.");
        Objects.requireNonNull(reason, "Reason은 Null일 수 없습니다.");
        Objects.requireNonNull(lastModifier, "LastModifier는 Null일 수 없습니다.");
        this.state = state;
        this.reason = reason;
        this.lastModifier = lastModifier;
    }

    public boolean isUniqueState() {
        return this.state.isUnique();
    }

    public String getName() {
        return this.name.getName();
    }

    public String getDescription() {
        return this.description.getDescription();
    }

    public String getDescription(int wordCount) {
        return this.description.getDescription(wordCount);
    }

    public String getState() {
        return this.state.getName();
    }

    public String getStateExceptionName() {
        return this.state.getExceptionName();
    }

    public List<Post> getPosts() {
        return Collections.unmodifiableList(posts);
    }
}
