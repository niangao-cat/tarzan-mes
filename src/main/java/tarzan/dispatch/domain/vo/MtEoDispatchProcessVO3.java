package tarzan.dispatch.domain.vo;

import java.io.Serializable;

public class MtEoDispatchProcessVO3 implements Serializable {
    private static final long serialVersionUID = -1587398336403696433L;

    private String operationId;
    private String stepName;
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
