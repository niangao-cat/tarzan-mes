package tarzan.pull.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtPullOnhandSnapshotVO3
 *
 * @author: {xieyiyang}
 * @date: 2020/2/5 19:37
 * @description:
 */
public class MtPullOnhandSnapshotVO3 implements Serializable {
    private static final long serialVersionUID = -2094776326396337381L;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty("站点Id")
    private String siteId;
    @ApiModelProperty(value = "配送周期T")
    private Double distributionCycle;
    @ApiModelProperty(value = "库位ID")
    private String locatorId;
    @ApiModelProperty(value = "快照版本")
    private String snapshotRevision;
    @ApiModelProperty(value = "触发到送达的周期P")
    private Double pullToArrive;
    @ApiModelProperty(value = "物料消耗速率@")
    private List<Double> materialConsumeRate;
    @ApiModelProperty(value = "安全库存a")
    private List<Double> bufferInventory;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public Double getDistributionCycle() {
        return distributionCycle;
    }

    public void setDistributionCycle(Double distributionCycle) {
        this.distributionCycle = distributionCycle;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getSnapshotRevision() {
        return snapshotRevision;
    }

    public void setSnapshotRevision(String snapshotRevision) {
        this.snapshotRevision = snapshotRevision;
    }

    public Double getPullToArrive() {
        return pullToArrive;
    }

    public void setPullToArrive(Double pullToArrive) {
        this.pullToArrive = pullToArrive;
    }

    public List<Double> getMaterialConsumeRate() {
        return materialConsumeRate;
    }

    public void setMaterialConsumeRate(List<Double> materialConsumeRate) {
        this.materialConsumeRate = materialConsumeRate;
    }

    public List<Double> getBufferInventory() {
        return bufferInventory;
    }

    public void setBufferInventory(List<Double> bufferInventory) {
        this.bufferInventory = bufferInventory;
    }
}
