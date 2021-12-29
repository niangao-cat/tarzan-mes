package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/10/26 19:23
 */
public class MtEoRouterActualVO41 implements Serializable {
    private static final long serialVersionUID = -9167750698485931955L;

    @ApiModelProperty("返工标识")
    private String reworkStepFlag;

    @ApiModelProperty("原地重工标识")
    private String localReworkFlag;

    @ApiModelProperty("全量清除标识")
    private String allClearFlag;

    @ApiModelProperty("事件请求")
    private String eventRequestId;

    @ApiModelProperty("工作单元")
    private String workcellId;

    @ApiModelProperty("事件所属班次日期")
    private LocalDate shiftDate;

    @ApiModelProperty("事件班次代码")
    private String shiftCode;

    @ApiModelProperty("排队列表")
    private List<QueueProcessInfo> queueMessageList;

    public String getReworkStepFlag() {
        return reworkStepFlag;
    }

    public void setReworkStepFlag(String reworkStepFlag) {
        this.reworkStepFlag = reworkStepFlag;
    }

    public String getLocalReworkFlag() {
        return localReworkFlag;
    }

    public void setLocalReworkFlag(String localReworkFlag) {
        this.localReworkFlag = localReworkFlag;
    }

    public String getAllClearFlag() {
        return allClearFlag;
    }

    public void setAllClearFlag(String allClearFlag) {
        this.allClearFlag = allClearFlag;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public List<QueueProcessInfo> getQueueMessageList() {
        return queueMessageList;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public void setQueueMessageList(List<QueueProcessInfo> queueMessageList) {
        this.queueMessageList = queueMessageList;
    }

    public static class QueueProcessInfo implements Serializable {
        private static final long serialVersionUID = 4865730067480037912L;
        @ApiModelProperty("执行作业")
        private String eoId;

        @ApiModelProperty("前道步骤")
        private String previousStepId;

        @ApiModelProperty("来源步骤")
        private String sourceEoStepActualId;

        @ApiModelProperty("步骤ID")
        private String routerStepId;

        @ApiModelProperty("数量")
        private Double qty;

        public String getEoId() {
            return eoId;
        }

        public void setEoId(String eoId) {
            this.eoId = eoId;
        }

        public Double getQty() {
            return qty;
        }

        public void setQty(Double qty) {
            this.qty = qty;
        }

        public String getRouterStepId() {
            return routerStepId;
        }

        public void setRouterStepId(String routerStepId) {
            this.routerStepId = routerStepId;
        }

        public String getPreviousStepId() {
            return previousStepId;
        }

        public void setPreviousStepId(String previousStepId) {
            this.previousStepId = previousStepId;
        }

        public String getSourceEoStepActualId() {
            return sourceEoStepActualId;
        }

        public void setSourceEoStepActualId(String sourceEoStepActualId) {
            this.sourceEoStepActualId = sourceEoStepActualId;
        }

    }

}
