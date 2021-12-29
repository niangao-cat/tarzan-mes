package tarzan.modeling.domain.vo;

import java.io.Serializable;

public class MtModOrganizationRelVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2461149423370763558L;
    private String organizationType;
    private String organizationId;
    private String siteType;

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    @Override
    public String toString() {
        return "MtModOrganizationRelVO{" +
                "organizationType='" + organizationType + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", siteType='" + siteType + '\'' +
                '}';
    }
}
