package wooteco.team.ittabi.legenoaroundhere.domain.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.award.PopularPostAward;
import wooteco.team.ittabi.legenoaroundhere.domain.award.SectorCreatorAward;
import wooteco.team.ittabi.legenoaroundhere.domain.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"posts", "comments"})
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Table(indexes = @Index(name = "idx_user_email", columnList = "email"))
public class User extends BaseEntity implements UserDetails {

    @Embedded
    private Email email;

    @Embedded
    private Nickname nickname;

    @Embedded
    private Password password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles
        = new ArrayList<>(Collections.singletonList(Role.USER.getRoleName()));

    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private UserImage image = null;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "creator", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area = null;

    @OneToMany(mappedBy = "awardee")
    private List<PopularPostAward> popularPostAwards = new ArrayList<>();

    @OneToMany(mappedBy = "awardee")
    private List<SectorCreatorAward> sectorCreatorAwards = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    private String providerId;

    private LocalDateTime deactivatedAt;

    @Builder
    public User(String email, String nickname, String password, Area area, UserImage image) {
        this.email = makeEmail(email);
        this.nickname = new Nickname(nickname);
        this.password = new Password(password);
        this.area = area;
        this.image = image;
    }

    private Email makeEmail(String email) {
        if (Objects.nonNull(email)) {
            return new Email(email);
        }
        return null;
    }

    public boolean isNotEquals(User user) {
        return !this.equals(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public int countTopPopularityPostCreatorAwardsBy(int number) {
        return (int) popularPostAwards.stream()
            .filter(popularPostAward -> popularPostAward.isTopBy(number))
            .count();
    }

    public int countSectorCreatorAwards() {
        return sectorCreatorAwards.size();
    }

    public boolean hasNotRole(Role role) {
        return !this.roles.contains(role.getRoleName());
    }

    public void deactivate() {
        this.deactivatedAt = LocalDateTime.now();
    }

    public boolean isDeactivated() {
        return Objects.nonNull(deactivatedAt);
    }

    @Override
    public String getUsername() {
        return this.email.getEmail();
    }

    public String getNickname() {
        return this.nickname.getNickname();
    }

    public void setNickname(String nickname) {
        this.nickname = new Nickname(nickname);
    }

    @Override
    public String getPassword() {
        return this.password.getPassword();
    }

    public void setPassword(String password) {
        this.password = new Password(password);
    }

    public List<String> getRoles() {
        return Collections.unmodifiableList(this.roles);
    }

    public void setImage(UserImage image) {
        this.image = image;
        setUserAtImage(image);
    }

    private void setUserAtImage(UserImage userImage) {
        if (Objects.isNull(userImage)) {
            return;
        }
        if (userImage.hasNotUser()) {
            image.setUser(this);
        }
    }

    public List<Post> getPosts() {
        return Collections.unmodifiableList(this.posts);
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public boolean isDifferentProvider(String registrationId) {
        return !provider.isSame(registrationId);
    }
}
