package wooteco.team.ittabi.legenoaroundhere.service;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Long createUser(User user) {
        User persistUser = userRepository.save(user);
        return persistUser.getId();
    }
}
