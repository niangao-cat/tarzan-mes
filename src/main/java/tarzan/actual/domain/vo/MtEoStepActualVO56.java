package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/11/4 16:13
 * @Description:
 */
public class MtEoStepActualVO56 implements Serializable {
    private static final long serialVersionUID = -9155749541198276713L;

    @ApiModelProperty("事件请求ID")
    private String eventRequestId;
    @ApiModelProperty("工作单元ID")
    private String workcellId;
    @ApiModelProperty("班次编码")
    private String shiftCode;
    @ApiModelProperty("班次时间")
    private Date shiftDate;
    @ApiModelProperty("工艺路线实绩集合")
    private List<MtEoRouterActualVO50> eoRouterActualList;

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public Date getShiftDate() {
        if (shiftDate == null) {
            return null;
        } else {
            return (Date) shiftDate.clone();
        }
    }

    public void setShiftDate(Date shiftDate) {
        if (shiftDate == null) {
            this.shiftDate = null;
        } else {
            this.shiftDate = (Date) shiftDate.clone();
        }
    }

    public List<MtEoRouterActualVO50> getEoRouterActualList() {
        return eoRouterActualList;
    }

    public void setEoRouterActualList(List<MtEoRouterActualVO50> eoRouterActualList) {
        this.eoRouterActualList = eoRouterActualList;
    }
}
