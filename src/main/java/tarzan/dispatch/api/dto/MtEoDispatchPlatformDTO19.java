package tarzan.dispatch.api.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author : MrZ
 * @date : 2019-12-12 10:18
 **/
public class MtEoDispatchPlatformDTO19 implements Serializable {
    private static final long serialVersionUID = -9079781022926826324L;
    @ApiModelProperty("主键ID,eoDispatchActionId或者eoDispatchProcessId")
    private String eoDispatchId;
    @ApiModelProperty("调度状态(DISPATCH_STATUS),PUBLISH或者UNPUBLISH")
    private String eoDispatchStatus;
    @ApiModelProperty("调度状态描述")
    private String eoDispatchStatusDesc;

    @ApiModelProperty("执行作业")
    private String eoId;
    @ApiModelProperty(value = "EO编号")
    private String eoNum;
    @ApiModelProperty("工艺路线步骤ID")
    private String routerStepId;
    @ApiModelProperty("执行作业步骤识别码")
    private String stepName;

    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编号")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "调度数")
    private Double dispatchQty;
    @ApiModelProperty("WKC ID")
    private String workcellId;
    @ApiModelProperty(value = "工作单元编号")
    private String workcellCode;
    @ApiModelProperty(value = "日历日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    @ApiModelProperty(value = "班次")
    private String shiftCode;
    @ApiModelProperty(value = "优先级", hidden = true)
    private Long priority;

    public String getEoDispatchId() {
        return eoDispatchId;
    }

    public void setEoDispatchId(String eoDispatchId) {
        this.eoDispatchId = eoDispatchId;
    }

    public String getEoDispatchStatus() {
        return eoDispatchStatus;
    }

    public void setEoDispatchStatus(String eoDispatchStatus) {
        this.eoDispatchStatus = eoDispatchStatus;
    }

    public String getEoDispatchStatusDesc() {
        return eoDispatchStatusDesc;
    }

    public void setEoDispatchStatusDesc(String eoDispatchStatusDesc) {
        this.eoDispatchStatusDesc = eoDispatchStatusDesc;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Date getShiftDate() {
        if (shiftDate != null) {
            return (Date) shiftDate.clone();
        } else {
            return null;
        }
    }

    public void setShiftDate(Date shiftDate) {
        if (shiftDate == null) {
            this.shiftDate = null;
        } else {
            this.shiftDate = (Date) shiftDate.clone();
        }
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public Double getDispatchQty() {
        return dispatchQty;
    }

    public void setDispatchQty(Double dispatchQty) {
        this.dispatchQty = dispatchQty;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getEoNum() {
        return eoNum;
    }

    public void setEoNum(String eoNum) {
        this.eoNum = eoNum;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }
}
