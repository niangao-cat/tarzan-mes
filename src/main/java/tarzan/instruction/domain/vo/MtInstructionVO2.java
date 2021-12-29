package tarzan.instruction.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;


public class MtInstructionVO2 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -9024207760343922575L;
    /**
     * 指令ID
     */
    private String instructionId;
    /**
     * 指令编号
     */
    private String instructionNum;

    /**
     * 来源指令ID
     */
    private String sourceInstructionId;

    /**
     * 指令移动类型
     */
    private String instructionType;

    /**
     * 指令状态
     */
    private String instructionStatus;

    /**
     * 物料ID
     */
    private String materialId;

    /**
     * 单位
     */
    private String uomId;

    /**
     * 在制品id
     */
    private String eoId;

    /**
     * 配送路线
     */
    private String disRouterId;

    /**
     * 订单类型（EO, NOTIFICATION,STOCKTAKE)
     */
    private String orderType;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * ERP订单类型（PO，SO）
     */
    private String sourceOrderType;

    /**
     * ERP订单ID
     */
    private String sourceOrderId;

    /**
     * 订单行id
     */
    private String sourceOrderLineId;

    /**
     * 订单分配行id（oracle的分配行id）
     */
    private String sourceOrderLineLocationId;

    /**
     * 订单分配行id
     */
    private String sourceOrderLineDistId;


    private String sourceOutsideCompLineId;

    /**
     * 来源站点id
     */
    private String fromSiteId;
    /**
     * 目标站点id
     */
    private String toSiteId;


    private String fromParentLocatorId;

    /**
     * 来源库位id
     */
    private String fromLocatorId;

    private String toParentLocatorId;

    /**
     * 目标库位id
     */
    private String toLocatorId;

    /**
     * 成本中心
     */
    private String costCenterId;
    /**
     * 指令数量
     */
    private Double quantity;

    /**
     * 需求日期
     */
    private Date demandTime;

    /**
     * 波次
     */
    private String waveSequence;

    /**
     * 班次日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;

    /**
     * 班次代码
     */
    private String shiftCode;

    /**
     * 覆盖数量
     */
    private Double coverQty;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 备注
     */
    private String remark;
    /**
     * 实际业务需要的单据编号
     */
    private String identification;
    /**
     * 库存形态
     */
    private String fromOwnerType;
    /**
     * 目标库存形
     */
    private String toOwnerType;


    private Double executableQty;


    private List<InstructionActualMessage> instructionActualMessageList;

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public String getInstructionNum() {
        return instructionNum;
    }

    public void setInstructionNum(String instructionNum) {
        this.instructionNum = instructionNum;
    }

    public String getSourceInstructionId() {
        return sourceInstructionId;
    }

    public void setSourceInstructionId(String sourceInstructionId) {
        this.sourceInstructionId = sourceInstructionId;
    }

    public String getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(String instructionType) {
        this.instructionType = instructionType;
    }

    public String getInstructionStatus() {
        return instructionStatus;
    }

    public void setInstructionStatus(String instructionStatus) {
        this.instructionStatus = instructionStatus;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getDisRouterId() {
        return disRouterId;
    }

    public void setDisRouterId(String disRouterId) {
        this.disRouterId = disRouterId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSourceOrderType() {
        return sourceOrderType;
    }

    public void setSourceOrderType(String sourceOrderType) {
        this.sourceOrderType = sourceOrderType;
    }

    public String getSourceOrderId() {
        return sourceOrderId;
    }

    public void setSourceOrderId(String sourceOrderId) {
        this.sourceOrderId = sourceOrderId;
    }

    public String getSourceOrderLineId() {
        return sourceOrderLineId;
    }

    public void setSourceOrderLineId(String sourceOrderLineId) {
        this.sourceOrderLineId = sourceOrderLineId;
    }

    public String getSourceOrderLineLocationId() {
        return sourceOrderLineLocationId;
    }

    public void setSourceOrderLineLocationId(String sourceOrderLineLocationId) {
        this.sourceOrderLineLocationId = sourceOrderLineLocationId;
    }

    public String getSourceOrderLineDistId() {
        return sourceOrderLineDistId;
    }

    public void setSourceOrderLineDistId(String sourceOrderLineDistId) {
        this.sourceOrderLineDistId = sourceOrderLineDistId;
    }

    public String getSourceOutsideCompLineId() {
        return sourceOutsideCompLineId;
    }

    public void setSourceOutsideCompLineId(String sourceOutsideCompLineId) {
        this.sourceOutsideCompLineId = sourceOutsideCompLineId;
    }

    public String getFromSiteId() {
        return fromSiteId;
    }

    public void setFromSiteId(String fromSiteId) {
        this.fromSiteId = fromSiteId;
    }

    public String getToSiteId() {
        return toSiteId;
    }

    public void setToSiteId(String toSiteId) {
        this.toSiteId = toSiteId;
    }

    public String getFromParentLocatorId() {
        return fromParentLocatorId;
    }

    public void setFromParentLocatorId(String fromParentLocatorId) {
        this.fromParentLocatorId = fromParentLocatorId;
    }

    public String getFromLocatorId() {
        return fromLocatorId;
    }

    public void setFromLocatorId(String fromLocatorId) {
        this.fromLocatorId = fromLocatorId;
    }

    public String getToParentLocatorId() {
        return toParentLocatorId;
    }

    public void setToParentLocatorId(String toParentLocatorId) {
        this.toParentLocatorId = toParentLocatorId;
    }

    public String getToLocatorId() {
        return toLocatorId;
    }

    public void setToLocatorId(String toLocatorId) {
        this.toLocatorId = toLocatorId;
    }

    public String getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(String costCenterId) {
        this.costCenterId = costCenterId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Date getDemandTime() {
        if (demandTime != null) {
            return (Date) demandTime.clone();
        } else {
            return null;
        }
    }

    public void setDemandTime(Date demandTime) {
        if (demandTime == null) {
            this.demandTime = null;
        } else {
            this.demandTime = (Date) demandTime.clone();
        }
    }

    public String getWaveSequence() {
        return waveSequence;
    }

    public void setWaveSequence(String waveSequence) {
        this.waveSequence = waveSequence;
    }


    public Date getShiftDate() {

        if (shiftDate == null) {
            return null;
        }
        return (Date) shiftDate.clone();
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

    public Double getCoverQty() {
        return coverQty;
    }

    public void setCoverQty(Double coverQty) {
        this.coverQty = coverQty;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getFromOwnerType() {
        return fromOwnerType;
    }

    public void setFromOwnerType(String fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public String getToOwnerType() {
        return toOwnerType;
    }

    public void setToOwnerType(String toOwnerType) {
        this.toOwnerType = toOwnerType;
    }

    public Double getExecutableQty() {
        return executableQty;
    }

    public void setExecutableQty(Double executableQty) {
        this.executableQty = executableQty;
    }

    public List<InstructionActualMessage> getInstructionActualMessageList() {
        return instructionActualMessageList;
    }

    public void setInstructionActualMessageList(List<InstructionActualMessage> instructionActualMessageList) {
        this.instructionActualMessageList = instructionActualMessageList;
    }



    public static class InstructionActualMessage implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -3299834933565598976L;
        private String actualId;
        private String sourceOrderType;
        private String sourceOrderId;
        private Double actualQty;
        private String fromLocatorId;
        private String toLocatorId;

        public String getActualId() {
            return actualId;
        }

        public void setActualId(String actualId) {
            this.actualId = actualId;
        }

        public String getSourceOrderType() {
            return sourceOrderType;
        }

        public void setSourceOrderType(String sourceOrderType) {
            this.sourceOrderType = sourceOrderType;
        }

        public String getSourceOrderId() {
            return sourceOrderId;
        }

        public void setSourceOrderId(String sourceOrderId) {
            this.sourceOrderId = sourceOrderId;
        }

        public Double getActualQty() {
            return actualQty;
        }

        public void setActualQty(Double actualQty) {
            this.actualQty = actualQty;
        }

        public String getFromLocatorId() {
            return fromLocatorId;
        }

        public void setFromLocatorId(String fromLocatorId) {
            this.fromLocatorId = fromLocatorId;
        }

        public String getToLocatorId() {
            return toLocatorId;
        }

        public void setToLocatorId(String toLocatorId) {
            this.toLocatorId = toLocatorId;
        }

    }
}
