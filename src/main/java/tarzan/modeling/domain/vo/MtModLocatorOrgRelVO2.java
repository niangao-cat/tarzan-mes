package tarzan.modeling.domain.vo;

import java.io.Serializable;

/**
 * @author benjamin
 */
public class MtModLocatorOrgRelVO2 implements Serializable {
    private static final long serialVersionUID = -5743519021410452199L;

    /**
     * 库位Id
     */
    private String locatorId;
    /**
     * 组织类型
     */
    private String organizationType;
    /**
     * 站点类型
     */
    private String siteType;

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }
}
