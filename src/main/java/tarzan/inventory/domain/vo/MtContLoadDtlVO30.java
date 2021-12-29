package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/11/13 11:03
 * @Description:
 */
public class MtContLoadDtlVO30 implements Serializable {

    private static final long serialVersionUID = -4822578520919121326L;
    @ApiModelProperty("容器ID")
    private String containerId;
    @ApiModelProperty("容器行")
    private Long locationRow;
    @ApiModelProperty("容器列")
    private Long locationColumn;

    private List<MtContLoadDtlVO29> unLoadObjectList;

    private String workcellId;
    private String parentEventId;
    private String eventRequestId;
    private Date shiftDate;
    private String shiftCode;

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

    public List<MtContLoadDtlVO29> getUnLoadObjectList() {
        return unLoadObjectList;
    }

    public void setUnLoadObjectList(List<MtContLoadDtlVO29> unLoadObjectList) {
        this.unLoadObjectList = unLoadObjectList;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getParentEventId() {
        return parentEventId;
    }

    public void setParentEventId(String parentEventId) {
        this.parentEventId = parentEventId;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public Date getShiftDate() {
        if (shiftDate != null) {
            return (Date) shiftDate.clone();
        } else {
            return null;
        }
    }

    public void setShiftDate(Date shiftDate) {
        if (shiftDate == null) {
            this.shiftDate = null;
        } else {
            this.shiftDate = (Date) shiftDate.clone();
        }
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }
}
