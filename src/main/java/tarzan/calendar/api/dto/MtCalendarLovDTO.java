package tarzan.calendar.api.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/12/6 9:58
 * @Description:
 */
public class MtCalendarLovDTO implements Serializable {
    private static final long serialVersionUID = 8912075295613204191L;

    @ApiModelProperty("作为日历唯一标识，用于其他数据结构引用")
    private String calendarId;

    @ApiModelProperty(value = "该日历编码")
    private String calendarCode;

    @ApiModelProperty(value = "TYPE_GROUP:CALENDAR_TYPE：1.【PLAN】 2.【STANDARD】3.【PURCHASE】")
    private String calendarType;

    @ApiModelProperty(value = "类型描述")
    private String calendarTypeDesc;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否有效。默认为“N”")
    private String enableFlag;


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

    public String getCalendarTypeDesc() {
        return calendarTypeDesc;
    }

    public void setCalendarTypeDesc(String calendarTypeDesc) {
        this.calendarTypeDesc = calendarTypeDesc;
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
}
