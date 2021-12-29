package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-10-30 15:04
 */
public class MtContLoadDtlVO16 implements Serializable {
    private static final long serialVersionUID = 6544557227382989668L;
    @ApiModelProperty("容器主键ID")
    private String containerId;
    @ApiModelProperty("容器行")
    private Long locationRow;
    @ApiModelProperty("容器列")
    private Long locationColumn;
    @ApiModelProperty("装载对象类型")
    private String loadObjectType;
    @ApiModelProperty("装载对象ID")
    private String loadObjectId;
    @ApiModelProperty("本次装载数量")
    private Double trxLoadQty;
    @ApiModelProperty("装载步骤")
    private Double loadQty;
    @ApiModelProperty("装载步骤")
    private String loadEoStepActualId;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public Long getLocationRow() {
        return locationRow;
    }

    public void setLocationRow(Long locationRow) {
        this.locationRow = locationRow;
    }

    public Long getLocationColumn() {
        return locationColumn;
    }

    public void setLocationColumn(Long locationColumn) {
        this.locationColumn = locationColumn;
    }

    public String getLoadObjectType() {
        return loadObjectType;
    }

    public void setLoadObjectType(String loadObjectType) {
        this.loadObjectType = loadObjectType;
    }

    public String getLoadObjectId() {
        return loadObjectId;
    }

    public void setLoadObjectId(String loadObjectId) {
        this.loadObjectId = loadObjectId;
    }

    public Double getTrxLoadQty() {
        return trxLoadQty;
    }

    public void setTrxLoadQty(Double trxLoadQty) {
        this.trxLoadQty = trxLoadQty;
    }

    public Double getLoadQty() {
        return loadQty;
    }

    public void setLoadQty(Double loadQty) {
        this.loadQty = loadQty;
    }

    public String getLoadEoStepActualId() {
        return loadEoStepActualId;
    }

    public void setLoadEoStepActualId(String loadEoStepActualId) {
        this.loadEoStepActualId = loadEoStepActualId;
    }
}
