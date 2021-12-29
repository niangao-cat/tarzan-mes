package tarzan.calendar.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MtCalendarShiftVO5 implements Serializable {
    private static final long serialVersionUID = 7704089299057238353L;

    private String organizationType; // 业务组织实体类型
    private String organizationId; // 业务组织实体ID
    private String siteType; // 站点类型
    private String calendarType; // 日历类型
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate; // 班次日期
    private String shiftCode; // 班次编码
    private String isEnableFlag; // 是否仅获取有效班次

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

    public Date getShiftDate() {
        if (shiftDate == null) {
            return null;
        }
        return (Date) shiftDate.clone();
    }

    public void setShiftDate(Date shiftDate) {
        if (shiftDate == null) {
            this.shiftDate = null;
        } else {
            this.shiftDate = (Date) shiftDate.clone();
        }
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public String getIsEnableFlag() {
        return isEnableFlag;
    }

    public void setIsEnableFlag(String isEnableFlag) {
        this.isEnableFlag = isEnableFlag;
    }
}
