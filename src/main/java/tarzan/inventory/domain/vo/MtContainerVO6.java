package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/4/4 14:14
 */
public class MtContainerVO6 implements Serializable {
    private static final long serialVersionUID = -4949424217203516199L;

    @ApiModelProperty("容器ID")
    private String containerId;
    @ApiModelProperty("容器位置行")
    private Long locationRow;
    @ApiModelProperty("容器位置列")
    private Long locationColumn;

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
}
