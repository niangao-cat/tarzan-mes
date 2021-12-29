package tarzan.general.api.dto;

import java.io.Serializable;

public class MtOrganizationDTO implements Serializable {
    private static final long serialVersionUID = 1592829548986664012L;

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationDesc() {
        return organizationDesc;
    }

    public void setOrganizationDesc(String organizationDesc) {
        this.organizationDesc = organizationDesc;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    private String organizationCode;
    private String organizationDesc;
    private String organizationType;
    private String organizationId;
}
