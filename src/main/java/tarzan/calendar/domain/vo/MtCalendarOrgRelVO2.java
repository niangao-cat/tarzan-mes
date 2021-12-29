package tarzan.calendar.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/9/26 12:59
 * @Author: {yiyang.xie@hand-china.com}
 */
public class MtCalendarOrgRelVO2 implements Serializable {
    private static final long serialVersionUID = -6030826379083527709L;

    @ApiModelProperty("日历组织关系唯一标识")
    private String calendarOrgRelId;

    @ApiModelProperty("日历唯一标识")
    private String calendarId;

    @ApiModelProperty("业务组织实体")
    private String organizationId;

    @ApiModelProperty("业务组织实体类型")
    private String organizationType;

    @ApiModelProperty("是否有效")
    private String enableFlag;

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getCalendarOrgRelId() {
        return calendarOrgRelId;
    }

    public void setCalendarOrgRelId(String calendarOrgRelId) {
        this.calendarOrgRelId = calendarOrgRelId;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
