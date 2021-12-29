package tarzan.calendar.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/14 23:46
 * @Author: ${yiyang.xie}
 */
public class MtCalendarShiftVO11 implements Serializable {
    private static final long serialVersionUID = -1500860477774447095L;
    @ApiModelProperty("日历班次")
    private String calendarShiftId;
    @ApiModelProperty("组织类型")
    private String organizationType;
    @ApiModelProperty("组织ID")
    private String organizationId;
    @ApiModelProperty("日历类型")
    private String calendarType;

    public String getCalendarShiftId() {
        return calendarShiftId;
    }

    public void setCalendarShiftId(String calendarShiftId) {
        this.calendarShiftId = calendarShiftId;
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

    public String getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(String calendarType) {
        this.calendarType = calendarType;
    }
}
