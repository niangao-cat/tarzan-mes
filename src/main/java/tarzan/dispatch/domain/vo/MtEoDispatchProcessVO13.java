package tarzan.dispatch.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author : MrZ
 * @date : 2019-12-13 10:29
 **/
public class MtEoDispatchProcessVO13 implements Serializable {
    private static final long serialVersionUID = 5595472135637447896L;
    @ApiModelProperty("主键")
    private List<String> eoDispatchProcessIdList;
    @ApiModelProperty("执行作业ID")
    private List<String> eoIdList;
    @ApiModelProperty("工艺路线步骤ID")
    private List<String> routerStepIdList;
    @ApiModelProperty("工艺ID")
    private List<String> operationIdList;
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
    private List<String> shiftCodeList;
    @ApiModelProperty("工作单元ID")
    private List<String> workcellIdList;

    public List<String> getEoDispatchProcessIdList() {
        return eoDispatchProcessIdList;
    }

    public void setEoDispatchProcessIdList(List<String> eoDispatchProcessIdList) {
        this.eoDispatchProcessIdList = eoDispatchProcessIdList;
    }

    public List<String> getEoIdList() {
        return eoIdList;
    }

    public void setEoIdList(List<String> eoIdList) {
        this.eoIdList = eoIdList;
    }

    public List<String> getRouterStepIdList() {
        return routerStepIdList;
    }

    public void setRouterStepIdList(List<String> routerStepIdList) {
        this.routerStepIdList = routerStepIdList;
    }

    public List<String> getOperationIdList() {
        return operationIdList;
    }

    public void setOperationIdList(List<String> operationIdList) {
        this.operationIdList = operationIdList;
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

    public List<String> getShiftCodeList() {
        return shiftCodeList;
    }

    public void setShiftCodeList(List<String> shiftCodeList) {
        this.shiftCodeList = shiftCodeList;
    }

    public List<String> getWorkcellIdList() {
        return workcellIdList;
    }

    public void setWorkcellIdList(List<String> workcellIdList) {
        this.workcellIdList = workcellIdList;
    }
}
