package wooteco.team.ittabi.legenoaroundhere.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.ranking.RankingCriteria;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PostSearchRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostWithCommentsCountResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.RankingRequest;

@Service
@AllArgsConstructor
public class RankingService {

    private final IAuthenticationFacade authenticationFacade;
    private final PostService postService;

    @Transactional(readOnly = true)
    public Page<PostWithCommentsCountResponse> searchRanking(Pageable pageable,
        RankingRequest rankingRequest, PostSearchRequest postSearchFilter) {
        Page<Post> allDateZzangPosts = postService
            .getPostByFilter(pageable, postSearchFilter.toPostSearch());
        List<Post> filteredDateZzangPosts
            = getPostsThatZzangFilteredByDate(rankingRequest.getCriteria(), allDateZzangPosts);

        User user = (User) authenticationFacade.getPrincipal();
        PageImpl<Post> posts = new PageImpl<>(filteredDateZzangPosts);
        return posts.map(post -> PostWithCommentsCountResponse.of(user, post));
    }

    private List<Post> getPostsThatZzangFilteredByDate(String criteria,
        Page<Post> unfilteredPosts) {
        RankingCriteria rankingCriteria = RankingCriteria.of(criteria);
        LocalDateTime startDate = rankingCriteria.getStartDate();
        LocalDateTime endDate = rankingCriteria.getEndDate();

        return unfilteredPosts.stream()
            .sorted(Comparator.comparing(post -> post.getPostZzangCountByDate(startDate, endDate)))
            .collect(Collectors.toList());
    }
}
