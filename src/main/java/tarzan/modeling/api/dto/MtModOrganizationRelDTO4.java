package tarzan.modeling.api.dto;

import java.io.Serializable;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModOrganizationRelDTO4 implements Serializable {

    private static final long serialVersionUID = -7253427173750420017L;

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


    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    public String getTopSiteId() {
        return topSiteId;
    }

    public void setTopSiteId(String topSiteId) {
        this.topSiteId = topSiteId;
    }

    private String parentOrganizationType;
    private String parentOrganizationId;
    private String prodLineId;
    private String topSiteId;
}
