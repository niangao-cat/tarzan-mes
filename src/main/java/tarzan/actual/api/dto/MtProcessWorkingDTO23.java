package tarzan.actual.api.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao tang
 * @date 2020-02-18 15:50:00
 */
public class MtProcessWorkingDTO23 implements Serializable {
    
    private static final long serialVersionUID = -5934435993956826546L;
    
    @ApiModelProperty(value = "工作单元Id", required = true)
    private String workcellId;
    @ApiModelProperty(value = "执行作业Id", required = true)
    private String eoId;
    @ApiModelProperty("步骤Id")
    private String routerStepId;
    @ApiModelProperty("工艺Id")
    private String operationId;
    @ApiModelProperty("eo步骤实绩Id")
    private String eoStepActualId;
    @ApiModelProperty("班次编码")
    private String shiftCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("日历日期")
    private Date shiftDate;
    @ApiModelProperty(value = "排队数量", required = true)
    private Double queueQty;
    @ApiModelProperty(value = "调度数量", required = true)
    private Double assignQty;
    @ApiModelProperty(value = "配置项（调度移动规则）", required = true)
    private String dispatchMoveRules;
    
    public String getWorkcellId() {
        return workcellId;
    }
    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
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
    public String getShiftCode() {
        return shiftCode;
    }
    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }
    public Date getShiftDate() {
        return shiftDate;
    }
    public void setShiftDate(Date shiftDate) {
        this.shiftDate = shiftDate;
    }
    public Double getQueueQty() {
        return queueQty;
    }
    public void setQueueQty(Double queueQty) {
        this.queueQty = queueQty;
    }
    public String getDispatchMoveRules() {
        return dispatchMoveRules;
    }
    public void setDispatchMoveRules(String dispatchMoveRules) {
        this.dispatchMoveRules = dispatchMoveRules;
    }
    public String getEoStepActualId() {
        return eoStepActualId;
    }
    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }
    public Double getAssignQty() {
        return assignQty;
    }
    public void setAssignQty(Double assignQty) {
        this.assignQty = assignQty;
    }
    public String getOperationId() {
        return operationId;
    }
    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
    
}