package wooteco.team.ittabi.legenoaroundhere.service.report;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.report.UserReport;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.ReportCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserReportAssembler;
import wooteco.team.ittabi.legenoaroundhere.dto.UserReportResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.UserReportRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserReportService {

    private final UserReportRepository userReportRepository;
    private final UserRepository userRepository;
    private final IAuthenticationFacade authenticationFacade;

    @Transactional
    public UserReportResponse createUserReport(ReportCreateRequest reportCreateRequest) {
        User user = findUserBy(reportCreateRequest.getTargetId());
        User reporter = (User) authenticationFacade.getPrincipal();

        UserReport userReport = UserReportAssembler.assemble(reportCreateRequest, reporter, user);

        UserReport savedUserReport = userReportRepository.save(userReport);
        return UserReportResponse.of(savedUserReport);
    }

    private User findUserBy(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotExistsException("ID : " + userId + " 에 해당하는 User가 없습니다!"));
    }
}
