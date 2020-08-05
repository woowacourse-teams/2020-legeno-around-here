package wooteco.team.ittabi.legenoaroundhere.domain.area;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public abstract class Area extends BaseEntity {

    private static final int MIN_SIZE = 1;
    static final String NOT_ALLOWED_NULL = "이름에 null은 허용되지 않습니다.";
    static final String INVALID_SIZE_ERROR = String
        .format("region의 갯수가 최소 갯수인 %d보다 작습니다.", MIN_SIZE);

    @Column(nullable = false, unique = true)
    private String code;

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "region.depth")
    @MapKeyEnumerated(EnumType.STRING)
    private Map<RegionDepth, RegionalRelationship> regionalRelationships;

    protected Area(String code) {
        this.code = code;
        this.regionalRelationships = new HashMap<>();
    }

    protected Area(String code,
        Map<RegionDepth, RegionalRelationship> regionalRelationships) {
        validate(regionalRelationships);
        this.code = code;
        this.regionalRelationships = regionalRelationships;
    }

    private void validate(Map<RegionDepth, RegionalRelationship> regions) {
        validateNull(regions);
        validateSize(regions);
    }

    private void validateNull(Map<RegionDepth, RegionalRelationship> regions) {
        if (Objects.isNull(regions)) {
            throw new IllegalArgumentException(NOT_ALLOWED_NULL);
        }
    }

    private void validateSize(Map<RegionDepth, RegionalRelationship> regions) {
        if (isNotValidSize(regions)) {
            throw new IllegalArgumentException(INVALID_SIZE_ERROR);
        }
    }

    private boolean isNotValidSize(Map<RegionDepth, RegionalRelationship> regions) {
        return regions.size() < MIN_SIZE;
    }

    void addRegions(List<Region> regions) {
        this.regionalRelationships = new HashMap<>(this.regionalRelationships);
        for (Region region : regions) {
            this.regionalRelationships.put(region.getDepth(), new RegionalRelationship(this, region));
        }
    }

    Region getSmallestRegion() {
        RegionDepth smallestRegionDepth = regionalRelationships.keySet().stream()
            .max(RegionDepth::compareTo)
            .orElseThrow(() -> new IllegalStateException("Region이 존재하지 않습니다."));
        RegionalRelationship regionalRelationship = regionalRelationships.get(smallestRegionDepth);
        return regionalRelationship.getRegion();
    }

    @Override
    public String toString() {
        return "Area{" +
            "code='" + code + '\'' +
            '}';
    }
}
