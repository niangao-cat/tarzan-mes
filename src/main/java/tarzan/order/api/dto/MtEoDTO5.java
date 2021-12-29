package tarzan.order.api.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Leeloing
 * @date 2019/12/23 8:16 下午
 */
public class MtEoDTO5 implements Serializable {
    private static final long serialVersionUID = 7577300607651584572L;
    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("执行作业编码")
    private String eoNum;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("执行作业类型")
    private String eoType;
    @ApiModelProperty("执行作业类型描述")
    private String eoTypeDesc;
    @ApiModelProperty("EO状态")
    private String status;
    @ApiModelProperty("EO状态描述")
    private String statusDesc;
    @ApiModelProperty("生产线ID")
    private String productionLineId;
    @ApiModelProperty("生产线编码")
    private String productionLineCode;
    @ApiModelProperty("生产线描述")
    private String productionLineName;
    @ApiModelProperty("计划开始时间")
    private Date planStartTime;
    @ApiModelProperty("计划结束时间")
    private Date planEndTime;
    @ApiModelProperty("EO数量")
    private Double qty;
    @ApiModelProperty("单位")
    private String uomId;
    @ApiModelProperty("单位名称")
    private String uomName;
    @ApiModelProperty("生产版本")
    private String productionVersion;

    @ApiModelProperty("EO装配清单ID")
    private String eoBomId;
    @ApiModelProperty("EO装配清单版本")
    private String eoBomNameRevision;

    @ApiModelProperty("EO工艺路线ID")
    private String eoRouterId;
    @ApiModelProperty("EO工艺路线版本")
    private String eoRouterNameRevision;

    @ApiModelProperty("生产指令ID")
    private String workOrderId;
    @ApiModelProperty("生产指令编码")
    private String workOrderNum;

    @ApiModelProperty("WO装配清单ID")
    private String woBomId;
    @ApiModelProperty("WO装配清单版本")
    private String woBomNameRevision;

    @ApiModelProperty("WO工艺路线ID")
    private String woRouterId;
    @ApiModelProperty("WO工艺路线版本")
    private String woRouterNameRevision;

    @ApiModelProperty("完成数量")
    private Double completedQty;
    @ApiModelProperty("报废数量")
    private Double scrappedQty;

    @ApiModelProperty("客户ID")
    private String customerId;
    @ApiModelProperty("客户编码")
    private String customerCode;
    @ApiModelProperty("客户名称")
    private String customerName;

    //2020-07-09 add by sanfeng.zhang
    //执行作业管理的清单 加字段 EO标识、当前EO所处工序
    @ApiModelProperty(value = "EO标识")
    private String eoIdentification;

    @ApiModelProperty(value = "当前工序")
    private String eoWorkcellIdDesc;

    //2020-12-07 add by sanfeng.zhang
    //执行作业管理的清单 加字段 EO标识、当前EO所处工序
    @ApiModelProperty(value = "销售订单")
    private String soNum;

    @ApiModelProperty(value = "返修SN")
    private String repairSn;

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

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public String getEoBomId() {
        return eoBomId;
    }

    public void setEoBomId(String eoBomId) {
        this.eoBomId = eoBomId;
    }

    public String getEoBomNameRevision() {
        return eoBomNameRevision;
    }

    public void setEoBomNameRevision(String eoBomNameRevision) {
        this.eoBomNameRevision = eoBomNameRevision;
    }

    public String getEoRouterId() {
        return eoRouterId;
    }

    public void setEoRouterId(String eoRouterId) {
        this.eoRouterId = eoRouterId;
    }

    public String getEoRouterNameRevision() {
        return eoRouterNameRevision;
    }

    public void setEoRouterNameRevision(String eoRouterNameRevision) {
        this.eoRouterNameRevision = eoRouterNameRevision;
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

    public String getWoBomId() {
        return woBomId;
    }

    public void setWoBomId(String woBomId) {
        this.woBomId = woBomId;
    }

    public String getWoBomNameRevision() {
        return woBomNameRevision;
    }

    public void setWoBomNameRevision(String woBomNameRevision) {
        this.woBomNameRevision = woBomNameRevision;
    }

    public String getWoRouterId() {
        return woRouterId;
    }

    public void setWoRouterId(String woRouterId) {
        this.woRouterId = woRouterId;
    }

    public String getWoRouterNameRevision() {
        return woRouterNameRevision;
    }

    public void setWoRouterNameRevision(String woRouterNameRevision) {
        this.woRouterNameRevision = woRouterNameRevision;
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

    public String getEoIdentification() {
        return eoIdentification;
    }

    public void setEoIdentification(String eoIdentification) {
        this.eoIdentification = eoIdentification;
    }

    public String getEoWorkcellIdDesc() {
        return eoWorkcellIdDesc;
    }

    public void setEoWorkcellIdDesc(String eoWorkcellIdDesc) {
        this.eoWorkcellIdDesc = eoWorkcellIdDesc;
    }

    public String getProductionVersion() {
        return productionVersion;
    }

    public void setProductionVersion(String productionVersion) {
        this.productionVersion = productionVersion;
    }

    public String getSoNum() {
        return soNum;
    }

    public void setSoNum(String soNum) {
        this.soNum = soNum;
    }

    public String getRepairSn() {
        return repairSn;
    }

    public void setRepairSn(String repairSn) {
        this.repairSn = repairSn;
    }
}
