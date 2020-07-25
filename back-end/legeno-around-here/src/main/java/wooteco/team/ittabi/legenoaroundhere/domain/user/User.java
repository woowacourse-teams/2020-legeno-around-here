package wooteco.team.ittabi.legenoaroundhere.domain.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    protected User() {
    }

    public User(Email email, Nickname nickname, Password password) {
        validate(email, nickname, password);
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }
//    Todo: roles 컨트롤 할 것
//    public User(Email email, Nickname nickname, Password password, List<String> roles) {
//        validate(email, nickname, password);
//        this.email = email;
//        this.nickname = nickname;
//        this.password = password;
//        this.roles = roles;
//    }

    private void validate(Email email, Nickname nickname, Password password) {
        validateEmailIsNull(email);
        validateNicknameIsNull(nickname);
        validatePasswordIsNull(password);
    }

    private void validateEmailIsNull(Email email) {
        if (Objects.isNull(email)) {
            throw new IllegalArgumentException("이메일이 null 입니다.");
        }
    }

    private void validateNicknameIsNull(Nickname nickname) {
        if (Objects.isNull(nickname)) {
            throw new IllegalArgumentException("닉네임이 null 입니다.");
        }
    }

    private void validatePasswordIsNull(Password password) {
        if (Objects.isNull(password)) {
            throw new IllegalArgumentException("비밀번호가 null 입니다.");
        }
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

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", email=" + email +
            ", nickname=" + nickname +
            ", password=" + password +
            '}';
    }
}
