package tarzan.pull.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtPullOnhandSnapshotVO4
 *
 * @author: {xieyiyang}
 * @date: 2020/2/5 19:48
 * @description:
 */
public class MtPullOnhandSnapshotVO4 implements Serializable {
    private static final long serialVersionUID = -3879405955673506346L;

    @ApiModelProperty(value = "配送标识")
    private String distributionFlag;
    @ApiModelProperty(value = "配送数量")
    private Double distributionQty;

    public String getDistributionFlag() {
        return distributionFlag;
    }

    public void setDistributionFlag(String distributionFlag) {
        this.distributionFlag = distributionFlag;
    }

    public Double getDistributionQty() {
        return distributionQty;
    }

    public void setDistributionQty(Double distributionQty) {
        this.distributionQty = distributionQty;
    }
}
