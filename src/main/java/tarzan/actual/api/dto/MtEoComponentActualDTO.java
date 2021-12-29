package tarzan.actual.api.dto;

import java.io.Serializable;
import java.util.Date;

public class MtEoComponentActualDTO implements Serializable {
    private static final long serialVersionUID = 2411650081441451480L;
    private String eoComponentActualId;
    private String eoId;
    private String materialId;
    private String operationId;
    private Double assembleQty;
    private Double scrappedQty;
    private String componentType;
    private String bomComponentId;
    private String bomId;
    private String routerStepId;
    private String assembleExcessFlag;
    private String assembleRouterType;
    private String substituteFlag;
    private Date actualFirstTime;
    private Date actualLastTime;
    private String eventId;
    private Double trxAssembleQty;
    private Double trxScrappedQty;

    public String getEoComponentActualId() {
        return eoComponentActualId;
    }

    public void setEoComponentActualId(String eoComponentActualId) {
        this.eoComponentActualId = eoComponentActualId;
    }

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

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public Double getAssembleQty() {
        return assembleQty;
    }

    public void setAssembleQty(Double assembleQty) {
        this.assembleQty = assembleQty;
    }

    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getAssembleExcessFlag() {
        return assembleExcessFlag;
    }

    public void setAssembleExcessFlag(String assembleExcessFlag) {
        this.assembleExcessFlag = assembleExcessFlag;
    }

    public String getAssembleRouterType() {
        return assembleRouterType;
    }

    public void setAssembleRouterType(String assembleRouterType) {
        this.assembleRouterType = assembleRouterType;
    }

    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

    public Date getActualFirstTime() {
        if (actualFirstTime != null) {
            return (Date) actualFirstTime.clone();
        } else {
            return null;
        }
    }

    public void setActualFirstTime(Date actualFirstTime) {
        if (actualFirstTime == null) {
            this.actualFirstTime = null;
        } else {
            this.actualFirstTime = (Date) actualFirstTime.clone();
        }
    }

    public Date getActualLastTime() {
        if (actualLastTime != null) {
            return (Date) actualLastTime.clone();
        } else {
            return null;
        }
    }

    public void setActualLastTime(Date actualLastTime) {
        if (actualLastTime == null) {
            this.actualLastTime = null;
        } else {
            this.actualLastTime = (Date) actualLastTime.clone();
        }
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Double getTrxAssembleQty() {
        return trxAssembleQty;
    }

    public void setTrxAssembleQty(Double trxAssembleQty) {
        this.trxAssembleQty = trxAssembleQty;
    }

    public Double getTrxScrappedQty() {
        return trxScrappedQty;
    }

    public void setTrxScrappedQty(Double trxScrappedQty) {
        this.trxScrappedQty = trxScrappedQty;
    }
}
