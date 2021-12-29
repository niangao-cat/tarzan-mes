package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年12月16日 下午2:45:26
 *
 */
public class MtWorkOrderVO38 implements Serializable {

    private static final long serialVersionUID = -4389218982523487574L;

    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "生产指令编码")
    private String workOrderNum;
    @ApiModelProperty(value = "生产指令类型")
    private List<String> workOrderType;
    @ApiModelProperty(value = "生产线")
    private String productionLineId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "生产指令状态")
    private List<String> status;
    @ApiModelProperty(value = "开始时间从")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planStartTimeFrom;
    @ApiModelProperty(value = "开始时间至")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planStartTimeTo;
    @ApiModelProperty(value = "结束时间从")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planEndTimeFrom;
    @ApiModelProperty(value = "结束时间至")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planEndTimeTo;
    @ApiModelProperty(value = "客户ID")
    private String customerId;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getWorkOrderNum() {
        return workOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
    }

    public String getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getPlanStartTimeFrom() {
        if (planStartTimeFrom != null) {
            return (Date) planStartTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setPlanStartTimeFrom(Date planStartTimeFrom) {
        if (planStartTimeFrom == null) {
            this.planStartTimeFrom = null;
        } else {
            this.planStartTimeFrom = (Date) planStartTimeFrom.clone();
        }
    }

    public Date getPlanStartTimeTo() {
        if (planStartTimeTo != null) {
            return (Date) planStartTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setPlanStartTimeTo(Date planStartTimeTo) {
        if (planStartTimeTo == null) {
            this.planStartTimeTo = null;
        } else {
            this.planStartTimeTo = (Date) planStartTimeTo.clone();
        }
    }

    public Date getPlanEndTimeFrom() {
        if (planEndTimeFrom != null) {
            return (Date) planEndTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setPlanEndTimeFrom(Date planEndTimeFrom) {
        if (planEndTimeFrom == null) {
            this.planEndTimeFrom = null;
        } else {
            this.planEndTimeFrom = (Date) planEndTimeFrom.clone();
        }
    }

    public Date getPlanEndTimeTo() {
        if (planEndTimeTo != null) {
            return (Date) planEndTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setPlanEndTimeTo(Date planEndTimeTo) {
        if (planEndTimeTo == null) {
            this.planEndTimeTo = null;
        } else {
            this.planEndTimeTo = (Date) planEndTimeTo.clone();
        }
    }

    public List<String> getWorkOrderType() {
        return workOrderType;
    }

    public void setWorkOrderType(List<String> workOrderType) {
        this.workOrderType = workOrderType;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }
}
