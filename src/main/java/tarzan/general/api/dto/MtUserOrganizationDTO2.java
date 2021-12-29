package tarzan.general.api.dto;

import java.io.Serializable;

public class MtUserOrganizationDTO2 implements Serializable {
    private static final long serialVersionUID = -6772927420077825007L;
    private Long userId;
    private String organizationType;
    private String organizationId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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
}
