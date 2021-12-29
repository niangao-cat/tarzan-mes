package tarzan.inventory.domain.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/10/15 14:20
 * @Description:
 */
public class MtMaterialLotVO19 implements Serializable {

    private static final long serialVersionUID = 2678258932268234972L;
    @ApiModelProperty("物料批ID")
    private String materialLotId;

    @ApiModelProperty(value = "主单位事务数量", hidden = true)
    @JsonIgnore
    private transient Double trxPrimaryUomQty;

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public Double getTrxPrimaryUomQty() {
        return trxPrimaryUomQty;
    }

    public void setTrxPrimaryUomQty(Double trxPrimaryUomQty) {
        this.trxPrimaryUomQty = trxPrimaryUomQty;
    }
}
