package tarzan.general.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-09-26 13:55
 */
public class MtTagHisDTO1 implements Serializable {
    private static final long serialVersionUID = 2468321090407216315L;
    @ApiModelProperty(value = "数据项ID")
    private String tagId;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public Date getStartTime() {
        if (startTime == null) {
            return null;
        } else {
            return (Date) startTime.clone();
        }
    }

    public void setStartTime(Date startTime) {
        if (startTime == null) {
            this.startTime = null;
        } else {
            this.startTime = (Date) startTime.clone();
        }
    }

    public Date getEndTime() {
        if (endTime == null) {
            return null;
        } else {
            return (Date) endTime.clone();
        }
    }

    public void setEndTime(Date endTime) {
        if (endTime == null) {
            this.endTime = null;
        } else {
            this.endTime = (Date) endTime.clone();
        }
    }
}
