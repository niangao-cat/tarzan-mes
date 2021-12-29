package tarzan.calendar.api.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/12/11 10:37
 * @Description:
 */
public class MtCalendarOrgRelDTO5 implements Serializable {
    private static final long serialVersionUID = 3259596613695316917L;

    @ApiModelProperty("顶层站点ID")
    private String topSiteId;

    @ApiModelProperty("父组织类型")
    private String parentOrganizationType;

    @ApiModelProperty("父组织ID")
    private String parentOrganizationId;

    @ApiModelProperty("请求是否为点击查询按钮方式")
    private String isBtnQuery;

    @ApiModelProperty("日历ID")
    private String calendarId;

    @ApiModelProperty("日历类型")
    private String calendarType;

    public String getTopSiteId() {
        return topSiteId;
    }

    public void setTopSiteId(String topSiteId) {
        this.topSiteId = topSiteId;
    }

    public String getParentOrganizationType() {
        return parentOrganizationType;
    }

    public void setParentOrganizationType(String parentOrganizationType) {
        this.parentOrganizationType = parentOrganizationType;
    }

    public String getParentOrganizationId() {
        return parentOrganizationId;
    }

    public void setParentOrganizationId(String parentOrganizationId) {
        this.parentOrganizationId = parentOrganizationId;
    }

    public String getIsBtnQuery() {
        return isBtnQuery;
    }

    public void setIsBtnQuery(String isBtnQuery) {
        this.isBtnQuery = isBtnQuery;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(String calendarType) {
        this.calendarType = calendarType;
    }
}
