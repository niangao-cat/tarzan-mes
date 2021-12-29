package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepActualVO21 implements Serializable {
    private static final long serialVersionUID = 8909791995028733174L;

    private String routerStepId; // 步骤唯一标识
    private String groupRouterStepId; // 组步骤
    private String eoRouterActualId; // 工艺路线实绩唯一标识
    private String workcellId; // 工作单元唯一标识
    private Double queueQty; // 排队更新数量
    private String reworkStepFlag; // 返工标识
    private String localReworkFlag; // 原地重工标识
    private String previousStepId; // 前道步骤唯一标识
    private String eventRequestId; // 事件类型

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getGroupRouterStepId() {
        return groupRouterStepId;
    }

    public void setGroupRouterStepId(String groupRouterStepId) {
        this.groupRouterStepId = groupRouterStepId;
    }

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public Double getQueueQty() {
        return queueQty;
    }

    public void setQueueQty(Double queueQty) {
        this.queueQty = queueQty;
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

    public void setLocalReworkFlag(String localReworkFlag) {
        this.localReworkFlag = localReworkFlag;
    }

    public String getPreviousStepId() {
        return previousStepId;
    }

    public void setPreviousStepId(String previousStepId) {
        this.previousStepId = previousStepId;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }
}
