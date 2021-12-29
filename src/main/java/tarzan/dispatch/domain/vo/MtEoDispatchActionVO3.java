package tarzan.dispatch.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtEoDispatchActionVO3 implements Serializable {
    private static final long serialVersionUID = -1292409165285800310L;

    private String eoId; // 执行作业主键，标识该步骤分配对应的执行作业结构
    private String routerStepId; // EO工艺路线步骤主键，用于标识唯一EO工艺路线步骤
    private String operationId; // 工艺ID
    private Long priority; // 优先级
    private Date planStartTime; // 调度计划开始时间
    private Date planEndTime; // 调度计划结束时间
    private Double assignQty; // 已分配数量
    private Double revocableQty; // 可修改数量
    private Long revision; // 版本
    private String workcellId; // 工作单元

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public void setPlanStartTime(Date planStartTime) {
        if (planStartTime != null) {
            this.planStartTime = (Date) planStartTime.clone();
        } else {
            this.planStartTime = null;
        }
    }

    public Date getPlanStartTime() {
        if (this.planStartTime != null) {
            return (Date) planStartTime.clone();
        } else {
            return null;
        }
    }

    public void setPlanEndTime(Date planEndTime) {
        if (planEndTime != null) {
            this.planEndTime = (Date) planEndTime.clone();
        } else {
            this.planEndTime = null;
        }
    }

    public Date getPlanEndTime() {
        if (planEndTime != null) {
            return (Date) planEndTime.clone();
        } else {
            return null;
        }
    }

    public Double getAssignQty() {
        return assignQty;
    }

    public void setAssignQty(Double assignQty) {
        this.assignQty = assignQty;
    }

    public Double getRevocableQty() {
        return revocableQty;
    }

    public void setRevocableQty(Double revocableQty) {
        this.revocableQty = revocableQty;
    }

    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }
}
