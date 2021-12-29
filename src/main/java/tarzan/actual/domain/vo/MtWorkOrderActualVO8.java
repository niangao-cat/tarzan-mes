package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/10/26 15:54
 */
public class MtWorkOrderActualVO8 implements Serializable {
    private static final long serialVersionUID = -4228046039012818368L;
    @ApiModelProperty(value = "事件Id")
    private String eventId;
    @ApiModelProperty(value = "实绩列表")
    private List<ActualInfo> actualInfoList;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<ActualInfo> getActualInfoList() {
        return actualInfoList;
    }

    public void setActualInfoList(List<ActualInfo> actualInfoList) {
        this.actualInfoList = actualInfoList;
    }

    public static class ActualInfo implements Serializable {
        private static final long serialVersionUID = 5081143405222210582L;
        @ApiModelProperty(value = "工单实绩")
        private String workOrderActualId;

        @ApiModelProperty(value = "工单")
        private String workOrderId;

        @ApiModelProperty(value = "生成EO的数量")
        private Double releasedQty;

        @ApiModelProperty(value = "完成数量")
        private Double completedQty;

        @ApiModelProperty(value = "报废数量")
        private Double scrappedQty;

        @ApiModelProperty(value = "保留数量")
        private Double holdQty;

        @ApiModelProperty(value = "实际开始时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date actualStartDate;

        @ApiModelProperty(value = "实际完成时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date actualEndDate;

        public String getWorkOrderActualId() {
            return workOrderActualId;
        }

        public void setWorkOrderActualId(String workOrderActualId) {
            this.workOrderActualId = workOrderActualId;
        }

        public String getWorkOrderId() {
            return workOrderId;
        }

        public void setWorkOrderId(String workOrderId) {
            this.workOrderId = workOrderId;
        }

        public Double getReleasedQty() {
            return releasedQty;
        }

        public void setReleasedQty(Double releasedQty) {
            this.releasedQty = releasedQty;
        }

        public Double getCompletedQty() {
            return completedQty;
        }

        public void setCompletedQty(Double completedQty) {
            this.completedQty = completedQty;
        }

        public Double getScrappedQty() {
            return scrappedQty;
        }

        public void setScrappedQty(Double scrappedQty) {
            this.scrappedQty = scrappedQty;
        }

        public Double getHoldQty() {
            return holdQty;
        }

        public void setHoldQty(Double holdQty) {
            this.holdQty = holdQty;
        }

        public Date getActualStartDate() {
            if (actualStartDate == null) {
                return null;
            } else {
                return (Date) actualStartDate.clone();
            }
        }

        public void setActualStartDate(Date actualStartDate) {
            if (actualStartDate == null) {
                this.actualStartDate = null;
            } else {
                this.actualStartDate = (Date) actualStartDate.clone();
            }
        }

        public Date getActualEndDate() {
            if (actualEndDate == null) {
                return null;
            } else {
                return (Date) actualEndDate.clone();
            }
        }

        public void setActualEndDate(Date actualEndDate) {
            if (actualEndDate == null) {
                this.actualEndDate = null;
            } else {
                this.actualEndDate = (Date) actualEndDate.clone();
            }
        }
    }
}
