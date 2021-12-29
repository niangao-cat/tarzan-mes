package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/7/1 14:41
 * @Description:
 */
public class MtAssembleConfirmActualVO13 implements Serializable {
    private static final long serialVersionUID = -642417037169397283L;

    @ApiModelProperty("装配过程实绩ID")
    private String assembleProcessActualId;
    @ApiModelProperty("装配确认实绩ID")
    private String assembleConfirmActualId;
    @ApiModelProperty("事件类型ID")
    private String eventTypeId;
    @ApiModelProperty("实际装配物料")
    private String materialId;
    @ApiModelProperty("组件需求工序")
    private String operationId;
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty("工作单元ID")
    private String workcellId;
    @ApiModelProperty("装配数量")
    private Double assembleQty;
    @ApiModelProperty("报废数量")
    private Double scrapQty;
    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("事件ID")
    private String eventId;
    @ApiModelProperty("物料批历史ID")
    private String materialLotHisId;
    

    public String getAssembleProcessActualId() {
        return assembleProcessActualId;
    }

    public void setAssembleProcessActualId(String assembleProcessActualId) {
        this.assembleProcessActualId = assembleProcessActualId;
    }

    public String getAssembleConfirmActualId() {
        return assembleConfirmActualId;
    }

    public void setAssembleConfirmActualId(String assembleConfirmActualId) {
        this.assembleConfirmActualId = assembleConfirmActualId;
    }

    public String getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(String eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public Double getAssembleQty() {
        return assembleQty;
    }

    public void setAssembleQty(Double assembleQty) {
        this.assembleQty = assembleQty;
    }

    public Double getScrapQty() {
        return scrapQty;
    }

    public void setScrapQty(Double scrapQty) {
        this.scrapQty = scrapQty;
    }

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

    public String getMaterialLotHisId() {
        return materialLotHisId;
    }

    public void setMaterialLotHisId(String materialLotHisId) {
        this.materialLotHisId = materialLotHisId;
    }
}
