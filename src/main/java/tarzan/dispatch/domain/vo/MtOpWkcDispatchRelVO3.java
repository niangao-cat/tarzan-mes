package tarzan.dispatch.domain.vo;

import java.io.Serializable;

public class MtOpWkcDispatchRelVO3 implements Serializable {
    private static final long serialVersionUID = 7187382401922506378L;

    private String operationId; // 工艺
    private String stepName; // 步骤识别码
    private String eoId; // 执行作业
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

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
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
