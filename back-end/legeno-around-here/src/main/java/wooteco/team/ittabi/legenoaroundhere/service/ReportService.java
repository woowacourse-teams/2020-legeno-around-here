package wooteco.team.ittabi.legenoaroundhere.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.domain.post.PostReport;
import wooteco.team.ittabi.legenoaroundhere.dto.PostReportResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.PostReportRepository;

@Service
@AllArgsConstructor
public class ReportService {

    private final PostReportRepository postReportRepository;

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
    public Page<PostReportResponse> findPostReportByPage(Pageable pageable) {
        Page<PostReport> postReportPage = postReportRepository.findAll(pageable);
        return postReportPage.map(PostReportResponse::of);
    }

    @Transactional
    public void deletePostReport(Long id) {
        PostReport postReport = findPostReportBy(id);
        postReportRepository.delete(postReport);
    }
}
