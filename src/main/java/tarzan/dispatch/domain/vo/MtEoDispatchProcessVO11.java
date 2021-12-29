package tarzan.dispatch.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtEoDispatchProcessVO11
 * @description
 * @date 2019年10月14日 17:57
 */
public class MtEoDispatchProcessVO11 implements Serializable {
    private static final long serialVersionUID = 7829426684166305318L;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("生产线")
    private String productionLineId;
    @ApiModelProperty("站点")
    private String siteId;
    @ApiModelProperty("开始时间")
    private Date startTime;
    @ApiModelProperty("结束时间")
    private Date endTime;

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public Date getStartTime() {
        if (startTime == null) {
            return null;
        } else {
            return (Date) startTime.clone();
        }
    }

    public void setStartTime(Date startTime) {
        if (startTime == null) {
            this.startTime = null;
        } else {
            this.startTime = (Date) startTime.clone();
        }
    }

    public Date getEndTime() {
        if (endTime == null) {
            return null;
        } else {
            return (Date) endTime.clone();
        }
    }

    public void setEndTime(Date endTime) {
        if (endTime == null) {
            this.endTime = null;
        } else {
            this.endTime = (Date) endTime.clone();
        }
    }
}
