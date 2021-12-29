package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/2/18 10:12 上午
 */
public class MtProcessWorkingDTO20 implements Serializable {
    private static final long serialVersionUID = -3149600808102282138L;
    @ApiModelProperty("组件ID")
    private String bomComponentId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料批Id")
    private String materialLotId;
    @ApiModelProperty("装配数量")
    private Double assembleQty;
    @ApiModelProperty("强制装配标识")
    private String assembleExcessFlag;

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public Double getAssembleQty() {
        return assembleQty;
    }

    public void setAssembleQty(Double assembleQty) {
        this.assembleQty = assembleQty;
    }

    public String getAssembleExcessFlag() {
        return assembleExcessFlag;
    }

    public void setAssembleExcessFlag(String assembleExcessFlag) {
        this.assembleExcessFlag = assembleExcessFlag;
    }
}
