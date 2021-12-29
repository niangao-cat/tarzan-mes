package tarzan.order.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/12/24 9:50 上午
 */
public class MtEoDTO6 implements Serializable {
    private static final long serialVersionUID = 5653387283900327932L;
    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("执行作业编码")
    private String eoNum;
    @ApiModelProperty("执行作业类型")
    private String eoType;
    @ApiModelProperty("执行作业类型描述")
    private String eoTypeDesc;
    @ApiModelProperty("执行作业状态")
    private String status;
    @ApiModelProperty("执行作业状态描述")
    private String statusDesc;

    //2020/7/27 add by sanfeng.zhang 执行作业管理-基础数据加EO标识
    @ApiModelProperty("EO标识")
    private String eoIdentification;

    @ApiModelProperty("生产指令ID")
    private String workOrderId;
    @ApiModelProperty("生产指令编码")
    private String workOrderNum;
    @ApiModelProperty("生产指令类型")
    private String woType;
    @ApiModelProperty("生产指令类型")
    private String woTypeDesc;
    @ApiModelProperty("生产指令状态")
    private String woStatus;
    @ApiModelProperty("生产指令状态")
    private String woStatusDesc;
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("数量")
    private Double qty;
    @ApiModelProperty("单位")
    private String uomId;
    @ApiModelProperty("单位编码")
    private String uomCode;
    @ApiModelProperty("单位名称")
    private String uomName;

    @ApiModelProperty("客户ID")
    private String customerId;
    @ApiModelProperty("客户编码")
    private String customerCode;
    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("计划开始时间")
    private Date planStartTime;
    @ApiModelProperty("计划结束时间")
    private Date planEndTime;
    @ApiModelProperty("生产线ID")
    private String productionLineId;
    @ApiModelProperty("生产线编码")
    private String productionLineCode;
    @ApiModelProperty("生产线描述")
    private String productionLineName;
    @ApiModelProperty("EO装配清单ID")
    private String eoBomId;
    @ApiModelProperty("EO装配清单编码")
    private String eoBomName;
    @ApiModelProperty("EO工艺路线ID")
    private String eoRouterId;
    @ApiModelProperty("EO工艺路线")
    private String eoRouterName;

    @ApiModelProperty("实绩开始时间")
    private Date actualStartTime;
    @ApiModelProperty("实绩结束时间")
    private Date actualEndTime;
    @ApiModelProperty("累计完成数量")
    private Double completedQty;
    @ApiModelProperty("累计报废数量")
    private Double scrappedQty;

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

    public String getEoType() {
        return eoType;
    }

    public void setEoType(String eoType) {
        this.eoType = eoType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderNum() {
        return workOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
    }

    public String getWoType() {
        return woType;
    }

    public void setWoType(String woType) {
        this.woType = woType;
    }

    public String getWoStatus() {
        return woStatus;
    }

    public void setWoStatus(String woStatus) {
        this.woStatus = woStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getPlanStartTime() {
        if (planStartTime == null) {
            return null;
        } else {
            return (Date) planStartTime.clone();
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
        if (planEndTime == null) {
            return null;
        } else {
            return (Date) planEndTime.clone();
        }
    }

    public void setPlanEndTime(Date planEndTime) {
        if (planEndTime == null) {
            this.planEndTime = null;
        } else {
            this.planEndTime = (Date) planEndTime.clone();
        }
    }

    public String getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }

    public String getProductionLineCode() {
        return productionLineCode;
    }

    public void setProductionLineCode(String productionLineCode) {
        this.productionLineCode = productionLineCode;
    }

    public String getProductionLineName() {
        return productionLineName;
    }

    public void setProductionLineName(String productionLineName) {
        this.productionLineName = productionLineName;
    }

    public String getEoBomId() {
        return eoBomId;
    }

    public void setEoBomId(String eoBomId) {
        this.eoBomId = eoBomId;
    }

    public String getEoBomName() {
        return eoBomName;
    }

    public void setEoBomName(String eoBomName) {
        this.eoBomName = eoBomName;
    }

    public String getEoRouterId() {
        return eoRouterId;
    }

    public void setEoRouterId(String eoRouterId) {
        this.eoRouterId = eoRouterId;
    }

    public String getEoRouterName() {
        return eoRouterName;
    }

    public void setEoRouterName(String eoRouterName) {
        this.eoRouterName = eoRouterName;
    }

    public Date getActualStartTime() {
        if (actualStartTime == null) {
            return null;
        } else {
            return (Date) actualStartTime.clone();
        }
    }

    public void setActualStartTime(Date actualStartTime) {
        if (actualStartTime == null) {
            this.actualStartTime = null;
        } else {
            this.actualStartTime = (Date) actualStartTime.clone();
        }
    }

    public Date getActualEndTime() {
        if (actualEndTime == null) {
            return null;
        } else {
            return (Date) actualEndTime.clone();
        }
    }

    public void setActualEndTime(Date actualEndTime) {
        if (actualEndTime == null) {
            this.actualEndTime = null;
        } else {
            this.actualEndTime = (Date) actualEndTime.clone();
        }
    }

    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }

    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    public String getEoTypeDesc() {
        return eoTypeDesc;
    }

    public void setEoTypeDesc(String eoTypeDesc) {
        this.eoTypeDesc = eoTypeDesc;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getWoTypeDesc() {
        return woTypeDesc;
    }

    public void setWoTypeDesc(String woTypeDesc) {
        this.woTypeDesc = woTypeDesc;
    }

    public String getWoStatusDesc() {
        return woStatusDesc;
    }

    public void setWoStatusDesc(String woStatusDesc) {
        this.woStatusDesc = woStatusDesc;
    }

    public String getEoIdentification() {
        return eoIdentification;
    }

    public void setEoIdentification(String eoIdentification) {
        this.eoIdentification = eoIdentification;
    }
}
