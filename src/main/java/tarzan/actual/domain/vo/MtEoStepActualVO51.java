package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/10/26 19:09
 */
public class MtEoStepActualVO51 implements Serializable {

    private static final long serialVersionUID = 1501038332504459031L;
    @ApiModelProperty("步骤ID")
    private String routerStepId;
    @ApiModelProperty("工作单元")
    private String workcellId;
    @ApiModelProperty("返工标识")
    private String reworkStepFlag;
    @ApiModelProperty("原地重工标识")
    private String localReworkFlag;
    @ApiModelProperty("入口步骤标识")
    private String entryStepFlag;
    @ApiModelProperty("事件请求")
    private String eventRequestId;

    @ApiModelProperty("事件ID")
    private String eventId;

    @ApiModelProperty("排队列表")
    private List<QueueInfo> eoQueueMessageList;

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getReworkStepFlag() {
        return reworkStepFlag;
    }

    public void setReworkStepFlag(String reworkStepFlag) {
        this.reworkStepFlag = reworkStepFlag;
    }

    public String getLocalReworkFlag() {
        return localReworkFlag;
    }

    public String getEntryStepFlag() {
        return entryStepFlag;
    }

    public void setEntryStepFlag(String entryStepFlag) {
        this.entryStepFlag = entryStepFlag;
    }

    public void setLocalReworkFlag(String localReworkFlag) {
        this.localReworkFlag = localReworkFlag;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public List<QueueInfo> getEoQueueMessageList() {
        return eoQueueMessageList;
    }

    public void setEoQueueMessageList(List<QueueInfo> eoQueueMessageList) {
        this.eoQueueMessageList = eoQueueMessageList;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public static class QueueInfo implements Serializable {

        private static final long serialVersionUID = 1080798945534848054L;
        @ApiModelProperty("工艺路线实绩")
        private String eoRouterActualId;
        @ApiModelProperty("排队更新数量")
        private Double queueQty;
        @ApiModelProperty("前道步骤")
        private String previousStepId;
        @ApiModelProperty("步骤ID")
        private String routerStepId;
        @ApiModelProperty("入口步骤标识")
        private String entryStepFlag;

        public String getEoRouterActualId() {
            return eoRouterActualId;
        }

        public void setEoRouterActualId(String eoRouterActualId) {
            this.eoRouterActualId = eoRouterActualId;
        }

        public Double getQueueQty() {
            return queueQty;
        }

        public void setQueueQty(Double queueQty) {
            this.queueQty = queueQty;
        }

        public String getPreviousStepId() {
            return previousStepId;
        }

        public void setPreviousStepId(String previousStepId) {
            this.previousStepId = previousStepId;
        }

        public String getRouterStepId() {
            return routerStepId;
        }

        public void setRouterStepId(String routerStepId) {
            this.routerStepId = routerStepId;
        }

        public String getEntryStepFlag() {
            return entryStepFlag;
        }

        public void setEntryStepFlag(String entryStepFlag) {
            this.entryStepFlag = entryStepFlag;
        }
    }
}
