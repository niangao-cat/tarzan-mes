package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtMaterialLotVO5 implements Serializable {

    private static final long serialVersionUID = -4222149029370995974L;
    private String materialLotId;
    private String materialId;// 物料
    private String primaryUomId;// 主单位
    private Double primaryUomQty;// 主单位数量
    private String secondaryUomId;// 辅助单位
    private Double secondaryUomQty;// 辅助单位数量
    private String mPrimaryUomId;// 物料主单位
    private Double mPrimaryUomQty;// 物料主单位数量
    private String mSecondaryUomId;// 物料辅助单位
    private Double mSecondaryUomQty;// 物料辅助单位数量

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getPrimaryUomId() {
        return primaryUomId;
    }

    public void setPrimaryUomId(String primaryUomId) {
        this.primaryUomId = primaryUomId;
    }

    public Double getPrimaryUomQty() {
        return primaryUomQty;
    }

    public void setPrimaryUomQty(Double primaryUomQty) {
        this.primaryUomQty = primaryUomQty;
    }

    public String getSecondaryUomId() {
        return secondaryUomId;
    }

    public void setSecondaryUomId(String secondaryUomId) {
        this.secondaryUomId = secondaryUomId;
    }

    public Double getSecondaryUomQty() {
        return secondaryUomQty;
    }

    public void setSecondaryUomQty(Double secondaryUomQty) {
        this.secondaryUomQty = secondaryUomQty;
    }

    public String getmPrimaryUomId() {
        return mPrimaryUomId;
    }

    public void setmPrimaryUomId(String mPrimaryUomId) {
        this.mPrimaryUomId = mPrimaryUomId;
    }

    public Double getmPrimaryUomQty() {
        return mPrimaryUomQty;
    }

    public void setmPrimaryUomQty(Double mPrimaryUomQty) {
        this.mPrimaryUomQty = mPrimaryUomQty;
    }

    public String getmSecondaryUomId() {
        return mSecondaryUomId;
    }

    public void setmSecondaryUomId(String mSecondaryUomId) {
        this.mSecondaryUomId = mSecondaryUomId;
    }

    public Double getmSecondaryUomQty() {
        return mSecondaryUomQty;
    }

    public void setmSecondaryUomQty(Double mSecondaryUomQty) {
        this.mSecondaryUomQty = mSecondaryUomQty;
    }
}
