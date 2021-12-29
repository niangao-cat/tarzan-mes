package tarzan.dispatch.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/10 15:28
 * @Author: ${yiyang.xie}
 */
public class MtEoDispatchActionVO18 implements Serializable {
    private static final long serialVersionUID = -1162062786508624889L;
    @ApiModelProperty("主键ID")
    private String eoDispatchActionId;
    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("工艺路线步骤ID")
    private String routerStepId;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("优先级")
    private Long priority;
    @ApiModelProperty("计划开始时间从")
    private Date planStartTimeFrom;
    @ApiModelProperty("计划开始时间至")
    private Date planStartTimeTo;
    @ApiModelProperty("计划结束时间从")
    private Date planEndTimeFrom;
    @ApiModelProperty("计划结束时间至")
    private Date planEndTimeTo;
    @ApiModelProperty("班次日期从")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDateFrom;
    @ApiModelProperty("班次日期至")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDateTo;
    @ApiModelProperty("班次编码")
    private String shiftCode;
    @ApiModelProperty("工作单元ID")
    private String workcellId;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("版本")
    private Long revision;

    public String getEoDispatchActionId() {
        return eoDispatchActionId;
    }

    public void setEoDispatchActionId(String eoDispatchActionId) {
        this.eoDispatchActionId = eoDispatchActionId;
    }

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

    public Date getPlanStartTimeFrom() {
        if (planStartTimeFrom != null) {
            return (Date) planStartTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setPlanStartTimeFrom(Date planStartTimeFrom) {
        if (planStartTimeFrom == null) {
            this.planStartTimeFrom = null;
        } else {
            this.planStartTimeFrom = (Date) planStartTimeFrom.clone();
        }
    }

    public Date getPlanStartTimeTo() {
        if (planStartTimeTo != null) {
            return (Date) planStartTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setPlanStartTimeTo(Date planStartTimeTo) {
        if (planStartTimeTo == null) {
            this.planStartTimeTo = null;
        } else {
            this.planStartTimeTo = (Date) planStartTimeTo.clone();
        }
    }

    public Date getPlanEndTimeFrom() {
        if (planEndTimeFrom != null) {
            return (Date) planEndTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setPlanEndTimeFrom(Date planEndTimeFrom) {
        if (planEndTimeFrom == null) {
            this.planEndTimeFrom = null;
        } else {
            this.planEndTimeFrom = (Date) planEndTimeFrom.clone();
        }
    }

    public Date getPlanEndTimeTo() {
        if (planEndTimeTo != null) {
            return (Date) planEndTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setPlanEndTimeTo(Date planEndTimeTo) {
        if (planEndTimeTo == null) {
            this.planEndTimeTo = null;
        } else {
            this.planEndTimeTo = (Date) planEndTimeTo.clone();
        }
    }

    public Date getShiftDateFrom() {
        if (shiftDateFrom != null) {
            return (Date) shiftDateFrom.clone();
        } else {
            return null;
        }
    }

    public void setShiftDateFrom(Date shiftDateFrom) {
        if (shiftDateFrom == null) {
            this.shiftDateFrom = null;
        } else {
            this.shiftDateFrom = (Date) shiftDateFrom.clone();
        }
    }

    public Date getShiftDateTo() {
        if (shiftDateTo != null) {
            return (Date) shiftDateTo.clone();
        } else {
            return null;
        }
    }

    public void setShiftDateTo(Date shiftDateTo) {
        if (shiftDateTo == null) {
            this.shiftDateTo = null;
        } else {
            this.shiftDateTo = (Date) shiftDateTo.clone();
        }
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }
}
