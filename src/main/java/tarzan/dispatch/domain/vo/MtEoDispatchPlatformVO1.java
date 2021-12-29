package tarzan.dispatch.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author : MrZ
 * @date : 2019-12-07 16:22
 **/
public class MtEoDispatchPlatformVO1 implements Serializable {
    private static final long serialVersionUID = -1748305755658627478L;
    @ApiModelProperty("执行作业")
    private String eoId;
    @ApiModelProperty("工艺路线步骤ID")
    private String routerStepId;
    @ApiModelProperty("执行作业计划开始时间")
    private Date planStartTime;
    @ApiModelProperty("执行作业计划结束时间")
    private Date planEndTime;
    @ApiModelProperty("执行作业步骤工艺")
    private String operationId;
    @ApiModelProperty("执行作业步骤识别码")
    private String stepName;

    @ApiModelProperty(value = "EO编号")
    private String eoNum;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "工单，EO对应生产指令")
    private String workOrderId;
    @ApiModelProperty(value = "数量")
    private Double qty;

    @ApiModelProperty("唯一标识")
    private String specialId;

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

    public String getEoNum() {
        return eoNum;
    }

    public void setEoNum(String eoNum) {
        this.eoNum = eoNum;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getSpecialId() {
        return specialId;
    }

    public void setSpecialId(String specialId) {
        this.specialId = specialId;
    }
}
