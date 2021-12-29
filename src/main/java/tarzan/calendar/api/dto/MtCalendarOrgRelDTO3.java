package tarzan.calendar.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/12/10 17:32
 * @Description:
 */
public class MtCalendarOrgRelDTO3 implements Serializable {
    private static final long serialVersionUID = 2007418143164041048L;

    @ApiModelProperty("关系ID")
    private String calendarOrgRelId;

    @ApiModelProperty("组织ID")
    private String organizationId;

    @ApiModelProperty("组织类型")
    private String organizationType;

    @ApiModelProperty("有效性")
    private String enableFlag;

    public String getCalendarOrgRelId() {
        return calendarOrgRelId;
    }

    public void setCalendarOrgRelId(String calendarOrgRelId) {
        this.calendarOrgRelId = calendarOrgRelId;
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

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
