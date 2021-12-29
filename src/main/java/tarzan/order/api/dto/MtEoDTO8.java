package tarzan.order.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/12/24 3:36 下午
 */
public class MtEoDTO8 implements Serializable {
    private static final long serialVersionUID = -1971228798673735427L;
    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("EO编码")
    private String eoNum;
    @ApiModelProperty("EO数量")
    private Double eoQty;
    @ApiModelProperty("已下达数量")
    private Double releaseQty;
    @ApiModelProperty("已完成数量")
    private Double completedQty;
    @ApiModelProperty("未下达数量")
    private Double unReleaseQty;
    @ApiModelProperty("拆分数量")
    private Double splitQty;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getEoNum() {
        return eoNum;
    }

    public void setEoNum(String eoNum) {
        this.eoNum = eoNum;
    }

    public Double getEoQty() {
        return eoQty;
    }

    public void setEoQty(Double eoQty) {
        this.eoQty = eoQty;
    }

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

    public Double getUnReleaseQty() {
        return unReleaseQty;
    }

    public void setUnReleaseQty(Double unReleaseQty) {
        this.unReleaseQty = unReleaseQty;
    }

    public Double getSplitQty() {
        return splitQty;
    }

    public void setSplitQty(Double splitQty) {
        this.splitQty = splitQty;
    }
}
