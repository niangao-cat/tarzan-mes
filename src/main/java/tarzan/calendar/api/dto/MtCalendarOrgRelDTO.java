package tarzan.calendar.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtCalendarOrgRelDTO implements Serializable {
    private static final long serialVersionUID = 2074745310452097435L;

    @ApiModelProperty("作为日历组织关系唯一标识，主键")
    private String calendarOrgRelId;

    @ApiModelProperty(value = "作为日历唯一标识，用于其他数据结构引用")
    private String calendarId;

    @ApiModelProperty(value = "日历编码")
    private String calendarCode;

    @ApiModelProperty(value = "日历描述")
    private String calendarDesc;

    @ApiModelProperty(value = "日历类型")
    private String calendarType;

    @ApiModelProperty(value = "日历类型描述")
    private String calendarTypeDesc;

    @ApiModelProperty(value = "组织Id")
    private String organizationId;

    @ApiModelProperty(value = "组织编码")
    private String organizationCode;

    @ApiModelProperty(value = "组织描述")
    private String organizationDesc;

    @ApiModelProperty(value = "组织类型")
    private String organizationType;

    @ApiModelProperty(value = "组织类型描述")
    private String organizationTypeDesc;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    public String getCalendarOrgRelId() {
        return calendarOrgRelId;
    }

    public void setCalendarOrgRelId(String calendarOrgRelId) {
        this.calendarOrgRelId = calendarOrgRelId;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getCalendarCode() {
        return calendarCode;
    }

    public void setCalendarCode(String calendarCode) {
        this.calendarCode = calendarCode;
    }

    public String getCalendarDesc() {
        return calendarDesc;
    }

    public void setCalendarDesc(String calendarDesc) {
        this.calendarDesc = calendarDesc;
    }

    public String getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(String calendarType) {
        this.calendarType = calendarType;
    }

    public String getCalendarTypeDesc() {
        return calendarTypeDesc;
    }

    public void setCalendarTypeDesc(String calendarTypeDesc) {
        this.calendarTypeDesc = calendarTypeDesc;
    }

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

    public String getOrganizationDesc() {
        return organizationDesc;
    }

    public void setOrganizationDesc(String organizationDesc) {
        this.organizationDesc = organizationDesc;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getOrganizationTypeDesc() {
        return organizationTypeDesc;
    }

    public void setOrganizationTypeDesc(String organizationTypeDesc) {
        this.organizationTypeDesc = organizationTypeDesc;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
