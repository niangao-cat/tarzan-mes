package tarzan.modeling.api.dto;

import java.io.Serializable;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModOrganizationRelDTO2 implements Serializable {

    private static final long serialVersionUID = 8008166552276257779L;

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


    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getTopSiteId() {
        return topSiteId;
    }

    public void setTopSiteId(String topSiteId) {
        this.topSiteId = topSiteId;
    }

    private String parentOrganizationType;
    private String parentOrganizationId;
    private String areaId;
    private String topSiteId;
}
