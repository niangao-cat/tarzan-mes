package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/1/14 14:28
 * @Description:
 */
public class MtInstructionActualDetailVO4 implements Serializable {
    private static final long serialVersionUID = -7936297468158764168L;

    @ApiModelProperty("实绩ID")
    private String actualId;

    @ApiModelProperty("物料批ID")
    private String materialLotId;

    @ApiModelProperty("单位")
    private String uomId;

    @ApiModelProperty("实绩数量")
    private Double actualQty;

    @ApiModelProperty("器具id")
    private String containerId;

    @ApiModelProperty("来源库位ID")
    private String fromLocatorId;

    @ApiModelProperty("目标库位ID")
    private String toLocatorId;

    public String getActualId() {
        return actualId;
    }

    public void setActualId(String actualId) {
        this.actualId = actualId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public Double getActualQty() {
        return actualQty;
    }

    public void setActualQty(Double actualQty) {
        this.actualQty = actualQty;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getFromLocatorId() {
        return fromLocatorId;
    }

    public void setFromLocatorId(String fromLocatorId) {
        this.fromLocatorId = fromLocatorId;
    }

    public String getToLocatorId() {
        return toLocatorId;
    }

    public void setToLocatorId(String toLocatorId) {
        this.toLocatorId = toLocatorId;
    }
}
