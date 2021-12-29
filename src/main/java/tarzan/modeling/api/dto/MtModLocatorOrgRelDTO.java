package tarzan.modeling.api.dto;

import java.io.Serializable;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModLocatorOrgRelDTO implements Serializable {
    private static final long serialVersionUID = -2362302268826480668L;
    private String locatorId;
    private String parentOrganizationType;
    private String parentOrganizationId;

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
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

}
