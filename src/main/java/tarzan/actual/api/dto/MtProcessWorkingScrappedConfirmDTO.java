package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 2020-02-12 19:08
 */
public class MtProcessWorkingScrappedConfirmDTO implements Serializable {

    private static final long serialVersionUID = 2639055485312762099L;
    @ApiModelProperty("EO步骤实绩ID")
    private String eoStepActualId;

    @ApiModelProperty("工作单元ID")
    private String workcellId;

    @ApiModelProperty("数量")
    private Double qty;

    @ApiModelProperty("松散标识")
    private String relaxedFlowFlag;

    @ApiModelProperty("EO报废确认时组件报废")
    private String eoAbandonAndComponentAbandon;

    @ApiModelProperty("是否按照物料批退回或报废")
    private String  assembleAbandonOrCancelAssociatedMaterialLot;

    @ApiModelProperty("执行作业ID")
    private String eoId;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getRelaxedFlowFlag() {
        return relaxedFlowFlag;
    }

    public void setRelaxedFlowFlag(String relaxedFlowFlag) {
        this.relaxedFlowFlag = relaxedFlowFlag;
    }

    public String getEoAbandonAndComponentAbandon() {
        return eoAbandonAndComponentAbandon;
    }

    public void setEoAbandonAndComponentAbandon(String eoAbandonAndComponentAbandon) {
        this.eoAbandonAndComponentAbandon = eoAbandonAndComponentAbandon;
    }

    public String getAssembleAbandonOrCancelAssociatedMaterialLot() {
        return assembleAbandonOrCancelAssociatedMaterialLot;
    }

    public void setAssembleAbandonOrCancelAssociatedMaterialLot(String assembleAbandonOrCancelAssociatedMaterialLot) {
        this.assembleAbandonOrCancelAssociatedMaterialLot = assembleAbandonOrCancelAssociatedMaterialLot;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }
}
