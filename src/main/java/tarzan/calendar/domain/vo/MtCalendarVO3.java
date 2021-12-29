package tarzan.calendar.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtCalendarVO3
 * @description
 * @date 2019年09月19日 13:51
 */
public class MtCalendarVO3 implements Serializable {
    private static final long serialVersionUID = 6248810466937798796L;

    @ApiModelProperty("作为日历唯一标识，用于其他数据结构引用")
    private String calendarId;
    @ApiModelProperty(value = "该日历编码", required = true)
    private String calendarCode;
    @ApiModelProperty(value = "TYPE_GROUP:CALENDAR_TYPE：1.【PLAN】 2.【STANDARD】3.【PURCHASE】", required = true)
    private String calendarType;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "是否有效。默认为“N”", required = true)
    private String enableFlag;
    @ApiModelProperty(value = "日历组织关系")
    private List<MtCalendarOrgRelVO> calendarOrgRelList;

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

    public String getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(String calendarType) {
        this.calendarType = calendarType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public List<MtCalendarOrgRelVO> getCalendarOrgRelList() {
        return calendarOrgRelList;
    }

    public void setCalendarOrgRelList(List<MtCalendarOrgRelVO> calendarOrgRelList) {
        this.calendarOrgRelList = calendarOrgRelList;
    }
}
