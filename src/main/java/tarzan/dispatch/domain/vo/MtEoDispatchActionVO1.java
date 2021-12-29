package tarzan.dispatch.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MtEoDispatchActionVO1 implements Serializable {
    private static final long serialVersionUID = -5940926694547128061L;

    private String workcellId; // 工作单元ID
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate; // 班次日期
    private String shiftCode; // 班次编码
    private String operationId; // 工艺
    private String productionLineId; // 生产线
    private String siteId; // 站点

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public void setShiftDate(Date shiftDate) {
        if (shiftDate != null) {
            this.shiftDate = (Date) shiftDate.clone();
        } else {
            this.shiftDate = null;
        }
    }

    public Date getShiftDate() {
        if (this.shiftDate == null) {
            return null;
        } else {
            return (Date) this.shiftDate.clone();
        }
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

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
}
