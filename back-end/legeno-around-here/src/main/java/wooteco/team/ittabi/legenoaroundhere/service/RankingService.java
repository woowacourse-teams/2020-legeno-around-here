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
    public Page<PostWithCommentsCountResponse> searchRanking2(Pageable pageable,
        PostSearchRequest postSearchFilter, RankingRequest rankingRequest) {
        PostSearch postSearch = postSearchFilter.toPostSearch();
        RankingCriteria rankingCriteria = RankingCriteria.of(rankingRequest.getCriteria());

        User user = (User) authenticationFacade.getPrincipal();
        Page<Post> posts = getPostByFilter(pageable, postSearch, rankingCriteria);

        return posts.map(post -> PostWithCommentsCountResponse.of(user, post));
    }

    private Page<Post> getPostByFilter(Pageable pageable, PostSearch postSearch,
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
    public Page<PostWithCommentsCountResponse> searchRanking(Pageable pageable,
        RankingRequest rankingRequest, PostSearchRequest postSearchFilter) {

        Page<Post> allDateZzangPosts = getPostByFilter(pageable, postSearchFilter.toPostSearch());
        List<Post> filteredDateZzangPosts
            = getPostsThatZzangFilteredByDate(rankingRequest.getCriteria(), allDateZzangPosts);
        Collections.reverse(filteredDateZzangPosts);

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

    private Page<Post> getPostByFilter(Pageable pageable, PostSearch postSearch) {
        if (postSearch.isNotExistsFilter()) {
            return postRepository.findAllBy(pageable);
        }

        if (postSearch.isAreaFilter()) {
            return postRepository.findAllByAreaId(pageable, postSearch.getAreaId());
        }
        if (postSearch.isSectorFilter()) {
            return postRepository.findAllBySectorIds(pageable, postSearch.getSectorIds());
        }
        return postRepository.findAllByAreaIdsAndSectorIds(pageable, postSearch.getAreaId(),
            postSearch.getSectorIds());
    }
}
