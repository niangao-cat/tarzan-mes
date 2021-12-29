package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/11/22 10:38
 * @Author: ${yiyang.xie}
 */
public class MtEoVO35 implements Serializable {
    private static final long serialVersionUID = -6633405749191085152L;

    @ApiModelProperty("目标执行作业ID")
    private String targetEoId;

    @ApiModelProperty("拆分数量")
    private Double qty;

    public String getTargetEoId() {
        return targetEoId;
    }

    public void setTargetEoId(String targetEoId) {
        this.targetEoId = targetEoId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }
}
