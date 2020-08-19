package wooteco.team.ittabi.legenoaroundhere.domain.user;

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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(exclude = {"posts", "comments"})
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class User extends BaseEntity implements UserDetails {

    @Embedded
    @Column(nullable = false, unique = true)
    private Email email;

    @Embedded
    @Column(nullable = false)
    private Nickname nickname;

    @Embedded
    @Column(nullable = false)
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

    public void update(User user) {
        this.nickname = user.nickname;
        this.password = user.password;
        this.area = user.area;
        this.image = user.image;
        setUserAtImage(user.image);
    }

    private void setUserAtImage(UserImage userImage) {
        if (Objects.isNull(userImage)) {
            return;
        }
        if (userImage.hasNotUser()) {
            image.setUser(this);
        }
    }

    public boolean isNotSame(User user) {
        return !this.equals(user);
    }

    public String getPasswordByString() {
        return this.password.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email.getEmail();
    }

    @Override
    public String getPassword() {
        return password.getPassword();
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

    public String getEmailByString() {
        return this.email.getEmail();
    }

    public String getNicknameByString() {
        return getNickname().getNickname();
    }
}
