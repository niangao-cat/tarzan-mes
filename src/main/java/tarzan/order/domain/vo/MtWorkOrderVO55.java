package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年12月20日 下午6:38:45
 *
 */
public class MtWorkOrderVO55 implements Serializable {
    
    private static final long serialVersionUID = -2451409199928730165L;
    
    @ApiModelProperty(value = "生产指令ID")
    private String workOrderId;
    @ApiModelProperty(value = "拆分数量")
    private Double splitQty;
    
    public String getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
    public Double getSplitQty() {
        return splitQty;
    }
    public void setSplitQty(Double splitQty) {
        this.splitQty = splitQty;
    }
    
}
