package tarzan.dispatch.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author : MrZ
 * @date : 2019-12-04 10:50
 **/
public class MtEoDispatchPlatformDTO6 implements Serializable {
    private static final long serialVersionUID = 2706474354418850649L;
    @ApiModelProperty(value = "班次日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    @ApiModelProperty(value = "工艺唯一标识", required = true)
    private String operationId;
    @ApiModelProperty(value = "工作单元ID数组")
    private List<String> workcellIdList;
    @ApiModelProperty(value = "生产线ID", required = true)
    private String prodLineId;
    @ApiModelProperty(value = "用户默认站点ID", required = true)
    private String defaultSiteId;

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

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public List<String> getWorkcellIdList() {
        return workcellIdList;
    }

    public void setWorkcellIdList(List<String> workcellIdList) {
        this.workcellIdList = workcellIdList;
    }

    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    public String getDefaultSiteId() {
        return defaultSiteId;
    }

    public void setDefaultSiteId(String defaultSiteId) {
        this.defaultSiteId = defaultSiteId;
    }
}
