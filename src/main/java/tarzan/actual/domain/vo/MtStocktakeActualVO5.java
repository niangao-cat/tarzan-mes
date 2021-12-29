package tarzan.actual.domain.vo;

import java.io.Serializable;

import tarzan.actual.domain.entity.MtStocktakeActual;
import tarzan.inventory.domain.entity.MtMaterialLot;

/**
 * @author Leeloing
 * @date 2019/7/9 19:08
 */
public class MtStocktakeActualVO5 implements Serializable {

    private static final long serialVersionUID = -1165500040297452272L;

    private String materialUomId;
    private Double materialQty;
    private String adjustMaterialUomId;
    private Double adjustTargetValue;
    private MtStocktakeActual actual;
    private MtStocktakeActualVO3 actualVO3;
    private MtMaterialLot mtMaterialLot;
    private Boolean adjustFlag;
    private String adjustMaterialSecondaryUomId;

    public MtStocktakeActual getActual() {
        return actual;
    }

    public void setActual(MtStocktakeActual actual) {
        this.actual = actual;
    }

    public Double getMaterialQty() {
        return materialQty;
    }

    public void setMaterialQty(Double materialQty) {
        this.materialQty = materialQty;
    }

    public String getMaterialUomId() {
        return materialUomId;
    }

    public void setMaterialUomId(String materialUomId) {
        this.materialUomId = materialUomId;
    }

    public String getAdjustMaterialUomId() {
        return adjustMaterialUomId;
    }

    public void setAdjustMaterialUomId(String adjustMaterialUomId) {
        this.adjustMaterialUomId = adjustMaterialUomId;
    }

    public Double getAdjustTargetValue() {
        return adjustTargetValue;
    }

    public void setAdjustTargetValue(Double adjustTargetValue) {
        this.adjustTargetValue = adjustTargetValue;
    }

    public MtStocktakeActualVO3 getActualVO3() {
        return actualVO3;
    }

    public void setActualVO3(MtStocktakeActualVO3 actualVO3) {
        this.actualVO3 = actualVO3;
    }

    public MtMaterialLot getMtMaterialLot() {
        return mtMaterialLot;
    }

    public void setMtMaterialLot(MtMaterialLot mtMaterialLot) {
        this.mtMaterialLot = mtMaterialLot;
    }

    public Boolean getAdjustFlag() {
        return adjustFlag;
    }

    public void setAdjustFlag(Boolean adjustFlag) {
        this.adjustFlag = adjustFlag;
    }

    public String getAdjustMaterialSecondaryUomId() {
        return adjustMaterialSecondaryUomId;
    }

    public void setAdjustMaterialSecondaryUomId(String adjustMaterialSecondaryUomId) {
        this.adjustMaterialSecondaryUomId = adjustMaterialSecondaryUomId;
    }

}
