package wooteco.team.ittabi.legenoaroundhere.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth.MailAuth;

public interface MailAuthRepository extends JpaRepository<MailAuth, Long> {

    Optional<MailAuth> findByEmail(Email email);
}
