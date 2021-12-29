package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtEoRouterActualVO34
 * @description
 * @date 2019年11月25日 18:34
 */
public class MtEoRouterActualVO34 implements Serializable {
    private static final long serialVersionUID = 2028373790880964409L;

    @ApiModelProperty(value = "目标执行作业唯一标识")
    private String targetEoId;

    @ApiModelProperty(value = "数量")
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
