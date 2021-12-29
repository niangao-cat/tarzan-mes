package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by sen.luo on 2019/3/12.
 */
public class MtWoComponentActualVO2 implements Serializable {

    private static final long serialVersionUID = 1821474262896788252L;

    private String workOrderComponentActualId;
    private String eventId;
    private String workOrderId;
    private String materialId;
    private String operationId;
    private Double assembleQty;
    private Double trxAssembleQty;
    private Double scrappedQty;
    private Double trxScrappedQty;
    private String componentType;
    private String bomComponentId;
    private String bomId;
    private String routerStepId;
    private String assembleExcessFlag;
    private String assembleRouterType;
    private String substituteFlag;
    private Date actualFirstTime;
    private Date actualLastTime;

    public String getWorkOrderComponentActualId() {
        return workOrderComponentActualId;
    }

    public void setWorkOrderComponentActualId(String workOrderComponentActualId) {
        this.workOrderComponentActualId = workOrderComponentActualId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
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

    public Double getTrxAssembleQty() {
        return trxAssembleQty;
    }

    public void setTrxAssembleQty(Double trxAssembleQty) {
        this.trxAssembleQty = trxAssembleQty;
    }

    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    public Double getTrxScrappedQty() {
        return trxScrappedQty;
    }

    public void setTrxScrappedQty(Double trxScrappedQty) {
        this.trxScrappedQty = trxScrappedQty;
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
        return actualFirstTime == null ? null : (Date) actualFirstTime.clone();
    }

    public void setActualFirstTime(Date actualFirstTime) {
        this.actualFirstTime = actualFirstTime == null ? null : (Date) actualFirstTime.clone();
    }

    public Date getActualLastTime() {
        return actualLastTime == null ? null : (Date) actualLastTime.clone();
    }

    public void setActualLastTime(Date actualLastTime) {
        this.actualLastTime = actualLastTime == null ? null : (Date) actualLastTime.clone();
    }
}

