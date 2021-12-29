package tarzan.order.api.dto;


import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


public class MtEoDTO implements Serializable {

    private static final long serialVersionUID = -1128301526084982196L;

    private String eoId;    //执行作业ID
    private String eventId; //事件ID
    private String eoNum;   //执行作业编码
    private String siteId;   //站点ID
    private String workOrderId;   //生产指令ID
    private String status;   //状态
    private String lastEoStatus;   //前次更新状态
    private String productionLineId;   //生产线ID
    private String workcellId;   //工作单元ID
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planStartTime;   //计划开始时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planEndTime;   //计划结束时间
    private Double qty;   //数量
    private String uomId;   //单位
    private String eoType;   //执行作业类型
    private String validateFlag;   //验证标识
    private String identification;   //标识说明
    private String materialId;   //物料ID
    
    public String getEoId() {
        return eoId;
    }
    public void setEoId(String eoId) {
        this.eoId = eoId;
    }
    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public String getEoNum() {
        return eoNum;
    }
    public void setEoNum(String eoNum) {
        this.eoNum = eoNum;
    }
    public String getSiteId() {
        return siteId;
    }
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
    public String getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(String workOrderId) {
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
    public String getProductionLineId() {
        return productionLineId;
    }
    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }
    public String getWorkcellId() {
        return workcellId;
    }
    public void setWorkcellId(String workcellId) {
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
    public String getUomId() {
        return uomId;
    }
    public void setUomId(String uomId) {
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
    public String getMaterialId() {
        return materialId;
    }
    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtEoDTO [eoId=");
        builder.append(eoId);
        builder.append(", eventId=");
        builder.append(eventId);
        builder.append(", eoNum=");
        builder.append(eoNum);
        builder.append(", siteId=");
        builder.append(siteId);
        builder.append(", workOrderId=");
        builder.append(workOrderId);
        builder.append(", status=");
        builder.append(status);
        builder.append(", lastEoStatus=");
        builder.append(lastEoStatus);
        builder.append(", productionLineId=");
        builder.append(productionLineId);
        builder.append(", workcellId=");
        builder.append(workcellId);
        builder.append(", planStartTime=");
        builder.append(planStartTime);
        builder.append(", planEndTime=");
        builder.append(planEndTime);
        builder.append(", qty=");
        builder.append(qty);
        builder.append(", uomId=");
        builder.append(uomId);
        builder.append(", eoType=");
        builder.append(eoType);
        builder.append(", validateFlag=");
        builder.append(validateFlag);
        builder.append(", identification=");
        builder.append(identification);
        builder.append(", materialId=");
        builder.append(materialId);
        builder.append("]");
        return builder.toString();
    }

    

}
