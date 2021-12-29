package tarzan.calendar.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/10 14:05
 * @Author: ${yiyang.xie}
 */
public class MtCalendarVO5 implements Serializable {
    private static final long serialVersionUID = -7107759409144844217L;
    @ApiModelProperty("日历ID")
    private String calendarId;
    @ApiModelProperty(value = "该日历编码")
    private String calendarCode;
    @ApiModelProperty(value = "日历类型")
    private String calendarType;
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
