package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/9 10:50
 * @Author: ${yiyang.xie}
 */
public class MtAssembleConfirmActualVO16 implements Serializable {
    private static final long serialVersionUID = -4302459290173777559L;
    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "装配数量")
    private Double assembleQty;

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getMaterialLotCode() {
        return materialLotCode;
    }

    public void setMaterialLotCode(String materialLotCode) {
        this.materialLotCode = materialLotCode;
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

    public Double getAssembleQty() {
        return assembleQty;
    }

    public void setAssembleQty(Double assembleQty) {
        this.assembleQty = assembleQty;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtAssembleConfirmActualVO16 that = (MtAssembleConfirmActualVO16) o;
        return Objects.equals(getMaterialLotId(), that.getMaterialLotId())
                && Objects.equals(getMaterialLotCode(), that.getMaterialLotCode())
                && Objects.equals(getMaterialId(), that.getMaterialId())
                && Objects.equals(getMaterialCode(), that.getMaterialCode())
                && Objects.equals(getAssembleQty(), that.getAssembleQty());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaterialLotId(), getMaterialLotCode(), getMaterialId(), getMaterialCode(),
                getAssembleQty());
    }
}
