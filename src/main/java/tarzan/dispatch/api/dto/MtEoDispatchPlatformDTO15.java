package tarzan.dispatch.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author : MrZ
 * @date : 2019-12-10 16:28
 **/
public class MtEoDispatchPlatformDTO15 implements Serializable {
    private static final long serialVersionUID = 8040140815321227632L;
    @ApiModelProperty(value = "主键ID ,表示唯一一条记录", required = true)
    private List<String> workcellIdList;
    @ApiModelProperty(value = "班次开始日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date shiftDateFrom;
    @ApiModelProperty(value = "班次结束日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date shiftDateTo;
    @ApiModelProperty(value = "工艺唯一标识", required = true)
    private String operationId;

    public List<String> getWorkcellIdList() {
        return workcellIdList;
    }

    public void setWorkcellIdList(List<String> workcellIdList) {
        this.workcellIdList = workcellIdList;
    }

    public Date getShiftDateFrom() {
        if (shiftDateFrom != null) {
            return (Date) shiftDateFrom.clone();
        } else {
            return null;
        }
    }

    public void setShiftDateFrom(Date shiftDateFrom) {
        if (shiftDateFrom == null) {
            this.shiftDateFrom = null;
        } else {
            this.shiftDateFrom = (Date) shiftDateFrom.clone();
        }
    }

    public Date getShiftDateTo() {
        if (shiftDateTo != null) {
            return (Date) shiftDateTo.clone();
        } else {
            return null;
        }
    }

    public void setShiftDateTo(Date shiftDateTo) {
        if (shiftDateTo == null) {
            this.shiftDateTo = null;
        } else {
            this.shiftDateTo = (Date) shiftDateTo.clone();
        }
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
