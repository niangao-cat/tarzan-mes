package tarzan.calendar.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/12/17 7:01 下午
 */
public class MtCalendarVO7 implements Serializable {
    private static final long serialVersionUID = -5572511312114022723L;
    @ApiModelProperty("工作日历ID")
    private String calendarId;
    @ApiModelProperty("组织ID")
    private String organizationId;

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

    public MtCalendarVO7() {
    }

    public MtCalendarVO7(String calendarId, String organizationId) {
        this.calendarId = calendarId;
        this.organizationId = organizationId;
    }
}
