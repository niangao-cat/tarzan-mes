package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtContLoadDtlVO19
 * @description
 * @date 2020年02月05日 15:12
 */
public class MtContLoadDtlVO19 implements Serializable {
    private static final long serialVersionUID = -7571721826046520492L;
    @ApiModelProperty(value = "容器")
    private String containerId;
    @ApiModelProperty(value = "对象类型")
    private String loadObjectType;
    @ApiModelProperty(value = "对象值")
    private String loadObjectId;
    @ApiModelProperty(value = "容器行")
    private String locationRow;
    @ApiModelProperty(value = "容器列")
    private String locationColumn;
    @ApiModelProperty(value = "装载顺序")
    private String loadSequence;
    @ApiModelProperty(value = "装载数量")
    private Double loadQty;
    @ApiModelProperty(value = "装载步骤")
    private String loadEoStepActualId;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
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

    public String getLocationRow() {
        return locationRow;
    }

    public void setLocationRow(String locationRow) {
        this.locationRow = locationRow;
    }

    public String getLocationColumn() {
        return locationColumn;
    }

    public void setLocationColumn(String locationColumn) {
        this.locationColumn = locationColumn;
    }

    public String getLoadSequence() {
        return loadSequence;
    }

    public void setLoadSequence(String loadSequence) {
        this.loadSequence = loadSequence;
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
