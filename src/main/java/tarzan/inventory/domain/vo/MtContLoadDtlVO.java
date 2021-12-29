package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

public class MtContLoadDtlVO implements Serializable {
    private static final long serialVersionUID = 5684323514633138150L;

    @ApiModelProperty("容器")
    private String containerId;
    @ApiModelProperty("对象类型")
    private String loadObjectType;
    @ApiModelProperty("容器行")
    private Long locationRow;
    @ApiModelProperty("容器列")
    private Long locationColumn;
    @ApiModelProperty("是否获取所有层级")
    private String allLevelFlag;

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

    public String getAllLevelFlag() {
        return allLevelFlag;
    }

    public void setAllLevelFlag(String allLevelFlag) {
        this.allLevelFlag = allLevelFlag;
    }

    @Override
    public String toString() {
        return "MtContLoadDtlVO{" + "containerId='" + containerId + '\'' + ", loadObjectType='" + loadObjectType + '\''
                        + ", locationRow=" + locationRow + ", locationColumn=" + locationColumn + ", allLevelFlag='"
                        + allLevelFlag + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MtContLoadDtlVO that = (MtContLoadDtlVO) o;
        return Objects.equals(containerId, that.containerId) &&
                Objects.equals(loadObjectType, that.loadObjectType) &&
                Objects.equals(locationRow, that.locationRow) &&
                Objects.equals(locationColumn, that.locationColumn) &&
                Objects.equals(allLevelFlag, that.allLevelFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containerId, loadObjectType, locationRow, locationColumn, allLevelFlag);
    }
}
