package tarzan.dispatch.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtEoDispatchProcessVO4 implements Serializable {
    private static final long serialVersionUID = 2952879401493014872L;

    private String eoId; // 执行作业
    private String routerStepId; // 工艺路线步骤ID
    private Date planStartTime; // 执行作业计划开始时间
    private Date planEndTime; // 执行作业计划结束时间
    private String operationId; // 执行作业步骤工艺
    private String stepName; // 执行作业步骤识别码
    private Double unassignQty; // 未分配数量

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eoId == null) ? 0 : eoId.hashCode());
        result = prime * result + ((routerStepId == null) ? 0 : routerStepId.hashCode());
        result = prime * result + ((operationId == null) ? 0 : operationId.hashCode());
        result = prime * result + ((planEndTime == null) ? 0 : planEndTime.hashCode());
        result = prime * result + ((planStartTime == null) ? 0 : planStartTime.hashCode());
        result = prime * result + ((stepName == null) ? 0 : stepName.hashCode());
        result = prime * result + ((unassignQty == null) ? 0 : unassignQty.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        MtEoDispatchProcessVO4 other = (MtEoDispatchProcessVO4) obj;
        if (eoId == null) {
            if (other.eoId != null) {
                return false;
            }
        } else if (!eoId.equals(other.eoId)) {
            return false;
        }
        if (routerStepId == null) {
            if (other.routerStepId != null) {
                return false;
            }
        } else if (!routerStepId.equals(other.routerStepId)) {
            return false;
        }
        if (operationId == null) {
            if (other.operationId != null) {
                return false;
            }
        } else if (!operationId.equals(other.operationId)) {
            return false;
        }
        if (planEndTime == null) {
            if (other.planEndTime != null) {
                return false;
            }
        } else if (planEndTime.compareTo(other.planEndTime) != 0) {
            return false;
        }
        if (planStartTime == null) {
            if (other.planStartTime != null) {
                return false;
            }
        } else if (planStartTime.compareTo(other.planStartTime) != 0) {
            return false;
        }
        if (stepName == null) {
            if (other.stepName != null) {
                return false;
            }
        } else if (!stepName.equals(other.stepName)) {
            return false;
        }
        if (unassignQty == null) {
            if (other.unassignQty != null) {
                return false;
            }
        } else if (unassignQty.compareTo(other.unassignQty) != 0) {
            return false;
        }
        return true;
    }

}
