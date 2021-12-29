package tarzan.inventory.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtContainerDTO3 implements Serializable {
    private static final long serialVersionUID = -5212222600955693957L;

    @ApiModelProperty(value = "容器Id")
    private String containerId;
    @ApiModelProperty(value = "本次装载机对象类型")
    private String loadObjectType;
    @ApiModelProperty(value = "本次装载对象值")
    private String loadObjectId;

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
}
