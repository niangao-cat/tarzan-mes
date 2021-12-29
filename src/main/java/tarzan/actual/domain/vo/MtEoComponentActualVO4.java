package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Leeloing
 * @date 2019/3/13 11:39
 */
public class MtEoComponentActualVO4 implements Serializable {
    private static final long serialVersionUID = 168018889621873777L;

    private String eoId;
    private String materialId;
    private String bomComponentId;
    private String operationId;
    private String routerStepId;
    private Double trxScrappedQty;
    private String assembleExcessFlag;
    private String bomId;
    private String workcellId;
    private String locatorId;
    private String parentEventId;
    private String eventRequestId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    private String shiftCode;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public Double getTrxScrappedQty() {
        return trxScrappedQty;
    }

    public void setTrxScrappedQty(Double trxScrappedQty) {
        this.trxScrappedQty = trxScrappedQty;
    }

    public String getAssembleExcessFlag() {
        return assembleExcessFlag;
    }

    public void setAssembleExcessFlag(String assembleExcessFlag) {
        this.assembleExcessFlag = assembleExcessFlag;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

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

    public String getParentEventId() {
        return parentEventId;
    }

    public void setParentEventId(String parentEventId) {
        this.parentEventId = parentEventId;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
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
}
