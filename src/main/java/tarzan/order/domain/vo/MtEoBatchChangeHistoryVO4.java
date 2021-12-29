package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/12/2 11:43
 * @Description:
 */
public class MtEoBatchChangeHistoryVO4 implements Serializable {

    private static final long serialVersionUID = 392389148535695821L;
    @ApiModelProperty("主键ID，标识唯一一条记录")
    private String eoBatchChangeHistoryId;
    @ApiModelProperty("变更后EO,EO主键，标识唯一EO")
    private String eoId;
    @ApiModelProperty("变更来源EO，EO主键，标识唯一EO")
    private String sourceEoId;
    @ApiModelProperty("变更原因")
    private String reason;
    @ApiModelProperty("顺序")
    private Long sequence;
    @ApiModelProperty("事件ID，用于表示一次变更操作")
    private String eventId;
    @ApiModelProperty("来源变更数量")
    private Double sourceTrxQty;
    @ApiModelProperty("变更数量")
    private Double trxQty;
    @ApiModelProperty("步骤实绩ID")
    private String sourceEoStepActualId;

    public String getEoBatchChangeHistoryId() {
        return eoBatchChangeHistoryId;
    }

    public void setEoBatchChangeHistoryId(String eoBatchChangeHistoryId) {
        this.eoBatchChangeHistoryId = eoBatchChangeHistoryId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getSourceEoId() {
        return sourceEoId;
    }

    public void setSourceEoId(String sourceEoId) {
        this.sourceEoId = sourceEoId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Double getSourceTrxQty() {
        return sourceTrxQty;
    }

    public void setSourceTrxQty(Double sourceTrxQty) {
        this.sourceTrxQty = sourceTrxQty;
    }

    public Double getTrxQty() {
        return trxQty;
    }

    public void setTrxQty(Double trxQty) {
        this.trxQty = trxQty;
    }

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
    }
    
}

