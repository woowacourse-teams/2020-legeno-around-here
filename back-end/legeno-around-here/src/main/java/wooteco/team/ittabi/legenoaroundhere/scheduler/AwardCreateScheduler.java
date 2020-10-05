package wooteco.team.ittabi.legenoaroundhere.scheduler;

import static wooteco.team.ittabi.legenoaroundhere.domain.ranking.RankingCriteria.LAST_MONTH;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.ranking.RankingCriteria;
import wooteco.team.ittabi.legenoaroundhere.dto.PostSearchRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.RankingRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorSimpleResponse;
import wooteco.team.ittabi.legenoaroundhere.service.AreaService;
import wooteco.team.ittabi.legenoaroundhere.service.AwardService;
import wooteco.team.ittabi.legenoaroundhere.service.RankingService;
import wooteco.team.ittabi.legenoaroundhere.service.SectorService;

@AllArgsConstructor
@Component
public class AwardCreateScheduler {

    private AwardService awardService;
    private RankingService rankingService;
    private SectorService sectorService;
    private AreaService areaService;

    @Scheduled(cron = "0 0 0 1 * *")    // cron : 0초 0시 0분 1일 *월 *요일 (매월 1일 0시 0분 0초)
    public void createPopularityPostCreatorAwards() {
        LocalDateTime awardingTime = LocalDateTime.now();

        List<SectorSimpleResponse> allAvailableSectors = sectorService.findAllAvailableSectors();
        List<Area> allAreas = areaService.findAllAreas();

        allAvailableSectors.forEach(sector ->
            allAreas.forEach(area ->
                createAwardAt(area.getId(), sector.getId(), 5, awardingTime, LAST_MONTH))
        );
    }

    /**
     * @param rankLimit : 몇위까지 가져올지. ex) Top3 를 원할 경우 rankLimit = 3
     */
    private void createAwardAt(Long areaId, Long sectorId, int rankLimit,
        LocalDateTime awardingTime, RankingCriteria rankingCriteria) {
        RankingRequest rankingRequest = new RankingRequest(LAST_MONTH.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(areaId,
            String.valueOf(sectorId));

        List<Post> posts = rankingService
            .searchRanking(postSearchRequest, rankingRequest, rankLimit);
        List<PostWithRank> postWithRankList = makePostWithRankingList(posts);

        postWithRankList.forEach(postWithRank -> awardService.givePopularPostAward(
            postWithRank.getPost(),
            postWithRank.getRank(),
            rankingCriteria,
            rankingCriteria.getStartDate(awardingTime).toLocalDate(),
            rankingCriteria.getEndDate(awardingTime).toLocalDate()
        ));
    }

    private List<PostWithRank> makePostWithRankingList(List<Post> posts) {
        List<PostWithRank> postWithRankList = new ArrayList<>();
        int beforePostZzangCount = -1;
        int beforePostRank = 0;

        for (int index = 0; index < posts.size(); index++) {
            Post post = posts.get(index);
            int rank;

            if (beforePostZzangCount == post.getPostZzangCount()) {
                rank = beforePostRank;
            } else if (beforePostZzangCount > post.getPostZzangCount()) {
                rank = index + 1;
            } else {    // 이전 글의 짱 갯수 < 현재 글의 짱 갯수
                throw new IllegalArgumentException(
                    "posts 매개변수는 짱 갯수가 많은 글부터 차례대로 정렬되어있어야 합니다.");
            }
            postWithRankList.add(new PostWithRank(rank, post));

            beforePostZzangCount = post.getPostZzangCount();
            beforePostRank = rank;
        }
        return Collections.unmodifiableList(postWithRankList);
    }

    static class PostWithRank {

        private int rank;
        private Post post;

        PostWithRank(int rank, Post post) {
            this.rank = rank;
            this.post = post;
        }

        int getRank() {
            return rank;
        }

        Post getPost() {
            return post;
        }
    }
}
