package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtWorkOrderVO29 implements Serializable {
    private static final long serialVersionUID = -6803822496058256091L;

    @ApiModelProperty("生产指令ID")
    private String workOrderId;
    @ApiModelProperty("本次完成数量")
    private Double trxCompletedQty;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Double getTrxCompletedQty() {
        return trxCompletedQty;
    }

    public void setTrxCompletedQty(Double trxCompletedQty) {
        this.trxCompletedQty = trxCompletedQty;
    }
}
