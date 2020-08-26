package wooteco.team.ittabi.legenoaroundhere.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllBy(Pageable pageable);

    Page<Post> findAllByCreator(Pageable pageable, User creator);

    @Query(value = ""
        + "SELECT * "
        + "  FROM post "
        + " WHERE deleted_at IS NULL "
        + "   AND area_id IN (SELECT id "
        + "                     FROM area "
        + "                    WHERE deleted_at IS NULL "
        + "                      AND full_name LIKE (SELECT CONCAT(full_name,'%') "
        + "                                            FROM area "
        + "                                           WHERE deleted_at IS NULL "
        + "                                             AND id = :areaId))", nativeQuery = true)
    Page<Post> findAllByAreaId(Pageable pageable, @Param("areaId") Long areaId);

    @Query("SELECT p FROM Post p WHERE p.sector.id IN :sectorIds")
    Page<Post> findAllBySectorIds(Pageable pageable, @Param("sectorIds") List<Long> sectorIds);

    @Query(value = ""
        + "SELECT * "
        + "  FROM post "
        + " WHERE deleted_at IS NULL "
        + "   AND area_id IN (SELECT id "
        + "                     FROM area "
        + "                    WHERE deleted_at IS NULL "
        + "                      AND full_name LIKE (SELECT CONCAT(full_name,'%') "
        + "                                            FROM area "
        + "                                           WHERE deleted_at IS NULL "
        + "                                             AND id = :areaId)) "
        + "   AND sector_id IN (SELECT id "
        + "                       FROM sector "
        + "                      WHERE deleted_at IS NULL "
        + "                        AND id IN :sectorIds)", nativeQuery = true)
    Page<Post> findAllByAreaIdsAndSectorIds(Pageable pageable, @Param("areaId") Long areaId,
        @Param("sectorIds") List<Long> sectorIds);

    @Query(value = ""
        + "SELECT post.* "
        + "  FROM (SELECT * "
        + "          FROM post "
        + "         WHERE deleted_at IS NULL) AS post"
        + "  LEFT JOIN (SELECT post_id "
        + "                  , count(post_id) as count "
        + "               FROM post_zzang "
        + "              WHERE :startDate <= created_at "
        + "                AND created_at < :endDate "
        + "           GROUP BY post_id) AS zzang "
        + "    ON post.id = zzang.post_id "
        + " ORDER BY zzang.count DESC, id DESC "
        + "", nativeQuery = true)
    Page<Post> findRankingBy(Pageable pageable, @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);

    @Query(value = ""
        + "SELECT post.* "
        + "  FROM (SELECT * "
        + "          FROM post "
        + "         WHERE deleted_at IS NULL "
        + "           AND area_id IN (SELECT id "
        + "                             FROM area "
        + "                            WHERE deleted_at IS NULL "
        + "                              AND full_name LIKE (SELECT CONCAT(full_name,'%') "
        + "                                                    FROM area "
        + "                                                   WHERE deleted_at IS NULL "
        + "                                                     AND id = :areaId))) AS post"
        + "  LEFT JOIN (SELECT post_id "
        + "                  , count(post_id) as count "
        + "               FROM post_zzang "
        + "              WHERE :startDate <= created_at "
        + "                AND created_at < :endDate "
        + "           GROUP BY post_id) AS zzang "
        + "    ON post.id = zzang.post_id "
        + " ORDER BY zzang.count DESC, id DESC "
        + "", nativeQuery = true)
    Page<Post> findRankingByAreaId(Pageable pageable, @Param("areaId") Long areaId,
        @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = ""
        + "SELECT post.* "
        + "  FROM (SELECT * "
        + "          FROM post "
        + "         WHERE deleted_at IS NULL "
        + "           AND sector_id IN (SELECT id "
        + "                               FROM sector "
        + "                              WHERE deleted_at IS NULL "
        + "                                AND id IN :sectorIds)) AS post"
        + "  LEFT JOIN (SELECT post_id "
        + "                  , count(post_id) as count "
        + "               FROM post_zzang "
        + "              WHERE :startDate <= created_at "
        + "                AND created_at < :endDate "
        + "           GROUP BY post_id) AS zzang "
        + "    ON post.id = zzang.post_id "
        + "  WHERE 1=1 "
        + " ORDER BY zzang.count DESC, id DESC "
        + "", nativeQuery = true)
    Page<Post> findRankingBySectorIds(Pageable pageable, @Param("sectorIds") List<Long> sectorIds,
        @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = ""
        + "SELECT post.* "
        + "  FROM (SELECT * "
        + "          FROM post "
        + "         WHERE deleted_at IS NULL "
        + "           AND area_id IN (SELECT id "
        + "                             FROM area "
        + "                            WHERE deleted_at IS NULL "
        + "                              AND full_name LIKE (SELECT CONCAT(full_name,'%') "
        + "                                                    FROM area "
        + "                                                   WHERE deleted_at IS NULL "
        + "                                                     AND id = :areaId)) "
        + "           AND sector_id IN (SELECT id "
        + "                               FROM sector "
        + "                              WHERE deleted_at IS NULL "
        + "                                AND id IN :sectorIds)) AS post"
        + "  LEFT JOIN (SELECT post_id "
        + "                  , count(post_id) as count "
        + "               FROM post_zzang "
        + "              WHERE :startDate <= created_at "
        + "                AND created_at < :endDate "
        + "           GROUP BY post_id) AS zzang "
        + "    ON post.id = zzang.post_id "
        + " ORDER BY zzang.count DESC, id DESC "
        + "", nativeQuery = true)
    Page<Post> findRankingByAreaIdAndSectorIds(Pageable pageable, @Param("areaId") Long areaId,
        @Param("sectorIds") List<Long> sectorIds, @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);
}
