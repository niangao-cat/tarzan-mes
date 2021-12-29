package tarzan.calendar.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/12/9 10:46
 * @Description:
 */
public class MtCalendarOrgRelDTO1 implements Serializable {
    private static final long serialVersionUID = -404469706034758464L;

    @ApiModelProperty("日历唯一标识")
    private String calendarId;

    @ApiModelProperty("顶层站点ID")
    private String topSiteId;

    @ApiModelProperty(value = "组织ID")
    private String calendarOrganizationId;

    @ApiModelProperty(value = "组织类型")
    private String organizationType;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getTopSiteId() {
        return topSiteId;
    }

    public void setTopSiteId(String topSiteId) {
        this.topSiteId = topSiteId;
    }

    public String getCalendarOrganizationId() {
        return calendarOrganizationId;
    }

    public void setCalendarOrganizationId(String calendarOrganizationId) {
        this.calendarOrganizationId = calendarOrganizationId;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
