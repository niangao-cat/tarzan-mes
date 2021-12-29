package tarzan.modeling.api.dto;

import java.io.Serializable;

public class MtModOrganizationDTO4 implements Serializable {
    private static final long serialVersionUID = 2596644912067277951L;
    private String organizationId;
    private String organizationCode;
    private String organizationName;
    private String organizationTypeDesc;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationTypeDesc() {
        return organizationTypeDesc;
    }

    public void setOrganizationTypeDesc(String organizationTypeDesc) {
        this.organizationTypeDesc = organizationTypeDesc;
    }
}
