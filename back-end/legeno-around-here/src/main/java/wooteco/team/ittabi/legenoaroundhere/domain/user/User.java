package wooteco.team.ittabi.legenoaroundhere.domain.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor()
@Getter
@Setter
@ToString
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
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Post> post = new ArrayList<>();

    @Builder
    public User(Email email, Nickname nickname, Password password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public User(Long id, Email email, Nickname nickname, Password password) {
        super(id, LocalDateTime.now(), LocalDateTime.now());
        this.email = email;
        this.nickname = nickname;
        this.password = password;
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
