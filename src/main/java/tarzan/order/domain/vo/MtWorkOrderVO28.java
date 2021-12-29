package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/9/17 11:38
 */
public class MtWorkOrderVO28 implements Serializable {
    private static final long serialVersionUID = -3362679872917001072L;

    @ApiModelProperty("生产指令Id")
    private String workOrderId;
    @ApiModelProperty("生产指令历史Id")
    private String workOrderHisId;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderHisId() {
        return workOrderHisId;
    }

    public void setWorkOrderHisId(String workOrderHisId) {
        this.workOrderHisId = workOrderHisId;
    }
}
