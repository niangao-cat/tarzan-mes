package tarzan.calendar.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/10 13:57
 * @Author: ${yiyang.xie}
 */
public class MtCalendarVO4 implements Serializable {
    private static final long serialVersionUID = -552318338915216644L;
    @ApiModelProperty("日历ID")
    private String calendarId;
    @ApiModelProperty("日历类型")
    private String calendarType;
    @ApiModelProperty("是否有效")
    private String enableFlag;

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

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
