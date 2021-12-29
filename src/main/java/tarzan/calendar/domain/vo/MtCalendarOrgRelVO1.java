package tarzan.calendar.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Date: 2019/9/19 20:16
 * @Author: {yiyang.xie@hand-china.com}
 */
public class MtCalendarOrgRelVO1 implements Serializable {
    private static final long serialVersionUID = 2074745310452097435L;

    @ApiModelProperty("日历唯一标识")
    @NotNull
    private String calendarId;

    @ApiModelProperty("业务组织实体")
    @NotNull
    private String organizationId;

    @ApiModelProperty("业务组织实体类型")
    @NotNull
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

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
