package wooteco.team.ittabi.legenoaroundhere.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.PostSearch;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.ranking.RankingCriteria;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PostSearchRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostWithCommentsCountResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.RankingRequest;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;

@Service
@AllArgsConstructor
public class RankingService {

    private final IAuthenticationFacade authenticationFacade;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Page<PostWithCommentsCountResponse> searchRanking(Pageable pageable,
        PostSearchRequest postSearchFilter, RankingRequest rankingRequest) {
        PostSearch postSearch = postSearchFilter.toPostSearch();
        RankingCriteria rankingCriteria = RankingCriteria.of(rankingRequest.getCriteria());

        User user = (User) authenticationFacade.getPrincipal();
        Page<Post> posts = getRankingByFilter(pageable, postSearch, rankingCriteria);

        return posts.map(post -> PostWithCommentsCountResponse.of(user, post));
    }

    private Page<Post> getRankingByFilter(Pageable pageable, PostSearch postSearch,
        RankingCriteria rankingCriteria) {
        LocalDateTime startDate = rankingCriteria.getStartDate();
        LocalDateTime endDate = rankingCriteria.getEndDate();

        if (postSearch.isNotExistsFilter()) {
            return postRepository.findRankingBy(pageable, startDate, endDate);
        }

        if (postSearch.isAreaFilter()) {
            return postRepository.findRankingByAreaId(pageable, postSearch.getAreaId(), startDate,
                endDate);
        }
        if (postSearch.isSectorFilter()) {
            return postRepository.findRankingBySectorIds(pageable, postSearch.getSectorIds(),
                startDate, endDate);
        }
        return postRepository.findRankingByAreaIdAndSectorIds(pageable, postSearch.getAreaId(),
            postSearch.getSectorIds(), startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Post> searchRanking(PostSearchRequest postSearchFilter,
        RankingRequest rankingRequest, int postCount) {
        PostSearch postSearch = postSearchFilter.toPostSearch();
        RankingCriteria rankingCriteria = RankingCriteria.of(rankingRequest.getCriteria());

        List<Post> posts = getRankingByFilter(postSearch, rankingCriteria, postCount);

        return Collections.unmodifiableList(posts);
    }

    private List<Post> getRankingByFilter(PostSearch postSearch, RankingCriteria rankingCriteria,
        int postCount) {
        LocalDateTime startDate = rankingCriteria.getStartDate();
        LocalDateTime endDate = rankingCriteria.getEndDate();

        if (postSearch.isNotExistsFilter()) {
            return postRepository.findRankingBy(startDate, endDate, postCount);
        }
        if (postSearch.isAreaFilter()) {
            return postRepository.findRankingByAreaId(postSearch.getAreaId(), startDate,
                endDate, postCount);
        }
        if (postSearch.isSectorFilter()) {
            return postRepository.findRankingBySectorIds(postSearch.getSectorIds(),
                startDate, endDate, postCount);
        }
        return postRepository.findRankingByAreaIdAndSectorIds(postSearch.getAreaId(),
            postSearch.getSectorIds(), startDate, endDate, postCount);
    }
}
