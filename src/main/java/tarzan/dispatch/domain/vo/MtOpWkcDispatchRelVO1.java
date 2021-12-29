package tarzan.dispatch.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MtOpWkcDispatchRelVO1 implements Serializable {
    private static final long serialVersionUID = 3065755866535998175L;

    private String operationId; // 工艺
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate; // 班次日期
    private String shiftCode; // 班次编码
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

    public Date getShiftDate() {
        if (shiftDate == null) {
            return null;
        }
        return (Date) shiftDate.clone();
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
