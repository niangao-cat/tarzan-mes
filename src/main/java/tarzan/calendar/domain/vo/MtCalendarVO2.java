package tarzan.calendar.domain.vo;

import java.io.Serializable;

public class MtCalendarVO2 implements Serializable {

    private static final long serialVersionUID = -4681410864867822149L;
    private String organizationType; // 组织类型
    private String organizationId; // 对应的组织代码ID
    private String siteType;
    private String calendarType;

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

    public String getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(String calendarType) {
        this.calendarType = calendarType;
    }
}
