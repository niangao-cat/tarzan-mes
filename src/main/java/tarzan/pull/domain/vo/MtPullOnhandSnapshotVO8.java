package tarzan.pull.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtPullOnhandSnapshotVO8
 *
 * @author: {xieyiyang}
 * @date: 2020/2/12 20:57
 * @description:
 */
public class MtPullOnhandSnapshotVO8 implements Serializable {
    private static final long serialVersionUID = 5579266827390040776L;

    @ApiModelProperty("需求配送数")
    private Double distributionQty;
    @ApiModelProperty("整包配送标识")
    private String multiplesOfPackFlag;
    @ApiModelProperty("配送包装数")
    private Double packQty;

    public Double getDistributionQty() {
        return distributionQty;
    }

    public void setDistributionQty(Double distributionQty) {
        this.distributionQty = distributionQty;
    }

    public String getMultiplesOfPackFlag() {
        return multiplesOfPackFlag;
    }

    public void setMultiplesOfPackFlag(String multiplesOfPackFlag) {
        this.multiplesOfPackFlag = multiplesOfPackFlag;
    }

    public Double getPackQty() {
        return packQty;
    }

    public void setPackQty(Double packQty) {
        this.packQty = packQty;
    }
}
