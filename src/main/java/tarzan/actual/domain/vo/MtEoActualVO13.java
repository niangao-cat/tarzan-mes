package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/10/26 14:35
 */
public class MtEoActualVO13 implements Serializable {
    private static final long serialVersionUID = 2060242913921124286L;

    @ApiModelProperty("工作单元")
    private String workcellId;
    @ApiModelProperty("货位")
    private String locatorId;
    @ApiModelProperty("事件请求")
    private String eventRequestId;
    @ApiModelProperty("事件所属班次日期")
    private Date shiftDate;
    @ApiModelProperty("事件班次代码")
    private String shiftCode;

    @ApiModelProperty("执行作业实绩ID")
    private List<String> eoIdList;

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public Date getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(Date shiftDate) {
        this.shiftDate = shiftDate;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public List<String> getEoIdList() {
        return eoIdList;
    }

    public void setEoIdList(List<String> eoIdList) {
        this.eoIdList = eoIdList;
    }
}
