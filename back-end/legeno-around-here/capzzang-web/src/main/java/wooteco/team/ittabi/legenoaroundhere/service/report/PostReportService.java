package wooteco.team.ittabi.legenoaroundhere.service.report;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.report.PostReport;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PostReportAssembler;
import wooteco.team.ittabi.legenoaroundhere.dto.PostReportResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.ReportCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.PostReportRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;

@Service
@AllArgsConstructor
public class PostReportService {

    private final PostReportRepository postReportRepository;
    private final PostRepository postRepository;
    private final IAuthenticationFacade authenticationFacade;

    @Transactional
    public PostReportResponse createPostReport(ReportCreateRequest reportCreateRequest) {
        Post post = findPostBy(reportCreateRequest.getTargetId());
        User reporter = (User) authenticationFacade.getPrincipal();

        PostReport postReport = PostReportAssembler.assemble(reportCreateRequest, reporter, post);

        PostReport savedPostReport = postReportRepository.save(postReport);
        return PostReportResponse.of(savedPostReport);
    }

    private Post findPostBy(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new NotExistsException("ID : " + postId + " 에 해당하는 Post가 없습니다!"));
    }

    @Transactional(readOnly = true)
    public PostReportResponse findPostReport(Long id) {
        PostReport postReport = findPostReportBy(id);
        return PostReportResponse.of(postReport);
    }

    private PostReport findPostReportBy(Long id) {
        return postReportRepository.findById(id)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 REPORT가 없습니다."));
    }

    @Transactional(readOnly = true)
    public Page<PostReportResponse> findAllPostReport(Pageable pageable) {
        Page<PostReport> postReportPage = postReportRepository.findAll(pageable);
        return postReportPage.map(PostReportResponse::of);
    }

    @Transactional
    public void deletePostReport(Long id) {
        PostReport postReport = findPostReportBy(id);
        postReportRepository.delete(postReport);
    }
}

