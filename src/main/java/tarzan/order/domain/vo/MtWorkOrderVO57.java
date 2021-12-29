package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年12月20日 下午6:38:45
 *
 */
public class MtWorkOrderVO57 implements Serializable {

    private static final long serialVersionUID = -2451409199928730165L;

    @ApiModelProperty(value = "生产指令ID")
    private String workOrderId;
    @ApiModelProperty(value = "生产指令编码")
    private String workOrderNum;
    @ApiModelProperty(value = "生产指令数量")
    private Double qty;
    @ApiModelProperty(value = "最大下达数量")
    private Double maxQty;
    @ApiModelProperty(value = "可下达数量")
    private Double canQty;
    @ApiModelProperty(value = "完工限制数量")
    private Double completeControlQty;
    @ApiModelProperty(value = "已下达数量")
    private Double releasedQty;
    @ApiModelProperty(value = "已完成数量")
    private Double completedQty;
    @ApiModelProperty(value = "完工控制类型")
    private String completeControlType;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderNum() {
        return workOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getReleasedQty() {
        return releasedQty;
    }

    public void setReleasedQty(Double releasedQty) {
        this.releasedQty = releasedQty;
    }

    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }

    public Double getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(Double maxQty) {
        this.maxQty = maxQty;
    }

    public Double getCanQty() {
        return canQty;
    }

    public void setCanQty(Double canQty) {
        this.canQty = canQty;
    }

    public Double getCompleteControlQty() {
        return completeControlQty;
    }

    public void setCompleteControlQty(Double completeControlQty) {
        this.completeControlQty = completeControlQty;
    }

    public String getCompleteControlType() {
        return completeControlType;
    }

    public void setCompleteControlType(String completeControlType) {
        this.completeControlType = completeControlType;
    }
}
