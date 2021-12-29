package tarzan.calendar.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtCalendarOrgRelVO
 * @description
 * @date 2019年09月19日 11:06
 */
public class MtCalendarOrgRelVO implements Serializable {
    private static final long serialVersionUID = -4274842668794000410L;

    @ApiModelProperty("作为日历组织关系唯一标识，主键")
    private String calendarOrgRelId;
    @ApiModelProperty(value = "对应的组织代码ID",required = true)
    private String organizationId;
    @ApiModelProperty(value = "如SITE\\AREA\\PRODUCTIONLINE等，TYPE_GROUP:CALENDAR_ORG_TYPE",required = true)
    private String organizationType;
    @ApiModelProperty(value = "是否有效。默认为“N”",required = true)
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
