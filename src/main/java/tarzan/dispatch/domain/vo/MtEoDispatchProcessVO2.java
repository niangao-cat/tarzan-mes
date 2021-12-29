package tarzan.dispatch.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MtEoDispatchProcessVO2 implements Serializable {
    private static final long serialVersionUID = -8732019437149511384L;

    private String eoDispatchProcessId; // 主键
    private String routerStepId;
    private String status; // 状态
    private String workcellId; // 步骤执行工作单元，工作单元主键，标识唯一工作单元
    private Long priority; // 执行顺序
    private Date planStartTime; // 调度计划开始时间
    private Date planEndTime; // 调度计划结束时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate; // 日历日期
    private String shiftCode; // 日历班次
    private Double assignQty; // 本次分配数量（分到不同的WKC）
    private Double trxAssignQty;
    private String eoId;

    public String getEoDispatchProcessId() {
        return eoDispatchProcessId;
    }

    public void setEoDispatchProcessId(String eoDispatchProcessId) {
        this.eoDispatchProcessId = eoDispatchProcessId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
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

    public void setShiftDate(Date shiftDate) {
        if (shiftDate != null) {
            this.shiftDate = (Date) shiftDate.clone();
        } else {
            this.shiftDate = null;
        }
    }

    public Date getShiftDate() {
        if (this.shiftDate == null) {
            return null;
        } else {
            return (Date) this.shiftDate.clone();
        }
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public Double getAssignQty() {
        return assignQty;
    }

    public void setAssignQty(Double assignQty) {
        this.assignQty = assignQty;
    }

    public Double getTrxAssignQty() {
        return trxAssignQty;
    }

    public void setTrxAssignQty(Double trxAssignQty) {
        this.trxAssignQty = trxAssignQty;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

}
