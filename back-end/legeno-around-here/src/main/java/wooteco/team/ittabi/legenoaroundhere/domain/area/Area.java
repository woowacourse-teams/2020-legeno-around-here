package wooteco.team.ittabi.legenoaroundhere.domain.area;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.Getter;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;

@Getter
@Entity
public class Area extends BaseEntity {

    private static final int MIN_SIZE = 1;
    static final String NOT_ALLOWED_NULL = "이름에 null은 허용되지 않습니다.";
    static final String INVALID_SIZE_ERROR = String
        .format("region의 갯수가 최소 갯수인 %d보다 작습니다.", MIN_SIZE);

    @Column
    private String code;

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<RegionalRelationship> regionalRelationships;

    public Area() {
    }

    protected Area(String code) {
        this.code = code;
        this.regionalRelationships = new ArrayList<>();
    }

    protected Area(String code,
        List<RegionalRelationship> regionalRelationships) {
        validate(regionalRelationships);
        this.code = code;
        this.regionalRelationships = regionalRelationships;
    }

    public void addRegion(RegionalRelationship regionalRelationship) {
        this.regionalRelationships.add(regionalRelationship);
    }

//    //todo: check
//    Region getSmallestRegion() {
//        RegionDepth smallestRegionDepth = regionalRelationships.keySet().stream()
//            .max(RegionDepth::compareTo)
//            .orElseThrow(() -> new IllegalStateException("Region이 존재하지 않습니다."));
//        RegionalRelationship regionalRelationship = regionalRelationships.get(smallestRegionDepth);
//        return regionalRelationship.getRegion();
//    }

    private void validate(List<RegionalRelationship> regions) {
        validateNull(regions);
        validateSize(regions);
    }

    private void validateNull(List<RegionalRelationship> regions) {
        if (Objects.isNull(regions)) {
            throw new IllegalArgumentException(NOT_ALLOWED_NULL);
        }
    }

    private void validateSize(List<RegionalRelationship> regions) {
        if (isNotValidSize(regions)) {
            throw new IllegalArgumentException(INVALID_SIZE_ERROR);
        }
    }

    private boolean isNotValidSize(List<RegionalRelationship> regions) {
        return regions.size() < MIN_SIZE;
    }
}
