package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by HP on 2019/3/14.
 */
public class MtWoComponentActualVO11 implements Serializable {
    private static final long serialVersionUID = 5378621655945039979L;

    private String workOrderComponentActualId;
    private String workOrderId;
    private String materialId;
    private String operationId;
    private Double assembleQty;
    private Double scrappedQty;
    private Double requirementQty;
    private String bomComponentId;
    private String componentMaterialId;
    private Double componentRequirementQty;
    private String bomId;
    private String routerStepId;
    private Date actualFirstTime;
    private Date actualLastTime;

    public String getWorkOrderComponentActualId() {
        return workOrderComponentActualId;
    }

    public void setWorkOrderComponentActualId(String workOrderComponentActualId) {
        this.workOrderComponentActualId = workOrderComponentActualId;
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

    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    public Double getRequirementQty() {
        return requirementQty;
    }

    public void setRequirementQty(Double requirementQty) {
        this.requirementQty = requirementQty;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getComponentMaterialId() {
        return componentMaterialId;
    }

    public void setComponentMaterialId(String componentMaterialId) {
        this.componentMaterialId = componentMaterialId;
    }

    public Double getComponentRequirementQty() {
        return componentRequirementQty;
    }

    public void setComponentRequirementQty(Double componentRequirementQty) {
        this.componentRequirementQty = componentRequirementQty;
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
