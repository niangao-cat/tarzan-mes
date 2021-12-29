package tarzan.general.api.dto;

import java.io.Serializable;

public class MtUserOrganizationDTO implements Serializable {

    private static final long serialVersionUID = 1779750974244748757L;
    private Long userId;
    private String organizationType;

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
}
