package tarzan.modeling.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/11/11 9:58
 * @Description:
 */
public class MtModOrganizationSingleNodeVO implements Serializable {
    private static final long serialVersionUID = 8527017830493921549L;

    private String topSiteId;
    private String parentOrganizationType;
    private String parentOrganizationId;
    private String isOnhand;

    public String getTopSiteId() {
        return topSiteId;
    }

    public void setTopSiteId(String topSiteId) {
        this.topSiteId = topSiteId;
    }

    public String getParentOrganizationType() {
        return parentOrganizationType;
    }

    public void setParentOrganizationType(String parentOrganizationType) {
        this.parentOrganizationType = parentOrganizationType;
    }

    public String getParentOrganizationId() {
        return parentOrganizationId;
    }

    public void setParentOrganizationId(String parentOrganizationId) {
        this.parentOrganizationId = parentOrganizationId;
    }

    public String getIsOnhand() {
        return isOnhand;
    }

    public void setIsOnhand(String isOnhand) {
        this.isOnhand = isOnhand;
    }

    @Override
    public String toString() {
        return "MtModOrganizationSingleNodeVO{" +
                "topSiteId='" + topSiteId + '\'' +
                ", parentOrganizationType='" + parentOrganizationType + '\'' +
                ", parentOrganizationId='" + parentOrganizationId + '\'' +
                ", isOnhand='" + isOnhand + '\'' +
                '}';
    }
}
