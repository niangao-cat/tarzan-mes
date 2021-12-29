package tarzan.actual.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/2/12 10:26 上午
 */
public class MtProcessWorkingDTO2 implements Serializable {
    private static final long serialVersionUID = -5715188275358057379L;
    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("EO编码")
    private String eoNum;

    @ApiModelProperty("工艺路线Id")
    private String routerId;
    @ApiModelProperty("装配清单Id")
    private String bomId;
    @ApiModelProperty("步骤Id")
    private String routerStepId;
    @ApiModelProperty("eo工艺路线实绩Id")
    private String eoRouterActualId;
    @ApiModelProperty("eo步骤实绩Id")
    private String eoStepActualId;
    @ApiModelProperty("排队时间")
    private Date queueDate;
    @ApiModelProperty("完成步骤标识")
    private String doneStepFlag;
    @ApiModelProperty("松散标识")
    private String relaxedFlowFlag;
    @ApiModelProperty("按工序装配标识")
    private String operationAssembleFlag;

    @ApiModelProperty("生产指令ID")
    private String workOrderId;
    @ApiModelProperty("生产指令编码")
    private String workOrderNum;

    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("执行作业数量")
    private Double eoQty;
    @ApiModelProperty("排队数量")
    private Double queueQty;

    @ApiModelProperty("加工数量")
    private Double workingQty;
    @ApiModelProperty("完成暂存数量")
    private Double completePendingQty;
    @ApiModelProperty("完成数量")
    private Double completeQty;
    @ApiModelProperty("报废数量")
    private Double scrappedQty;
    @ApiModelProperty("保留数量")
    private Double holdQty;
    @ApiModelProperty("投料套数")
    private Double kitQty;

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

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public Date getQueueDate() {
        if (queueDate != null) {
            return (Date) queueDate.clone();
        } else {
            return null;
        }
    }

    public void setQueueDate(Date queueDate) {
        if (queueDate == null) {
            this.queueDate = null;
        } else {
            this.queueDate = (Date) queueDate.clone();
        }
    }

    public String getDoneStepFlag() {
        return doneStepFlag;
    }

    public void setDoneStepFlag(String doneStepFlag) {
        this.doneStepFlag = doneStepFlag;
    }

    public String getRelaxedFlowFlag() {
        return relaxedFlowFlag;
    }

    public void setRelaxedFlowFlag(String relaxedFlowFlag) {
        this.relaxedFlowFlag = relaxedFlowFlag;
    }

    public String getOperationAssembleFlag() {
        return operationAssembleFlag;
    }

    public void setOperationAssembleFlag(String operationAssembleFlag) {
        this.operationAssembleFlag = operationAssembleFlag;
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

    public Double getEoQty() {
        return eoQty;
    }

    public void setEoQty(Double eoQty) {
        this.eoQty = eoQty;
    }

    public Double getQueueQty() {
        return queueQty;
    }

    public void setQueueQty(Double queueQty) {
        this.queueQty = queueQty;
    }

    public Double getWorkingQty() {
        return workingQty;
    }

    public void setWorkingQty(Double workingQty) {
        this.workingQty = workingQty;
    }

    public Double getCompletePendingQty() {
        return completePendingQty;
    }

    public void setCompletePendingQty(Double completePendingQty) {
        this.completePendingQty = completePendingQty;
    }

    public Double getCompleteQty() {
        return completeQty;
    }

    public void setCompleteQty(Double completeQty) {
        this.completeQty = completeQty;
    }

    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    public Double getHoldQty() {
        return holdQty;
    }

    public void setHoldQty(Double holdQty) {
        this.holdQty = holdQty;
    }

    public Double getKitQty() {
        return kitQty;
    }

    public void setKitQty(Double kitQty) {
        this.kitQty = kitQty;
    }
}
