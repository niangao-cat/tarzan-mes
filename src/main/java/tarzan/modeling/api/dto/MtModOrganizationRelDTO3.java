package tarzan.modeling.api.dto;

import java.io.Serializable;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModOrganizationRelDTO3 implements Serializable {

    private static final long serialVersionUID = 2153789316204369895L;

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


    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getTopSiteId() {
        return topSiteId;
    }

    public void setTopSiteId(String topSiteId) {
        this.topSiteId = topSiteId;
    }

    private String parentOrganizationType;
    private String parentOrganizationId;
    private String workcellId;
    private String topSiteId;
}
