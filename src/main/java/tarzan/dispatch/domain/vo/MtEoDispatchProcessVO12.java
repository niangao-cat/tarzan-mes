package tarzan.dispatch.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author peng.yuan
 * @ClassName MtEoDispatchProcessVO12
 * @description
 * @date 2019年10月14日 17:58
 */
public class MtEoDispatchProcessVO12 implements Serializable {
    private static final long serialVersionUID = 2873273680230747296L;

    private String eoId;// 执行作业
    private String routerStepId;// 工艺路线步骤ID
    private Date planStartTime;// 执行作业计划开始时间
    private Date planEndTime;// 执行作业计划结束时间
    private String operationId;// 执行作业步骤工艺
    private String stepName;// 执行作业步骤识别码
    private Double unassignQty;// 未分配数量

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

    public Date getPlanStartTime() {
        if (planStartTime != null) {
            return (Date) planStartTime.clone();
        } else {
            return null;
        }
    }

    public void setPlanStartTime(Date planStartTime) {
        if (planStartTime == null) {
            this.planStartTime = null;
        } else {
            this.planStartTime = (Date) planStartTime.clone();
        }
    }

    public Date getPlanEndTime() {
        if (planEndTime != null) {
            return (Date) planEndTime.clone();
        } else {
            return null;
        }
    }

    public void setPlanEndTime(Date planEndTime) {
        if (planEndTime == null) {
            this.planEndTime = null;
        } else {
            this.planEndTime = (Date) planEndTime.clone();
        }
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public Double getUnassignQty() {
        return unassignQty;
    }

    public void setUnassignQty(Double unassignQty) {
        this.unassignQty = unassignQty;
    }
}
