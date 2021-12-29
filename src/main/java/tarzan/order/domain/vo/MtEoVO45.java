package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/10/29 19:02
 */
public class MtEoVO45 implements Serializable {
    private static final long serialVersionUID = -4876738451547538342L;
    @ApiModelProperty("执行作业ID")
    private  String eoId;
    @ApiModelProperty("执行作业编码")
    private String eoNum;
    @ApiModelProperty("站点ID")
    private  String siteId;
    @ApiModelProperty("生产指令ID")
    private  String workOrderId;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("前次更新状态")
    private String lastEoStatus;
    @ApiModelProperty("生产线ID")
    private  String productionLineId;
    @ApiModelProperty("工作单元ID")
    private  String workcellId;
    @ApiModelProperty("计划开始时间")
    private Date planStartTime;
    @ApiModelProperty("计划结束时间")
    private Date planEndTime;
    @ApiModelProperty("数量")
    private Double qty;
    @ApiModelProperty("单位")
    private  String uomId;
    @ApiModelProperty("执行作业类型")
    private String eoType;
    @ApiModelProperty("验证标识")
    private String validateFlag;
    @ApiModelProperty("标识说明")
    private String identification;
    @ApiModelProperty("物料ID")
    private  String materialId;

    @ApiModelProperty("事务数量")
    private Double trxQty;

    public  String getEoId() {
        return eoId;
    }

    public void setEoId( String eoId) {
        this.eoId = eoId;
    }

    public String getEoNum() {
        return eoNum;
    }

    public void setEoNum(String eoNum) {
        this.eoNum = eoNum;
    }

    public  String getSiteId() {
        return siteId;
    }

    public void setSiteId( String siteId) {
        this.siteId = siteId;
    }

    public  String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId( String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastEoStatus() {
        return lastEoStatus;
    }

    public void setLastEoStatus(String lastEoStatus) {
        this.lastEoStatus = lastEoStatus;
    }

    public  String getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId( String productionLineId) {
        this.productionLineId = productionLineId;
    }

    public  String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId( String workcellId) {
        this.workcellId = workcellId;
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

    public  String getUomId() {
        return uomId;
    }

    public void setUomId( String uomId) {
        this.uomId = uomId;
    }

    public String getEoType() {
        return eoType;
    }

    public void setEoType(String eoType) {
        this.eoType = eoType;
    }

    public String getValidateFlag() {
        return validateFlag;
    }

    public void setValidateFlag(String validateFlag) {
        this.validateFlag = validateFlag;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public  String getMaterialId() {
        return materialId;
    }

    public void setMaterialId( String materialId) {
        this.materialId = materialId;
    }

    public Double getTrxQty() {
        return trxQty;
    }

    public void setTrxQty(Double trxQty) {
        this.trxQty = trxQty;
    }
}
