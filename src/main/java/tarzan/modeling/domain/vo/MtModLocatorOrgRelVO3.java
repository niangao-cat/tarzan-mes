package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author liujunhui
 * @Classname MtModLocatorOrgRelVO3
 * @Description TODO
 * @Date 2019/10/10 20:15
 */
public class MtModLocatorOrgRelVO3 implements Serializable {
    private static final long serialVersionUID = 5910109812586838376L;
    private String organizationId;

    private String organizationType;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtModLocatorOrgRelVO3 that = (MtModLocatorOrgRelVO3) o;
        return Objects.equals(organizationId, that.organizationId)
                        && Objects.equals(organizationType, that.organizationType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizationId, organizationType);
    }
}
