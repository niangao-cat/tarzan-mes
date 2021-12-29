package tarzan.order.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/12/24 3:46 下午
 */
public class MtEoDTO9 implements Serializable {
    private static final long serialVersionUID = -5644931934887991224L;
    @ApiModelProperty("已下达数量")
    private Double releaseQty;
    @ApiModelProperty("已完成数量")
    private Double completedQty;

    public Double getReleaseQty() {
        return releaseQty;
    }

    public void setReleaseQty(Double releaseQty) {
        this.releaseQty = releaseQty;
    }

    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }
}
