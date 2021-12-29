package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/30 14:44
 * @Description:
 */
public class MtEoVO48 implements Serializable {
    private static final long serialVersionUID = -4490047098029658979L;

    @ApiModelProperty("生产指令ID")
    private String workOrderId;
    @ApiModelProperty("生产指令下eo的数量")
    private Long eoCount;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Long getEoCount() {
        return eoCount;
    }

    public void setEoCount(Long eoCount) {
        this.eoCount = eoCount;
    }
}
