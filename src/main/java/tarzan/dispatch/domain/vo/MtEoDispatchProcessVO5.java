package tarzan.dispatch.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtEoDispatchProcessVO5 implements Serializable {

    private static final long serialVersionUID = -7060295594348812399L;

    private String operationId; // 执行作业步骤工艺
    private String stepName; // 执行作业步骤识别码
    private Date planTimeFrom; // 执行作业计划开始时间
    private Date planTimeTo; // 执行作业计划结束时间
    private String productionLineId; // 生产线
    private String siteId; // 站点

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public Date getPlanTimeFrom() {

        if (planTimeFrom == null) {
            return null;
        } else {
            return (Date) planTimeFrom.clone();
        }

    }

    public void setPlanTimeFrom(Date planTimeFrom) {
        if (planTimeFrom == null) {
            this.planTimeFrom = null;
        } else {
            this.planTimeFrom = (Date) planTimeFrom.clone();
        }
    }

    public Date getPlanTimeTo() {

        if (planTimeTo == null) {
            return null;
        } else {
            return (Date) planTimeTo.clone();
        }

    }

    public void setPlanTimeTo(Date planTimeTo) {
        if (planTimeTo == null) {
            this.planTimeTo = null;
        } else {
            this.planTimeTo = (Date) planTimeTo.clone();
        }
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
}
