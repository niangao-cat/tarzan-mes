package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author chuang.yang
 * @date 2021/4/22
 */
public class MtMaterialLotVO33 implements Serializable {

    private static final long serialVersionUID = -3028696221826618373L;
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty("顺序")
    private Long sequence;
    @ApiModelProperty("是否被消耗标识")
    private String consumeFlag;

    private BigDecimal trxQty;

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getConsumeFlag() {
        return consumeFlag;
    }

    public void setConsumeFlag(String consumeFlag) {
        this.consumeFlag = consumeFlag;
    }

    public BigDecimal getTrxQty() {
        return trxQty;
    }

    public void setTrxQty(BigDecimal trxQty) {
        this.trxQty = trxQty;
    }
}
