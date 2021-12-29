package tarzan.order.domain.vo;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年12月16日 下午2:45:33
 *
 */
public class MtWorkOrderVO50 implements Serializable {

    private static final long serialVersionUID = -3341321378118237577L;
    
    @ApiModelProperty(value = "生产指令ID",required = true)
    @NotEmpty
    private String workOrderId;
    @ApiModelProperty(value = "EO生成个数",required = true)
    @NotNull
    private Double eoCount;
    @ApiModelProperty(value = "单位数量",required = true)
    @NotNull
    private Double unitQty;
    @ApiModelProperty(value = "下达数量",required = true)
    @NotNull
    private Double trxReleasedQty;
    
    public String getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
    public Double getEoCount() {
        return eoCount;
    }
    public void setEoCount(Double eoCount) {
        this.eoCount = eoCount;
    }
    public Double getTrxReleasedQty() {
        return trxReleasedQty;
    }
    public void setTrxReleasedQty(Double trxReleasedQty) {
        this.trxReleasedQty = trxReleasedQty;
    }
    public Double getUnitQty() {
        return unitQty;
    }
    public void setUnitQty(Double unitQty) {
        this.unitQty = unitQty;
    }
    
}
