package tarzan.calendar.domain.vo;

import java.io.Serializable;

public class MtCalendarVO implements Serializable {
    private static final long serialVersionUID = 6360474798867062710L;
    private String organizationType; // 组织类型
    private String organizationId; // 对应的组织代码ID

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
