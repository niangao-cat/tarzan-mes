package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/11/13 11:23
 * @Description:
 */
public class MtContLoadDtlVO31 implements Serializable {

    private static final long serialVersionUID = 6910345884262267375L;
    @ApiModelProperty("容器ID")
    private String containerId;
    @ApiModelProperty("容器行")
    private Long locationRow;
    @ApiModelProperty("容器列")
    private Long locationColumn;

    private List<MtContLoadDtlVO13> unloadObjectList;

    @ApiModelProperty("事件ID")
    private String eventId;

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

    public List<MtContLoadDtlVO13> getUnloadObjectList() {
        return unloadObjectList;
    }

    public void setUnloadObjectList(List<MtContLoadDtlVO13> unloadObjectList) {
        this.unloadObjectList = unloadObjectList;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
