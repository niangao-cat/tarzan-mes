package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author chuang.yang
 * @date 2021/4/22
 */
public class MtMaterialLotVO32 implements Serializable {

    private static final long serialVersionUID = -8547355036163224543L;
    @ApiModelProperty("物料Id")
    private String materialId;

    @ApiModelProperty("数量")
    private Double trxPrimaryUomQty;

    @ApiModelProperty("物料批")
    List<MtMaterialLotVO33> mtMaterialLotVO33List;


    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public Double getTrxPrimaryUomQty() {
        return trxPrimaryUomQty;
    }

    public void setTrxPrimaryUomQty(Double trxPrimaryUomQty) {
        this.trxPrimaryUomQty = trxPrimaryUomQty;
    }

    public List<MtMaterialLotVO33> getMtMaterialLotVO33List() {
        return mtMaterialLotVO33List;
    }

    public void setMtMaterialLotVO33List(List<MtMaterialLotVO33> mtMaterialLotVO33List) {
        this.mtMaterialLotVO33List = mtMaterialLotVO33List;
    }

    public MtMaterialLotVO32(String materialId, Double trxPrimaryUomQty,
                    List<MtMaterialLotVO33> mtMaterialLotVO16List) {
        this.materialId = materialId;
        this.trxPrimaryUomQty = trxPrimaryUomQty;
        this.mtMaterialLotVO33List = mtMaterialLotVO16List;
    }

    public MtMaterialLotVO32() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtMaterialLotVO32 that = (MtMaterialLotVO32) o;
        return Objects.equals(getMaterialId(), that.getMaterialId())
                        && Objects.equals(getTrxPrimaryUomQty(), that.getTrxPrimaryUomQty())
                        && Objects.equals(getMtMaterialLotVO33List(), that.getMtMaterialLotVO33List());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaterialId(), getTrxPrimaryUomQty(), getMtMaterialLotVO33List());
    }
}
