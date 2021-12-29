package tarzan.calendar.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/12/9 10:46
 * @Description:
 */
public class MtCalendarOrgRelDTO2 implements Serializable {

    private static final long serialVersionUID = 6063693118124168755L;
    @ApiModelProperty("日历唯一标识")
    private String calendarId;

    @ApiModelProperty("日历组织关系集合")
    private List<MtCalendarOrgRelDTO3> calendarOrgRelList;

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public List<MtCalendarOrgRelDTO3> getCalendarOrgRelList() {
        return calendarOrgRelList;
    }

    public void setCalendarOrgRelList(List<MtCalendarOrgRelDTO3> calendarOrgRelList) {
        this.calendarOrgRelList = calendarOrgRelList;
    }
}
