package wooteco.team.ittabi.legenoaroundhere.domain.area;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@ToString
@SQLDelete(sql = "UPDATE area SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Area extends BaseEntity {

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String firstDepthName;

    @Column(nullable = false)
    private String secondDepthName;

    @Column(nullable = false)
    private String thirdDepthName;

    @Column(nullable = false)
    private String fourthDepthName;

    public String getLastDepthName() {
        if (!fourthDepthName.isEmpty()) {
            return fourthDepthName;
        }
        if (!thirdDepthName.isEmpty()) {
            return thirdDepthName;
        }
        if (!secondDepthName.isEmpty()) {
            return secondDepthName;
        }
        return firstDepthName;
    }

    public boolean isIncludeId(List<Long> areaIds) {
        return areaIds.contains(this.getId());
    }

    public boolean isSubAreaOf(Area targetArea) {
        return equalOrTargetEmpty(this.firstDepthName, targetArea.firstDepthName)
            && equalOrTargetEmpty(this.secondDepthName, targetArea.secondDepthName)
            && equalOrTargetEmpty(this.thirdDepthName, targetArea.thirdDepthName)
            && equalOrTargetEmpty(this.fourthDepthName, targetArea.fourthDepthName);
    }

    private boolean equalOrTargetEmpty(String name, String targetName) {
        if (targetName.isEmpty() || targetName.equals(name)) {
            return true;
        }
        return false;
    }
}
