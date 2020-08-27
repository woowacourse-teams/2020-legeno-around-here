package wooteco.team.ittabi.legenoaroundhere.service.report;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.report.CommentReport;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentReportAssembler;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentReportResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.ReportCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.CommentReportRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.CommentRepository;

@Service
@AllArgsConstructor
public class CommentReportService {

    private final CommentReportRepository commentReportRepository;
    private final CommentRepository commentRepository;
    private final IAuthenticationFacade authenticationFacade;

    @Transactional
    public CommentReportResponse createCommentReport(ReportCreateRequest reportCreateRequest) {
        Comment comment = findCommentBy(reportCreateRequest.getTargetId());
        User reporter = (User) authenticationFacade.getPrincipal();

        CommentReport commentReport
            = CommentReportAssembler.assemble(reportCreateRequest, reporter, comment);

        CommentReport savedCommentReport = commentReportRepository.save(commentReport);
        return CommentReportResponse.of(savedCommentReport);
    }

    private Comment findCommentBy(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(
                () -> new NotExistsException("ID : " + commentId + " 에 해당하는 Comment가 없습니다!"));
    }
}
